package reflect.classloader;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by chenyi
 * @date 2022/1/7
 */
public class MyClassLoader extends ClassLoader {

    private Map<String,String> classFMap;

    public MyClassLoader(Map<String, String> classFMap) {
        this.classFMap = classFMap;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classFMap.containsKey(name)) {
            String file = classFMap.get(name);
            try (FileInputStream stream = new FileInputStream(file)) {
                byte[] bytes = stream.readAllBytes();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new ClassNotFoundException();
    }
}
