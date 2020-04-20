package reflect.methodHolder;

/**
 * MethodHolder工厂类
 *
 * @author cheney
 * @date 2019-12-05
 */
public class WriteReadMethodHolderFactory extends DefaultMethodHolderFactory {

    public MethodHolder getMethodHolder(Class<?> clazz) {
        return methodHolderCache.computeIfAbsent(clazz, key -> registeredClass(clazz));
    }

    public MethodHolder registeredClass(Class<?> clazz) {
        return registeredClass(clazz, ReadWriteMethodHolder.class);
    }

}
