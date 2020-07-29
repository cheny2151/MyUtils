package javaagent.javassist.agent;

import DesignPattern.observer.single.Student;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author cheney
 * @date 2020-07-12
 */
public class AgentTest {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(Student.class);
        Object proxy = proxyFactory.create(new Class[]{String.class, String.class, String.class}, new Object[]{"1", "test", "man"}, new TestMethodHandler());
        System.out.println(proxy.getClass());
        Student student = (Student) proxy;
        System.out.println(student.getName());
    }

}
