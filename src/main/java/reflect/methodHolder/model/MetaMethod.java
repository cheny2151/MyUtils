package reflect.methodHolder.model;

import reflect.methodHolder.BaseMethodHolder;

import java.lang.reflect.Method;


/**
 * 方法--元数据
 *
 * @author cheney
 * @date 2020-04-01
 */
public class MetaMethod {

    private Method method;

    private Class<?> returnType;

    private String sign;

    private int argsNum;

    public MetaMethod(Method method) {
        this.method = method;
        this.returnType = method.getReturnType();
        this.sign = BaseMethodHolder.getSignature(method, false);
        this.argsNum = method.getParameterCount();
    }


    public Method getMethod() {
        return method;
    }

    public String getSign() {
        return sign;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public int getArgsNum() {
        return argsNum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !MetaMethod.class.equals(o.getClass())) {
            return false;
        }
        return ((MetaMethod) o).getMethod().equals(method);
    }

    @Override
    public int hashCode() {
        return method != null ? method.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MetaMethod{" +
                "method=" + method.getName() +
                ", returnType=" + returnType.getName() +
                ", sign='" + sign + '\'' +
                ", argsNum=" + argsNum +
                '}';
    }
}
