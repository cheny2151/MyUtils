package javaagent.javassist.api;

/**
 * @author cheney
 * @date 2020-03-25
 */
public class JavassistTest {

    public int test() {
        return 10;
    }
    public int test2(int test) {
        System.out.println("=========");
        if (test == 0) {
            throw new RuntimeException("throw test");
        }
        return test;
    }

}
