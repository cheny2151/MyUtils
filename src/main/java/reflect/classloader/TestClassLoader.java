package reflect.classloader;

import org.junit.Test;
import reflect.ReflectUtils;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;

/**
 * @author by chenyi
 * @date 2022/1/7
 */
public class TestClassLoader {

    @Test
    public void testMyClassLoader() {
        HashMap<String, String> map = new HashMap<>();
        String className = "com.yy.shopline.translation.common.utils.IpUtils";
        map.put(className, "/Users/chenyi/IdeaProjects/shopline/translation/translation-common/target/classes/com/yy/shopline/translation/common/utils/IpUtils.class");
        while (true) {
            try {
                MyClassLoader myClassLoader = new MyClassLoader(map);
                Class<?> aClass1 = myClassLoader.loadClass(className);
                Object getHost = ReflectUtils.getMethod(aClass1, "getHost").invoke(null);
                System.out.println(getHost);
                Thread.sleep(2000);
            } catch (Throwable e) {

            }
        }
    }

    @Test
    public void testMyUrlClassLoader() {
        String path = "file:/Users/chenyi/IdeaProjects/shopline/address-matching-api/target/address-matching-api-0.0.3.jar";
        while (true) {
            try {
                MyUrlClassLoader myUrlClassLoader = new MyUrlClassLoader(new URL[]{new URL(path)});
                Class<?> aClass = myUrlClassLoader.loadClass("com.joyy.address.api.entity.MatchQuery");
                System.out.println(aClass);
                Object o = ReflectUtils.newObject(aClass, null, null);
                Method test = ReflectUtils.getMethod(aClass, "test");
                Object invoke = test.invoke(o);
                System.out.println(invoke);
                Thread.sleep(2000);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
