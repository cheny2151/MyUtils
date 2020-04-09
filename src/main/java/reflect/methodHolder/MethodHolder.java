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
     * 缓存方法
     *
     * @param method 方法
     */
    void cacheMethod(Method method);

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
     * 反射调用方法
     *
     * @param methodName 方法名
     * @param obj        实例
     * @param args       参数
     * @return 执行结果
     */
    Object invoke(Class<?> returnType, String methodName, Object obj, Object... args);

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
    Optional<Method> getMethod(Class<?> returnType, String methodName, Class<?>... parameterTypes);

    /**
     * 通过方法名、方法签名定位方法
     *
     * @param methodName     方法名
     * @param parameterTypes 参数类型数组
     * @return 匹配的方法
     */
    Optional<Method> getMethod(String methodName, Class<?>... parameterTypes);

    /**
     * 通过方法名获取方法
     *
     * @param methodName 方法名
     * @return 方法
     * @throws reflect.methodHolder.exception.FindNotUniqueMethodException 查询多个方法时抛出该异常
     */
    Optional<Method> getMethod(String methodName);

    /**
     * 通过给定方法名和其他参数推测方法
     *
     * @param methodName 方法名，必填
     * @param returnType 返回类型，可为空
     * @param args       参数类型数组，可为空
     * @return 方法
     * @throws reflect.methodHolder.exception.FindNotUniqueMethodException 查询多个方法时抛出该异常
     */
    Optional<Method> speculateMethod(String methodName, Class<?> returnType, Class<?>... args);

}
