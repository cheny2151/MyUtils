package reflect.methodHandle;

import reflect.pojo.Admin;
import reflect.pojo.People;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * 句柄
 * @author cheney
 * @date 2019-09-02
 */
public class Main {

    public static void main(String[] args) throws Throwable {
        MethodType methodType = MethodType.methodType(void.class, String.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        // 通过方法名查找句柄
        MethodHandle setTest = lookup.findVirtual(Admin.class, "setAdminNumber", methodType);
        Admin admin = new Admin();
        setTest.invoke(admin, "value");
        // band实例对象获取句柄
        MethodHandle getAdminNumberMehtodHandle = lookup.bind(admin, "getAdminNumber", MethodType.methodType(String.class));
        String getAdminNumber = (String) getAdminNumberMehtodHandle.invokeWithArguments();
        System.out.println(getAdminNumber);
        // 通过method获取句柄
        Method method = Admin.class.getDeclaredMethod("getAdminNumber");
        MethodHandle methodHandle = MethodHandles.lookup().unreflect(method);
        System.out.println(methodHandle.invoke(admin));
        // 通过句柄执行default方法
        Method run = People.class.getDeclaredMethod("run");
        MethodHandle runMethodHandle = MethodHandles.lookup().unreflect(run).bindTo(new People() {
        });
        runMethodHandle.invokeWithArguments();
    }

}
