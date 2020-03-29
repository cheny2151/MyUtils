package javaagent.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * @author cheney
 * @date 2020-03-29
 */
public class CompilerTest {

    private static String source = "/source/CompileEntity.java";

    public static void main(String[] args) throws Exception {
        String basePath = new File(CompilerTest.class.getClassLoader().getResource("").getFile()).getAbsolutePath();
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        // -d "指定生成base目录" 目标java类(base目录+java类的package就是生成的class文件位置)
        int run = javaCompiler.run(null, null, null,
                "-d",
                // 只需指定基础路径，具体类路径在类的package声明上
                basePath,
                basePath + source);
        System.out.println(run == 0);
        Class<?> aClass = CompilerTest.class.getClassLoader().loadClass("javaagent.compiler.CompileEntity");
        Object o = aClass.getConstructor().newInstance();
        Method setTest = aClass.getDeclaredMethod("setTest", String.class);
        setTest.invoke(o, "1");
        Method getTest = aClass.getDeclaredMethod("getTest");
        System.out.println(getTest.invoke(o));
    }

}
