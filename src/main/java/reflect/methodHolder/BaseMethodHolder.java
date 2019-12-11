package reflect.methodHolder;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基础MethodHolder实现类
 *
 * @author cheney
 * @date 2019-12-06
 */
@Slf4j
public abstract class BaseMethodHolder implements MethodHolder {

    // 持有方法所属类
    private Class<?> holdClass;

    // 方法缓存Map
    protected ConcurrentHashMap<String, Method> methodMap;

    public BaseMethodHolder(Class<?> clazz) {
        this.holdClass = clazz;
        this.methodMap = new ConcurrentHashMap<>();
    }

    @Override
    public Object invoke(String methodName, Object obj, Object... args) {
        Optional<Method> methodOpt = getMethod(methodName);
        Method method = methodOpt.orElseThrow(() -> new NoSuchMethodException(methodName));
        try {
            int parameterCount = method.getParameterCount();
            if (args != null && args.length > parameterCount && parameterCount == 1) {
                return method.invoke(obj, new Object[]{args});
            } else {
                return method.invoke(obj, args);
            }
        } catch (Exception e) {
            throw new MethodHolderInvokeException(holdClass.getSimpleName() + "执行方法#" + methodName + "异常", e);
        }
    }

    @Override
    public boolean hasMethod(String methodName) {
        return methodMap.containsKey(methodName);
    }

    /**
     * 通过方法名获取方法
     *
     * @param name 方法名
     * @return 方法
     */
    public Optional<Method> getMethod(String name) {
        return Optional.ofNullable(methodMap.get(name));
    }

    /**
     * 缓存方法
     *
     * @param method 方法
     */
    public void cacheMethod(Method method) {
        if (method == null)
            return;
        methodMap.put(method.getName(), method);
    }

    public Class<?> getHoldClass() {
        return holdClass;
    }

}
