package SPI;

import reflect.methodHolder.factory.MethodHolderFactory;

import java.util.ServiceLoader;

/**
 * SPI使用实例:
 * {@link ServiceLoader#load(java.lang.Class)}
 * 会到META-INF/services的配置文件中寻找这个接口对应的实现类全路径名，
 * 然后使用Class.forName()（传入设定的类加载器）完成类的加载。
 *
 * @author cheney
 * @date 2020-08-19
 */
public class ServiceLoaDemo {

    public static void main(String[] args) {
        // 加载META-INF\services\reflect.methodHolder.factory.MethodHolderFactory文件中配置的类
        ServiceLoader<MethodHolderFactory> services = ServiceLoader.load(MethodHolderFactory.class);
        for (MethodHolderFactory service : services) {
            System.out.println(service.getClass());
        }
    }

}
