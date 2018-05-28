package thread.ThreadLocal;

import org.junit.Test;

public class ThreadLocalDemo {

    private ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    public ThreadLocalDemo() {
        this.setThreadLocal("default");
    }

    public void setThreadLocal(String local) {
        threadLocal.set(local);
    }

    public void show() {
        System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
    }

    @Test
    public void test() throws InterruptedException {
        ThreadLocalDemo threadLocalDemo = new ThreadLocalDemo();
        Thread thread = new Thread(() -> {
            threadLocalDemo.show();
            threadLocalDemo.setThreadLocal("new thread1");
            threadLocalDemo.show();
        });
        thread.start();
        Thread thread2 = new Thread(() -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadLocalDemo.show();
            threadLocalDemo.setThreadLocal("new thread2");
            threadLocalDemo.show();
        });
        thread2.start();
        thread2.join();
        threadLocalDemo.show();
    }


}
