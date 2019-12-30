package expression.cheney;

import java.util.regex.Pattern;

/**
 * 字符常量
 *
 * @author cheney
 * @date 2019-12-20
 */
public final class CharConstants {

    final static char COMMA_CHAR = ",".toCharArray()[0];

    final static char BRACKETS_LEFT_CHAR = "(".toCharArray()[0];

    final static char BRACKETS_RIGHT_CHAR = ")".toCharArray()[0];

    final static char APOSTROPHE_CHAR = "'".toCharArray()[0];

    final static char SPACE_CHAR = " ".toCharArray()[0];

    final static char ADD = "+".toCharArray()[0];

    final static char REDUCE = "-".toCharArray()[0];

    final static char MULTIPLY = "*".toCharArray()[0];

    final static char DIVIDE = "/".toCharArray()[0];

    final static char NON = "!".toCharArray()[0];

    final static char AND = "&".toCharArray()[0];

    final static char OR = "|".toCharArray()[0];

    final static char LE = "<".toCharArray()[0];

    final static char GE = ">".toCharArray()[0];

    final static char EQUAL = "=".toCharArray()[0];

    final static String APOSTROPHE_STRING = "'";

    final static String BRACKETS_LEFT_STRING = "(";

    /**
     * 可为结尾的字符
     */
    final static char[] END_CHAR = new char[]{COMMA_CHAR, BRACKETS_RIGHT_CHAR, APOSTROPHE_CHAR};

    /**
     * 运算符
     */
    final static char[] OPERATORS = new char[]{ADD, REDUCE, MULTIPLY, DIVIDE, NON, AND, OR, LE, GE, EQUAL};

    /**
     * 运算符开头正则
     */
    final static Pattern OPERATOR_START_PATTERN = Pattern.compile("^([+\\-*/%?><=|&!]).*");

    /**
     * 运算符结尾正则
     */
    final static Pattern ORIGIN_PATTERN = Pattern.compile(".*[+\\-*/%?&|><=!]$");

    /**
     * 单个运算符正则
     */
    final static Pattern OPERATOR_PATTERN = Pattern.compile("(([+\\-*/%?]|>=?|<=?)\\s*)|((==|!=?|&{1,2}|\\|{1,2})(\\s*!*)?)");

    /**
     * 一个或多个合法运算符正则(运算符前后不能重复出现运算符)
     */
    final static Pattern LEGITIMATE_OPERATOR_PATTERN
            = Pattern.compile("(((?![+\\-*/%?&|><=]).)*((([+\\-*/%?]|>=?|<=?)\\s*)|((==|!=?|&{1,2}|\\|{1,2})(\\s*!*)?))((?![+\\-*/%?&|><=]).)+)+");

    /**
     * 存在运算符正则
     */
    final static Pattern CONTAINS_OPERATOR_PATTERN = Pattern.compile("[+\\-*/%?><=|&!]");

    /**
     * 存在函数正则
     */
    final static Pattern CONTAINS_FUNC = Pattern.compile("(?<=[a-zA-Z])\\s*\\(");

    /**
     * 数字正则
     */
    final static Pattern NUMBER = Pattern.compile("\\d+(\\.?\\d+)?");

}
