package reflect.methodHolder;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 方法反射缓存接口
 *
 * @author cheney
 * @date 2019-12-05
 */
public interface MethodHolder {

    /**
     * 反射调用方法
     *
     * @param methodName 方法名
     * @param obj        实例
     * @param args       参数
     * @return 执行结果
     */
    Object invoke(String methodName, Object obj, Object... args);

    /**
     * 类是否持有此方法名
     *
     * @param methodName 方法名
     * @return 是否存在匹配此方法名的方法
     */
    boolean hasMethod(String methodName);

    /**
     * 通过方法名、返回类型、方法签名定位方法
     *
     * @param methodName     方法名
     * @param returnType     返回类型
     * @param parameterTypes 参数类型数组
     * @return 匹配的方法
     */
    Optional<Method> getMethod(String methodName, Class<?> returnType, Class<?>... parameterTypes);

    /**
     * 通过方法名、方法签名定位方法
     *
     * @param methodName     方法名
     * @param parameterTypes 残念是类型数组
     * @return 匹配的方法
     */
    Optional<Method> getMethod(String methodName, Class<?> parameterTypes);

    /**
     * 通过方法名获取方法
     *
     * @param methodName 方法名
     * @return 方法
     */
    Optional<Method> getMethod(String methodName);

}
