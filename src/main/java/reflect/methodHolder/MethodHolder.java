package reflect.methodHolder;

/**
 * 方法反射缓存接口
 *
 * @author cheney
 * @date 2019-12-05
 */
public interface MethodHolder {

    Object invoke(String methodName, Object obj, Object... args);

    boolean hasMethod(String methodName);

}
