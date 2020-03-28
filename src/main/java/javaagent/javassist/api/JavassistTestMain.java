package javaagent.javassist.api;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author cheney
 * @date 2020-03-25
 */
public class JavassistTestMain {

    @Test
    public void test() throws Exception {
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

    /**
     * 方法代理demo
     */
    @Test
    public void test2() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("javaagent.javassist.api.JavassistTest");
        CtMethod test2 = cc.getDeclaredMethod("test2", new CtClass[]{CtClass.intType});
        String resultTypeName = test2.getReturnType().getName();
        System.out.println(resultTypeName);
        String methodName = test2.getName() + "$agent";
        CtMethod agentMethod = CtNewMethod.copy(test2, methodName, cc, null);
        cc.addMethod(agentMethod);
        test2.setBody("{" +
                "System.out.println(\"before\");" +
                resultTypeName + " r = -1;" +
                "try {" +
                "r=" + methodName + "($$);" +
                "}catch(Throwable e) {" +
                "System.out.println(\"catch exception:\"+e.getMessage());" +
                "}" +
                "System.out.println(\"after\");" +
                "return r;" +
                "}");
        cc.toClass();
        JavassistTest javassistTest = new JavassistTest();
        System.out.println(javassistTest.test2(0));
    }

}
