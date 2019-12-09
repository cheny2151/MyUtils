package expression.cheney;

import jsonUtils.JsonUtils;
import reflect.methodHolder.MethodHolder;
import reflect.methodHolder.MethodHolderFactory;
import reflect.methodHolder.NoSuchMethodException;
import reflect.methodHolder.StatusMethodHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 解析表达式执行反射方法
 *
 * @author cheney
 * @date 2019-12-06
 */
public class ReflectExpressionExecutor extends ExpressionExecutor {

    private MethodHolderFactory methodHolderFactory;

    private Set<Class<?>> functionClasses;

    ReflectExpressionExecutor(String express, String functionName, List<ExpressionParser.Arg> args,
                              MethodHolderFactory methodHolderFactory, Set<Class<?>> functionClasses) {
        super(express, functionName, args);
        this.methodHolderFactory = methodHolderFactory;
        this.functionClasses = functionClasses;
    }

    @Override
    public Object execute(Map<String, Object> env) {
        for (Class<?> clazz : functionClasses) {
            MethodHolder methodHolder = methodHolderFactory.getMethodHolder(clazz, StatusMethodHolder.class);
            if (methodHolder.hasMethod(functionName)) {
                return methodHolder.invoke(functionName, null, loadArgs(env));
            }
        }
        throw new NoSuchMethodException(functionName);
    }

}
