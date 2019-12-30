package expression.cheney;

import expression.cheney.func.InternalFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static expression.cheney.CharConstants.*;
import static expression.cheney.func.InternalFunction.OUT_PUT_FUNC_NAME;

/**
 * 表达式解析器抽象接口,提供基础的解析方法实现
 * <p>
 * 表达式解析支持:
 * 1.解析方法表达式(funcA(xx));
 * 2:解析''为分隔符的字符串(funcA('xx'));
 * 3.解析变量(funA(a),a将视为变量);
 * 4.嵌套函数解析(例如：funcA(funcB(funcC()))，通过递归实现)
 * <p>
 * 解析的结果封装为{@link ParseResult},一个方法表达式为一个ParseResult，
 * 例如funcA(funcB()，解析结果ParseResult实体中，其成员变量args{@link ParseResult.args}为单个元素的{@link Arg}，
 * 元素Arg中的成员变量{@link Arg.value}为funB解析结果的实体{@link ParseResult}，形成嵌套。
 *
 * 1.1 新增支持运算符嵌套函数解析,见{@link #createArg(List, Arg, Object, short)}}
 * 1.2 新增支持解析方法名为运算符--运算符表达式,见{@link #createArg(List, Arg, Object, short)}};
 *     优化关键字符',(缺失/位置非法时时抛出异常。
 * 1.3 完美支持原始类型(包含运算符)与函数的组合（组合段落）。
 * 1.4 组合段落新支持常量，组合段落支持常量、原始类型、函数与运算符之间的组合（COMBINATION组合段落）。
 * 1.5 支持最外层为段落组合，通过拼接输出函数{@link InternalFunction#output(java.lang.Object)}实现，代码见{@link #parse(java.lang.String)}。
 *
 * @version 1.3
 * @author cheney
 * @date 2019-12-07
 */
public abstract class BaseExpressionParser implements ExpressionParser {

    @Override
    public abstract ExpressionExecutor parseExpression(String expression);

    /**
     * 表达式解析结果
     * 例如: ifs(a>b,c) 则
     * args字段：a>b与c {@link Arg}
     * funcName字段：ifs
     * noFunc字段:当表达式不为函数时，该字段为true
     */
    @Data
    @AllArgsConstructor
    protected static class ParseResult {
        private String funcName;
        private List<Arg> args;
        private short type;
        // 类型枚举值
        public final static short FUNC = 1;
        public final static short ORIGIN = 2;

        /**
         * 原始类型时,funcName作为完整的原始类型表达式
         *
         * @param expression 表达式
         */
        public static ParseResult origin(String expression) {
            return new ParseResult(expression, null, ORIGIN);
        }

        public static ParseResult func(String funcName, List<Arg> args) {
            return new ParseResult(funcName, args, FUNC);
        }

        public boolean isFunc() {
            return FUNC == this.type;
        }
    }

    /**
     * 参数段类实体
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Arg {
        // 值
        private Object value;
        // 类型：0:常量,1:函数,2:运算,3:组合段落
        private short type;
        // 类型枚举值
        public final static short CONSTANT = 0;
        public final static short FUNC = 1;
        public final static short ORIGIN = 2;
        public final static short COMBINATION = 3;
    }

    /**
     * 解析方法表达式
     * <p>
     * 1.判断表达式不存在函数(不匹配正则'字母 ('),则为原始类型ParseResult.ORIGIN
     * 2.（1.5新增）
     * 表达式首个非空字符为'(',或者达式首个'('前存在运算符，则拼接输出函数{@link InternalFunction#output(java.lang.Object)}，
     * 再执行解析{@link #parseArg(java.lang.String)}。
     * 3.表达式首个'('前不存在运算符,则为函数直接执行解析{@link #parseArg(java.lang.String)}。
     *
     * @param expression 表达式
     * @return 解析结果 ParseResult实体
     */
    protected ParseResult parse(String expression) {
        expression = expression.trim();
        if (StringUtils.isEmpty(expression)) {
            throw new ExpressionParseException("expression can not be empty");
        }
        if (!CONTAINS_FUNC.matcher(expression).find()) {
            // 不包含函数，则为原始类型
            return ParseResult.origin(expression);
        }
        int start = expression.indexOf("(");
        int length = expression.length();
        String funcName = expression.substring(0, start);
        String content;
        if ("".equals(funcName) || CONTAINS_OPERATOR_PATTERN.matcher(funcName).find()) {
            // 1.5：表达式以'('开头或者表达式为段落，则拼接输出函数
            funcName = OUT_PUT_FUNC_NAME;
            content = expression;
        } else {
            content = expression.substring(start + 1, length - 1);
        }
        List<Arg> args = parseArg(content);

        return ParseResult.func(funcName, args);
    }

