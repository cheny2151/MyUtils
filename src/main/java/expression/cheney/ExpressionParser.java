package expression.cheney;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cheney
 * @date 2019-12-07
 */
public abstract class ExpressionParser {

    private final static char COMMA_CHAR = ",".toCharArray()[0];

    private final static char BRACKETS_LEFT_CHAR = "(".toCharArray()[0];

    private final static char BRACKETS_RIGHT_CHAR = ")".toCharArray()[0];

    private final static char APOSTROPHE_CHAR = "'".toCharArray()[0];

    private final static char SPACE_CHAR = " ".toCharArray()[0];

    private final static char[] END_CHAR = new char[]{COMMA_CHAR, BRACKETS_RIGHT_CHAR, APOSTROPHE_CHAR};

    public ParseResult parse(String expression) {
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

    abstract ExpressionExecutor parseExpression(String expression);

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

    public List<Arg> parseArg(String expression) {
        char[] chars = expression.toCharArray();
        int length = chars.length;
        int endIndex = length - 1;
        Integer startIndex = null;
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
                // 开始
                startIndex = i;
            }
            if (APOSTROPHE_CHAR == c) {
                endCheck = APOSTROPHE_CHAR;
                count++;
            }
            if (BRACKETS_LEFT_CHAR == c) {
                if (endCheck != null) {
                    if (endCheck == BRACKETS_RIGHT_CHAR) {
                        count++;
                    } else {
                        throw new ExpressionParseException();
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
                        throw new ExpressionParseException("end char miss '" + endCheck + "'");
                    }
                } else {
                    if (startIndex == i) {
                        continue;
                    }
                    if (end) {
                        i++;
                    }
                    result.add(new Arg(expression.substring(startIndex, i), false, false));
                }
                if (count == 0) {
                    startIndex = null;
                    endCheck = null;
                }
            }
        }
        return result;
    }

}
