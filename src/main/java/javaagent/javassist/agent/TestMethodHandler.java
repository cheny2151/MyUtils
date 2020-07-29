package javaagent.javassist.agent;

import javassist.util.proxy.MethodHandler;

import java.lang.reflect.Method;

/**
 * @author cheney
 * @date 2020-07-12
 */
public class TestMethodHandler implements MethodHandler {
    @Override
    public Object invoke(Object o, Method method, Method proceed, Object[] objects) throws Throwable {
        String name = method.getName();
        Object r2 = proceed.invoke(o, objects);
        if (name.contains("Name")) {
            r2 = r2 + "test";
        }
        return r2;
    }
}
