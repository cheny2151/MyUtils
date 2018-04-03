package attention;

/**
 * 静态内部类实例化过程
 *
 * @author cheney 初始化静态成员变量,父类静态代码块->初始化静态成员变量,内部子类静态代码块->初始化普通成员变量->构造函数
 */
public class Base {

    private final static String a = "test";

    static {
        System.out.println("父类Base的静态代码块被调用了！");
        System.out.println(a);
    }

    private String baseName = "base";

    public Base() {
        System.out.println("父类Base的构造方法被调用了！");
        callName();
    }

    public void callName() {
        System.out.println("父类Base的callName()方法被调用了！");
        System.out.println(baseName);
    }

    static class Sub extends Base {

        static {
            System.out.println("子类Sub的静态代码块被调用了！");
        }

        public Sub() {
            System.out.println("子类Sub的构造方法被调用了！");
        }

        private String baseName = "sub";

        public void callName() {
            System.out.println("子类Sub的callName()方法被调用了！");
            System.out.println(baseName);
        }
    }

    public static void main(String[] args) {
        Sub b = new Sub();
        System.out.println(b.baseName);
    }
}
