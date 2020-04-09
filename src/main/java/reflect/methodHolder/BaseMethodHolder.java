package reflect.methodHolder;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import reflect.methodHolder.exception.MethodHolderInvokeException;
import reflect.methodHolder.exception.NoSuchMethodException;
import reflect.methodHolder.model.MetaMethodCollect;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    protected ConcurrentHashMap<String, MetaMethodCollect> methodMap;

    public BaseMethodHolder(Class<?> clazz) {
        this.holdClass = clazz;
        this.methodMap = new ConcurrentHashMap<>();
    }

    @Override
    public void cacheMethod(Method method) {
        if (method == null)
            return;
        String name = method.getName();
        methodMap.computeIfAbsent(name, key -> new MetaMethodCollect(holdClass, key)).add(method);
    }

    @Override
    public Object invoke(String methodName, Object obj, Object... args) {
        return invoke(null, methodName, obj, args);
    }

    @Override
    public Object invoke(Class<?> returnType, String methodName, Object obj, Object... args) {
        Class<?>[] classes = extractClass(args);
        Optional<Method> methodOpt = speculateMethod(methodName, returnType, classes);
        Method method = methodOpt.orElseThrow(() -> new NoSuchMethodException(methodName));
        return invoke(method, obj, args);
    }


    /**
     * 反射调用目标方法
     *
     * @param method 方法
     * @param obj    实例
     * @param args   参数
     * @return 返回值
     */
    public Object invoke(Method method, Object obj, Object... args) {
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
            throw new MethodHolderInvokeException("执行方法:" + holdClass.getSimpleName() + "#" + method.getName() + "异常，" +
                    "方法入参:" + JSON.toJSONString(args), e);
        }
    }

    @Override
    public boolean hasMethod(String methodName) {
        return methodMap.containsKey(methodName);
    }

    @Override
    public Optional<Method> getMethod(Class<?> returnType, String methodName, Class<?>... parameterTypes) {
        MetaMethodCollect metaMethodCollect = methodMap.get(methodName);
        return metaMethodCollect == null ? Optional.empty() : Optional.ofNullable(metaMethodCollect.exactMethod(returnType, parameterTypes));
    }

    @Override
    public Optional<Method> getMethod(String methodName, Class<?>... parameterTypes) {
        MetaMethodCollect metaMethodCollect = methodMap.get(methodName);
        return metaMethodCollect == null ? Optional.empty() : Optional.ofNullable(metaMethodCollect.exactMethodByArgs(parameterTypes));
    }

    @Override
    public Optional<Method> getMethod(String name) {
        MetaMethodCollect metaMethodCollect = methodMap.get(name);
        return metaMethodCollect == null ? Optional.empty() : Optional.ofNullable(metaMethodCollect.exactMethodByName());
    }

    @Override
    public Optional<Method> speculateMethod(String methodName, Class<?> returnType, Class<?>... args) {
        MetaMethodCollect metaMethodCollect = methodMap.get(methodName);
        return metaMethodCollect == null ? Optional.empty() : Optional.ofNullable(metaMethodCollect.speculateMethod(returnType, args));
    }

    /**
     * 获取持有方法的类
     *
     * @return 缓存方法对应的类
     */
    public Class<?> getHoldClass() {
        return holdClass;
    }

    /**
     * 获取方法签名
     *
     * @param method         方法
     * @param withReturnType 是否包含返回类型
     * @return 方法签名
     */
    public static String getSignature(Method method, boolean withReturnType) {
        StringBuilder builder = new StringBuilder();
        if (withReturnType) {
            Class<?> returnType = method.getReturnType();
            if (returnType != null) {
                builder.append(returnType.getName()).append("#");
            }
        }
        builder.append(method.getName());
        if (method.getParameterCount() > 0) {
            builder.append(":");
            String args = Stream.of(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining(","));
            builder.append(args);
        }
        return builder.toString();
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
     * 提取出对象数组的类型数组
     *
     * @param args 参数集合
     * @return 参数类型数组
     */
    private Class<?>[] extractClass(Object[] args) {
        return Stream.of(args).map(Object::getClass).toArray(Class[]::new);
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

}
