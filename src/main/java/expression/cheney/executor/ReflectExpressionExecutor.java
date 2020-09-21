package expression.cheney.executor;

import expression.cheney.exception.ExpressionExecuteException;
import expression.cheney.model.Arg;
import expression.cheney.model.FunctionClasses;
import expression.cheney.model.ParseResult;
import expression.cheney.parse.AviatorExpressionParser;
import reflect.methodHolder.MethodHolder;
import reflect.methodHolder.StaticMethodHolder;
import reflect.methodHolder.exception.NoSuchMethodException;
import reflect.methodHolder.factory.MethodHolderFactory;

import java.util.List;
import java.util.Map;

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
    private FunctionClasses functionClasses;

    public ReflectExpressionExecutor(String express, ParseResult parseResult,
                                     MethodHolderFactory methodHolderFactory, FunctionClasses functionClasses) {
        super(express, parseResult);
        this.methodHolderFactory = methodHolderFactory;
        this.functionClasses = functionClasses;
    }

    @Override
    public Object execute(Map<String, Object> env) {
        try {
            return executeFunc(parseResult.getFuncName(), parseResult.getArgs(), env);
        } catch (RuntimeException e) {
            throw new ExpressionExecuteException(express, e);
        }
    }

    @Override
    protected Object executeOperation(String expression, Map<String, Object> env, boolean cache) {
        expression = expression.replace("null", "nil");
        AviatorExpressionParser aviator = AviatorExpressionParser.getInstance();
        ExpressionExecutor executor = cache ? aviator.parseExpressionWithCache(expression) : aviator.parseExpression(expression);
        return executor.execute(env);
    }

    @Override
    protected Object executeFunc(String functionName, List<Arg> args, Map<String, Object> env) {
        for (FunctionClasses.FunctionClass functionClass : functionClasses) {
            MethodHolder methodHolder = methodHolderFactory.getMethodHolder(functionClass.getFuncClass(), StaticMethodHolder.class);
            if (methodHolder.hasMethod(functionName)) {
                return methodHolder.invoke(functionName, null, loadArgs(args, env));
            }
        }
        throw new NoSuchMethodException(functionName);
    }

}
