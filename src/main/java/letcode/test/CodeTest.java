package letcode.test;

/**
 * 代码考试题
 *
 * @author cheney
 * @date 2020-09-18
 */
public class CodeTest {

    /**
     * 字符串有三种编辑操作:插入一个字符、删除一个字符或者替换一个字符。 给定两个字符串，编写一个函数判定它们是否只需要一次(或者零次)编辑。
     *
     * 示例
     *
     * 输入:
     * first = "pale"
     * second = "ple"
     * 输出: True
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/one-away-lcci
     * @param first
     * @param second
     * @return
     */
    public static boolean oneEditAway(String first, String second) {
        int fl = first.length();
        int x = fl - second.length();
        if (x > 1 || x < -1) {
            return false;
        }
        String iterate;
        String match;
        if (x >= 0) {
            iterate = first;
            match = second;
        } else {
            iterate = second;
            match = first;
        }
        // 复用fl
        fl = match.length();
        boolean fail = false;
        Character m;
        char c;
        int i1;
        int offset = 0;
        for (int i = 0; i < iterate.length(); ) {
            c = iterate.charAt(i);
            i1 = i + offset;
            m = i1 >= fl || i1 < 0 ? null : match.charAt(i1);
            if (m != null && c == m) {
                i++;
                continue;
            }
            if (fail) {
                if (offset == 0) {
                    offset = -1;
                    continue;
                } else if (offset == -1) {
                    offset = 1;
                    continue;
                }
                return false;
            }
            i++;
            fail = true;
        }
        return offset == 0 || x != 0;
    }

}
