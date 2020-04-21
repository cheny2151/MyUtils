package reflect.methodHolder;

/**
 * MethodHolder工厂类
 *
 * @author cheney
 * @date 2019-12-05
 */
public class ReadWriteMethodHolderFactory extends DefaultMethodHolderFactory {

    // 全局静态工厂
    private final static ReadWriteMethodHolderFactory GLOBAL_READ_WRITE_METHOD_HOLDER_FACTORY = new ReadWriteMethodHolderFactory();

    public MethodHolder getMethodHolder(Class<?> clazz) {
        return methodHolderCache.computeIfAbsent(clazz, key -> registeredClass(clazz));
    }

    public MethodHolder registeredClass(Class<?> clazz) {
        return registeredClass(clazz, ReadWriteMethodHolder.class);
    }

    /**
     * 获取全局实例
     *
     * @return 工厂实例
     */
    public static ReadWriteMethodHolderFactory getInstance() {
        return GLOBAL_READ_WRITE_METHOD_HOLDER_FACTORY;
    }

}
