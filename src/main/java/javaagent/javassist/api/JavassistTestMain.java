package javaagent.javassist.api;

import javassist.*;

import java.lang.reflect.Method;

/**
 * @author cheney
 * @date 2020-03-25
 */
public class JavassistTestMain {

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("javaagent.javassist.api.JavassistTest");
        CtMethod export = ctClass.getDeclaredMethod("test");
        export.addLocalVariable("a", CtClass.intType);
        export.insertBefore("a = 20;");
        export.insertAfter("System.out.println(a);");
        CtMethod test2 = CtNewMethod.make(CtClass.intType, "test2", new CtClass[0], new CtClass[0], "{return 30;}", ctClass);
        ctClass.addMethod(test2);
        ctClass.toClass();
//        Loader loader = new Loader(pool); 如果当前类加载器已经加载该类，则需要通过其他类加载器加载
//        Object expenseReportController = loader.loadClass("javaagent.javassist.api.JavassistTestMain").getConstructor().newInstance();
        Object javassistTest = new JavassistTest();
        // test
        Method test = javassistTest.getClass().getMethod("test");
        Object invoke = test.invoke(javassistTest);
        System.out.println(invoke);
        // test2
        Method test2M = javassistTest.getClass().getMethod("test2");
        Object invoke2 = test2M.invoke(javassistTest);
        System.out.println(invoke2);
    }

}
