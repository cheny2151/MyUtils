package scan;

import expression.cheney.test.Main;

import java.net.URL;
import java.util.List;

/**
 * @author cheney
 * @date 2019-12-27
 */
public class TestMain {

    public static void main(String[] args) {
        test0();
        System.out.println("--------------");
        test1();
        System.out.println("--------------");
        test2();
    }

    public static void test0() {
        List<Class<?>> classes = PathScan.scanClass("");
        classes.forEach(clazz -> System.out.println(clazz.getSimpleName()));
    }

    public static void test1() {
        List<Class<?>> classes = PathScan.scanClass("expression.cheney");
        classes.forEach(clazz -> System.out.println(clazz.getSimpleName()));
    }

    public static void test2() {
        Class<? extends Main> aClass = Main.class;
        URL resource = aClass.getResource("../func");
        System.out.println(resource.getFile());
        List<Class<?>> classes = PathScan.scanClass(resource);
        classes.forEach(System.out::println);
    }

}
