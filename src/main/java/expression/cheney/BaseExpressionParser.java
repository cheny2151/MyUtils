package expression.cheney;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static expression.cheney.BaseExpressionExecutor.OPERATORS;

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
 * @author cheney
 * @date 2019-12-07
 */
public abstract class BaseExpressionParser implements ExpressionParser {

    private final static char COMMA_CHAR = ",".toCharArray()[0];

    private final static char BRACKETS_LEFT_CHAR = "(".toCharArray()[0];

    private final static char BRACKETS_RIGHT_CHAR = ")".toCharArray()[0];

    private final static char APOSTROPHE_CHAR = "'".toCharArray()[0];

    private final static char SPACE_CHAR = " ".toCharArray()[0];

    /**
     * 可为结尾的字符
     */
    private final static char[] END_CHAR = new char[]{COMMA_CHAR, BRACKETS_RIGHT_CHAR, APOSTROPHE_CHAR};

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
        private boolean noFunc;

        public static ParseResult noFunc() {
            return new ParseResult(null, null, true);
        }

        public static ParseResult func(String funcName, List<Arg> args) {
            return new ParseResult(funcName, args, false);
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
        // 是否常量
        private boolean constant;
        // 是否函数
        private boolean func;

        public static Arg create(Object value, boolean constant, boolean func) {
            if (func) {
                String function = (String) value;
                String substring = function.substring(0, function.indexOf("("));
                if (OPERATORS.matcher(substring).matches()) {
                    // 方法名包含运算符则视为运算
                    func = false;
                } else {
                    // 参数为方法表达式,执行方法表达式解析
                    value = parse(function);
                }
            } else if (!constant) {
                // 不为方法不为常类的arg，去除前后的空格
                value = ((String) value).trim();
            }
            return new Arg(value, constant, func);
        }
    }

    /**
     * 解析方法表达式
     *
     * @param expression 表达式
     * @return 解析结果 ParseResult实体
     */
    protected static ParseResult parse(String expression) {
        if (StringUtils.isEmpty(expression)) {
            throw new ExpressionParseException("expression can not be empty");
        }
        int start = expression.indexOf("(");
        int length = expression.length();
        if (start == -1 || start == 0 || ")".toCharArray()[0] != expression.charAt(length - 1)) {
            // 不包含()则不为函数
            return ParseResult.noFunc();
        }
        List<Arg> args = parseArg(expression.substring(start + 1, length - 1));

        return ParseResult.func(expression.substring(0, start), args);
    }

    /**
     * 解析方法表达式中的参数段类
     *
     * @param expression 方法的参数段类,例:ifs(a>b,c)-->'a>b,c'
     * @return 参数解析结果 Arg集合
     */
    private static List<Arg> parseArg(String expression) {
        char[] chars = expression.toCharArray();
        int length = chars.length;
        int endIndex = length - 1;
        // 语句开始位置
        Integer startIndex = null;
        // 语句结尾检查字符
        Character endCheck = null;
        int count = 0;
        List<Arg> result = new ArrayList<>();
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
                // 结束
                if (endCheck != null) {
                    if (c == endCheck) {
                        if (APOSTROPHE_CHAR == endCheck) {
                            if (count == 2) {
                                // 出现第二个'结束
                                result.add(Arg.create(expression.substring(startIndex + 1, i), true, false));
                                count = 0;
                            }
                        } else if (BRACKETS_RIGHT_CHAR == endCheck) {
                            if (count == 1) {
                                // 匹配')'时只有一个未匹配的'('结束
                                result.add(Arg.create(expression.substring(startIndex, ++i), false, true));
                            }
                            count--;
                        }
                    }
                    if (end && count != 0) {
                        // 到达结尾时，当前char无法匹配endCheck时抛出异常
                        throw new ExpressionParseException("end char miss '" + endCheck + "'");
                    }
                } else {
                    // 不需要endCheck
                    if (end) {
                        i++;
                    } else if (startIndex == i) {
                        continue;
                    }
                    result.add(Arg.create(expression.substring(startIndex, i), false, false));
                }
                if (count == 0) {
                    // 匹配结束符并且count为0时,标识段落结束
                    startIndex = null;
                    endCheck = null;
                }
            }
        }
        return result;
    }

}