    /**
     * 解析方法表达式中的参数段类
     *
     * @param expression 方法的参数段类,例:ifs(a>b,c)-->'a>b,c'
     * @return 参数解析结果 Arg集合
     */
    private List<Arg> parseArg(String expression) {
        expression = expression.trim();
        char[] chars = expression.toCharArray();
        int length = chars.length;
        int endIndex = length - 1;
        // 语句开始位置
        Integer startIndex = null;
        // 语句结尾检查字符
        Character endCheck = null;
        int count = 0;
        List<Arg> result = new ArrayList<>();
        Arg partLast = null;
        for (int i = 0; i < length; i++) {
            char c = chars[i];
            if (SPACE_CHAR == c) {
                // 跳过无用空格
                continue;
            }
            if (startIndex == null && COMMA_CHAR != c) {
                // 段落开始
                startIndex = i;
            }
            if (APOSTROPHE_CHAR == c && (endCheck == null || endCheck == APOSTROPHE_CHAR)) {
                // 当前char为'
                if (startIndex != null && chars[startIndex] != APOSTROPHE_CHAR) {
                    // 1.2:'出现在段落中间时，之前的段落为原始类型的COMBINATION段落组合
                    partLast = createArg(result, partLast, expression.substring(startIndex, i), Arg.ORIGIN);
                    startIndex = i;
                }
                endCheck = APOSTROPHE_CHAR;
                count++;
            }
            if (BRACKETS_LEFT_CHAR == c && (endCheck == null || endCheck == BRACKETS_RIGHT_CHAR)) {
                // 当前char为(,每遇到一个(加一，没遇到一个)减一，直到最后一个)视为结束
                if (endCheck == null) {
                    endCheck = BRACKETS_RIGHT_CHAR;
                }
                count++;
            }
            boolean end = i == endIndex;
            if ((ArrayUtils.contains(END_CHAR, c) || end) && startIndex != null) {
                // 匹配结束符
                if (endCheck != null) {
                    if (c == endCheck) {
                        if (APOSTROPHE_CHAR == endCheck) {
                            if (count == 2) {
                                // 出现第二个'结束
                                partLast = createArg(result, partLast, expression.substring(startIndex + 1, i), Arg.CONSTANT);
                                count = 0;
                            }
                        } else if (BRACKETS_RIGHT_CHAR == endCheck) {
                            if (count == 1) {
                                // 匹配')'时只有一个未匹配的'('结束
                                partLast = createArg(result, partLast, expression.substring(startIndex, i + 1), Arg.FUNC);
                            }
                            count--;
                        }
                    }
                    if (end && count != 0) {
                        // 到达结尾时，无法匹配完endCheck时抛出异常
                        throw new ExpressionParseException(expression.substring(startIndex, i + 1) + " : miss end char \"" + endCheck + "\"");
                    }
                } else {
                    // 不需要endCheck
                    if (end) {
                        i++;
                    } else if (startIndex == i) {
                        continue;
                    }
                    String part = expression.substring(startIndex, i);
                    // 段落尾部非法字符检查检查
                    checkEndPartChar(part, c, end);
                    partLast = createArg(result, partLast, part, Arg.ORIGIN);
                }
                if (count == 0) {
                    // 匹配结束符并且count为0时,标识段落结束
                    startIndex = null;
                    endCheck = null;
                    if (c == COMMA_CHAR) {
                        partLast = null;
                    }
                }
            } else if (c == COMMA_CHAR && count == 0 && partLast != null) {
                // 段落结束
                partLast = null;
            }
        }
        return result;
    }

