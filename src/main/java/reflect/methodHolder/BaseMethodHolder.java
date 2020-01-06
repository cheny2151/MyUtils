package reflect.methodHolder;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import reflect.methodHolder.exception.MethodHolderInvokeException;
import reflect.methodHolder.exception.NoSuchMethodException;

import java.lang.reflect.Array;
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
            Class<?> parameterType;
            if (args == null) {
                // 参数为null时填充null数组为入参
                return method.invoke(obj, createNullArray(parameterCount));
            } else if (parameterCount > 0
                    && (parameterType = method.getParameterTypes()[parameterCount - 1]).isArray()) {
                // 不定参数必在最后一位,对不定参数进行参数修复
                Class<?> componentType = parameterType.getComponentType();
                return method.invoke(obj, castToObjectArray(args, componentType, parameterCount));
            } else {
                return method.invoke(obj, args);
            }
        } catch (Exception e) {
            throw new MethodHolderInvokeException("执行方法" + holdClass.getSimpleName() + "#" + methodName + "异常，" +
                    "方法入参:" + JSON.toJSONString(args), e);
        }
    }

    /**
     * 修复不定参数,将最后一项参数(不定参数)包装为array，其他参数不变copy出新的Object[]
     *
     * @param args           参数
     * @param type           不定参数类型
     * @param parameterCount 方法参数个数
     * @return 修复后的数据
     */
    private Object[] castToObjectArray(Object[] args, Class<?> type, int parameterCount) {
        Object[] fixArgs = new Object[parameterCount];
        int defineNum = parameterCount - 1;
        // 非不定参数不变，copy
        System.arraycopy(args, 0, fixArgs, 0, defineNum);
        // 通过反射将不定参数包装到array中，存到fixArgs最后一位
        Object array = Array.newInstance(type, args.length - parameterCount + 1);
        int index = 0;
        for (int i = defineNum; i < args.length; i++) {
            Array.set(array, index++, args[i]);
        }
        fixArgs[defineNum] = array;
        return fixArgs;
    }

    /**
     * 生成空的Object array
     *
     * @param count 个数
     * @return 空array
     */
    private Object[] createNullArray(int count) {
        Object[] nullArray = new Object[count];
        for (int i = 0; i < count; i++) {
            nullArray[i] = null;
        }
        return nullArray;
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
