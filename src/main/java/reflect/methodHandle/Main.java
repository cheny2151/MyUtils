package reflect.methodHandle;

import reflect.pojo.Admin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 句柄
 * @author cheney
 * @date 2019-09-02
 */
public class Main {

    public static void main(String[] args) throws Throwable {
        MethodType methodType = MethodType.methodType(void.class, String.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle setTest = lookup.findVirtual(Admin.class, "setAdminNumber", methodType);
        Admin admin = new Admin();
        setTest.invoke(admin, "value");
        System.out.println(admin.getAdminNumber());
    }

}
