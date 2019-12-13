package expression.cheney;

import expression.cheney.func.InternalFunction;
import reflect.methodHolder.MethodHolder;
import reflect.methodHolder.MethodHolderFactory;
import reflect.methodHolder.StatusMethodHolder;
import reflect.methodHolder.exception.NoSuchMethodException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 解析表达式执行反射方法
 *
 * @author cheney
 * @date 2019-12-06
 */
public class ReflectExpressionExecutor extends BaseExpressionExecutor {

    /**
     * 方法反射工厂
     */
    private MethodHolderFactory methodHolderFactory;

    /**
     * 反射的类
     */
    private Set<Class<?>> functionClasses;

    ReflectExpressionExecutor(String express, String functionName, List<BaseExpressionParser.Arg> args,
                              MethodHolderFactory methodHolderFactory, Set<Class<?>> functionClasses) {
        super(express, functionName, args);
        this.methodHolderFactory = methodHolderFactory;
        if (functionClasses == null) {
            functionClasses = new HashSet<>();
        }
        // 添加内置函数类
        functionClasses.add(InternalFunction.class);
        this.functionClasses = functionClasses;
    }

    @Override
    public Object execute(Map<String, Object> env) {
        return executeFunc(functionName, args, env);
    }

    @Override
    protected Object executeOperation(String expression, Map<String, Object> env) {
        return AviatorExpressionParser.getInstance().parseExpression(expression).execute(env);
    }

    @Override
    protected Object executeFunc(String functionName, List<BaseExpressionParser.Arg> args, Map<String, Object> env) {
        for (Class<?> clazz : functionClasses) {
            MethodHolder methodHolder = methodHolderFactory.getMethodHolder(clazz, StatusMethodHolder.class);
            if (methodHolder.hasMethod(functionName)) {
                return methodHolder.invoke(functionName, null, loadArgs(args, env));
            }
        }
        throw new NoSuchMethodException(functionName);
    }

}
