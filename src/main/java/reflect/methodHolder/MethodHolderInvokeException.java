package reflect.methodHolder;

/**
 * 方法执行异常
 *
 * @author cheney
 * @date 2019-12-06
 */
public class MethodHolderInvokeException extends MethodHolderReflectException {

    public MethodHolderInvokeException(String message, Throwable cause) {
        super(message, cause);
    }
}
