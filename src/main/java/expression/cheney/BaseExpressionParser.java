package expression.cheney;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 表达式解析器抽象接口
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

    private final static char[] END_CHAR = new char[]{COMMA_CHAR, BRACKETS_RIGHT_CHAR, APOSTROPHE_CHAR};

    public abstract BaseExpressionExecutor parseExpression(String expression);

    @Data
    @AllArgsConstructor
    protected static class ParseResult {
        private String funcName;
        private List<Arg> args;
    }

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
    }

    protected ParseResult parse(String expression) {
        if (StringUtils.isEmpty(expression)) {
            throw new ExpressionParseException("expression can not be empty");
        }
        int start = expression.indexOf("(");
        int length = expression.length();
        if (start == -1 || start == 0 || ")".toCharArray()[0] != expression.charAt(length - 1)) {
            throw new ExpressionParseException("error expression,miss function main");
        }
        List<Arg> args = parseArg(expression.substring(start + 1, length - 1));

        return new ParseResult(expression.substring(0, start), args);
    }

    private List<Arg> parseArg(String expression) {
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
            if (SPACE_CHAR == c && endCheck != null && APOSTROPHE_CHAR != endCheck) {
                // 跳过无用空格
                continue;
            }
            if (startIndex == null && COMMA_CHAR != c) {
                // 段落开始
                startIndex = i;
            }
            if (APOSTROPHE_CHAR == c) {
                // 当前char为'
                endCheck = APOSTROPHE_CHAR;
                count++;
            }
            if (BRACKETS_LEFT_CHAR == c) {
                // 当前char为(,每遇到一个(加一，没遇到一个)减一，直到最后一个)视为结束
                if (endCheck != null) {
                    if (endCheck == BRACKETS_RIGHT_CHAR) {
                        count++;
                    } else {
                        throw new ExpressionParseException("'('与')'数量不匹配");
                    }
                } else {
                    endCheck = BRACKETS_RIGHT_CHAR;
                    count++;
                }
            }
            boolean end = i == endIndex;
            if (startIndex != null && (ArrayUtils.contains(END_CHAR, c) || end)) {
                // 结束
                if (endCheck != null) {
                    if (c == endCheck) {
                        if (APOSTROPHE_CHAR == endCheck) {
                            if (count == 2) {
                                // 出现第二个'结束
                                result.add(new Arg(expression.substring(startIndex + 1, i), true, false));
                                count = 0;
                            }
                        } else if (BRACKETS_RIGHT_CHAR == endCheck) {
                            if (count == 1) {
                                // 匹配')'时只有一个未匹配的'('结束
                                result.add(new Arg(parse(expression.substring(startIndex, ++i)), false, true));
                            }
                            count--;
                        }
                    } else if (count == 0) {
                        // 当前char不为endCheck时抛出异常
                        throw new ExpressionParseException("end char miss '" + endCheck + "'");
                    }
                } else {
                    // 不需要endCheck
                    if (startIndex == i) {
                        continue;
                    }
                    if (end) {
                        i++;
                    }
                    result.add(new Arg(expression.substring(startIndex, i), false, false));
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
