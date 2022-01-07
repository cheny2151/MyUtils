package reflect.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author by chenyi
 * @date 2022/1/7
 */
public class MyUrlClassLoader extends URLClassLoader {

    public MyUrlClassLoader(URL[] urls) {
        super(urls);
    }

}
