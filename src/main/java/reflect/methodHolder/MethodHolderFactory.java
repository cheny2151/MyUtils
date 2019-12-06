package reflect.methodHolder;

/**
 * MethodHolder接口
 *
 * @author cheney
 * @date 2019-12-05
 */
public interface MethodHolderFactory {

    MethodHolder getMethodHolder(Class<?> clazz, Class<? extends MethodHolder> methodHolderClass);

    MethodHolder registeredClass(Class<?> clazz, Class<? extends MethodHolder> methodHolderClass);
}