    /**
     * 创建arg/添加arg，并返回最新的arg
     * <p>
     * version1.1 新增
     * version1.2 新增解析 方法名为运算符:运算符表达式
     *
     * @param argResult arg集合
     * @param partLast  此段落上一个arg
     * @param value     新arg值
     * @param type      新arg类型
     * @return 最新arg
     */
    private Arg createArg(List<Arg> argResult, Arg partLast, Object value, short type) {
        boolean createNew = true;
        if (Arg.FUNC == type) {
            // 函数
            String function = (String) value;
            int startIndex = function.indexOf("(");
            String funcName = function.substring(0, startIndex).trim();
            boolean emptyFuncName = "".equals(funcName);
            if (emptyFuncName || ORIGIN_PATTERN.matcher(funcName).matches()) {
                /* 方法名为运算符结尾，则function为原生ORIGIN(type:2)+'无名函数'(content),
                    content不包含函数则为原生ORIGIN(type:2)
                    content包含函数则拼接输出函数作为FUNC(type:1)
                   1:partLast(此段落前一个arg)不为空，该原生ORIGIN(type:2)与count作为'组合段落(COMBINATION)'的一部分
                   2:partLast为空，则新建List存入原生ORIGIN(type:2)与count作为新arg*/
                String content = function.substring(startIndex);
                List<Arg> args;
                if (partLast != null) {
                    createNew = false;
                    args = argToOperatorFunc(partLast);
                } else {
                    type = Arg.COMBINATION;
                    args = new ArrayList<>();
                    value = args;
                }
                if (!emptyFuncName) {
                    args.add(new Arg(funcName, Arg.ORIGIN));
                }
                if (!CONTAINS_FUNC.matcher(content).find()) {
                    // 不包含函数，则为原始类型
                    args.add(new Arg(content, Arg.ORIGIN));
                } else {
                    // 包含函数的content拼接输出函数名，形成一个输出结果的函数
                    args.add(new Arg(parse(OUT_PUT_FUNC_NAME + content), Arg.FUNC));
                }
            } else if (CONTAINS_OPERATOR_PATTERN.matcher(funcName).find()) {
                /* 方法名包含运算符，则将arg解析为一个List用来存'运算符嵌套函数组合段落(COMBINATION)':
                   List中按源运算表达式顺序存放两种arg实体,一种为原生ORIGIN(type:2),一种存函数FUNC(type:1)*/
                int splitIndex = findLastOperatorIndex(funcName) + 1;
                Arg operator = new Arg(function.substring(0, splitIndex).trim(), Arg.ORIGIN);
                // 解析去除运算符后的函数表达式,解析结果存为函数FUNC(type:1)
                ParseResult func = parse(function.substring(splitIndex).trim());
                Arg funcArg = new Arg(func, Arg.FUNC);
                /* 最后将新生成的函数arg与运算符arg放入对应的List中
                   1:partLast(此段落前一个arg)不为空，该运算符嵌套函数表达式作为'组合段落(COMBINATION)'的一部分
                   2:partLast为空，则新建List存入运算符arg与函数arg作为新arg*/
                if (partLast != null) {
                    createNew = false;
                    List<Arg> args = argToOperatorFunc(partLast);
                    args.add(operator);
                    args.add(funcArg);
                } else {
                    type = Arg.COMBINATION;
                    List<Arg> newArgs = new ArrayList<>();
                    newArgs.add(operator);
                    newArgs.add(funcArg);
                    value = newArgs;
                }
            } else {
                // 参数为单独一个函数表达式,执行方法表达式解析
                value = parse(function);
            }
        } else if (Arg.ORIGIN == type) {
            // 原始类型
            value = ((String) value).trim();
            if (partLast != null) {
                // 1.3 支持原始类型嵌套
                String expression = (String) value;
                createNew = false;
                if (!OPERATOR_START_PATTERN.matcher(expression).matches()) {
                    throw new ExpressionParseException("miss operators before \"" + value + "\"");
                }
                List<Arg> args = argToOperatorFunc(partLast);
                args.add(new Arg(expression, Arg.ORIGIN));
            }
        } else if (Arg.CONSTANT == type && partLast != null) {
            // 1.4:常量并且partLast不为空时，拼接成组合段落(COMBINATION)
            createNew = false;
            List<Arg> args = argToOperatorFunc(partLast);
            args.add(new Arg(value, Arg.CONSTANT));
        }

        if (createNew) {
            Arg newArg = new Arg(value, type);
            argResult.add(newArg);
            return newArg;
        }
        return partLast;
    }

    /**
     * 返回最后一个运算符的index
     * 优化为从后往前查找,提高性能
     * <p>
     * version1.1新增
     *
     * @param expression 表达式
     * @return 0:List<Arg> 1:运算符结束index
     */
    private int findLastOperatorIndex(String expression) {
        char[] chars = expression.toCharArray();
        for (int index = chars.length - 1; index >= 0; index--) {
            if (ArrayUtils.contains(OPERATORS, chars[index])) {
                return index;
            }
        }
        return -1;
    }


    /**
     * 将参数格式化为OperatorFunc类型
     *
     * @param arg Arg参数
     * @return OperatorFunc类型参数value
     */
    @SuppressWarnings("unchecked")
    private List<Arg> argToOperatorFunc(Arg arg) {
        Object partLastValue = arg.getValue();
        short partLastType = arg.getType();
        ArrayList<Arg> args;
        if (partLastValue.getClass() == ArrayList.class) {
            args = (ArrayList<Arg>) partLastValue;
        } else if (partLastValue.getClass() == ParseResult.class || partLastType == Arg.ORIGIN || partLastType == Arg.CONSTANT) {
            // 必须为函数才可嵌套运算符
            args = new ArrayList<>();
            args.add(new Arg(partLastValue, partLastType));
            arg.setValue(args);
            arg.setType(Arg.COMBINATION);
        } else {
            throw new ExpressionParseException("error type of last Arg:" + partLastValue);
        }
        return args;
    }

    /**
     * 段落尾部检查
     * <p>
     * 1.2 新增
     *
     * @param part    段落
     * @param endChar 尾部char
     * @param end     是否表达式结尾
     */
    private void checkEndPartChar(String part, char endChar, boolean end) {
        if (endChar == BRACKETS_RIGHT_CHAR) {
            throw new ExpressionParseException(patch(part, endChar, end) + " : miss start char \"" + BRACKETS_LEFT_CHAR + "\"");
        } else if (endChar == APOSTROPHE_CHAR) {
            throw new ExpressionParseException(patch(part, endChar, end) + " : miss start char \"" + APOSTROPHE_CHAR + "\"");
        }
    }

    /**
     * 补充结尾char，不全日志所需符号
     */
    private String patch(String part, char endChar, boolean end) {
        if (!end) {
            part += endChar;
        }
        return "\"" + part + "\"";
    }

}
