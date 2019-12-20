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

    /**
     * 可为结尾的字符
     */
    final static char[] END_CHAR = new char[]{COMMA_CHAR, BRACKETS_RIGHT_CHAR, APOSTROPHE_CHAR};

    // 运算符正则
    final static Pattern OPERATORS = Pattern.compile(".*([+\\-*/%?><=|&!]).*");

    // 数字正则
    final static Pattern NUMBER = Pattern.compile("\\d+(\\.?\\d+)?");

}
