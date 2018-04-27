package pattern;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * 一，字符组匹配单个字符：
 * 1、我们用一对中括号（[.....]）表示字符组，整个字符组中会有多个字符位列其中，该字符组表示的含义是：
 * 匹配任意一个字符，该字符是位列字符组中的。例如：[single]匹配的是字符's','i','n','g','l','e'中的任意一个字符。
 * 以上我们简单介绍了字符组的基本概念以及它所能匹配的内容，其实有时候为了表述连续的字符，我们会结合元字符 '-' 一起来操作字符组。
 * 例如：[0123456789]，匹配的是0到9之间的任意一个数字，对于这种情况我们可以选择这样来简化操作：[0-9]。
 * 其实两者表述的含义是一样的，为了简化起见，如果遇到连续的字符表述，可以选择使用元字符来简化。同样的还有[a-z]，它匹配任意一个小写字母。
 * 对于元字符 '-' 还需要说明一点的是：该字符只有出现在两个字符之间才具有特殊含义，单独出现在字符组的所有字符之前或者之后只能表述普通字符 '-' 。
 * 2、元字符 '^' 表示排除的意思，和元字符 '-' 类似，只有放在所有字符的最前面才具有特殊含义，否则只能表示普通字符。
 * 例如：[^1234]，该字符组匹配一个字符，但是不是1或2或3或4。当然，[c^yy]，匹配的是四个普通字符，'c','^','y','y'。
 * \d：等同于字符组 [0-9]，表示任意一个数字字符
 * \w：较为常见，等同于字符组[0-9a-zA-Z]，表示任意一个world（单词字符）
 * \s：等同于[ \t\n\x0B\f\r]，匹配的是一个空格字符（space）
 * 当然，它们也有相对应的大写形式，但是表示的意思却是截然相反的。
 * \D：等同于[^0-9]，表示一个任意非数字字符
 * \W：等同于[^0-9a-zA-Z]，表示任意一个非单词字符，往往会是一些特殊符号
 * \S：等同于[^\t\n\x0B\f\r]，匹配一个任意非空格的字符
 * <p>
 * 二，用于指定字符多次出现的量词：
 * +：该元字符指定位于元字符前面的普通字符可以出现一次或者多次。
 * *：该元字符指定位于元字符前面的普通字符可以出现零次或多次。
 * ?：该元字符指定位于元字符前面的普通字符可以出现也可以不出现，但是不能多次出现。
 * {m,n}是通用量词的最基本形式，它指定前面的字符出现的次数在m到n之间。
 * se{0,10}cyy：其中e可以出现0-10次
 * se{9}cyy：其中e必须出现9次
 * se{0,}cyy：其中e可以出现0-无穷大次，等同于se*cyy。
 * <p>
 * 三，分组划分组别
 * ()括号表示分组
 * 结合元字符 '| '，可以实现和字符组一样的功效，(happy|cyy|single)：该正则表达式可以匹配三个字符子串，happy,cyy,single。
 * 在表达式 (A)(B(C)) 中，存在四个这样的组：
 * 0 ： (A)(B(C))
 * 1 ： (A)
 * 2 ： (B(C))
 * 3 ： (C)
 * 注意:组零始终代表整个表达式；使用\1对引号这个分组引用；为分组命名的语法格式为：(?<name>X)，引用分组的语法格式为：\k<name>。例如：<(?<num1>a)>(.*)</\k<num1>>：等效于：<a>(.*)</a>
 * <p>
 * 四，边界匹配
 * 元字符 ^ ，在字符组中，该元字符表示否定的意思，此处匹配正则表达式首部位置边界;
 * 元字符 $ ，匹配的字符串的尾部边界，它规定被匹配的字符串必须以什么结束。
 */
public class Pattern {

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 提取信息例子
     */
    @Test
    public void test() {
        //提取id
        String message = "http://localhost/test?id=10&name=test&id=20";
        String regex = "id=(\\d+)";
        java.util.regex.Pattern compile = java.util.regex.Pattern.compile(regex);
        Matcher matcher = compile.matcher(message);
        while (matcher.find()) {
            String group = matcher.group(1);
            System.out.println(group);
        }
    }

    @Test
    public void test2() {
        //提取id
        String message = "http://localhost/test?id=10&name=test&pId=20";
        String regex = "[?&]?(\\w+)=(\\w+)";
        java.util.regex.Pattern compile = java.util.regex.Pattern.compile(regex);
        Matcher matcher = compile.matcher(message);
        HashMap<String, String> map = new HashMap<>();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            map.put(key,value);
        }
        System.out.println(map);
    }

}
