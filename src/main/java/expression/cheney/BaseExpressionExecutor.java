package expression.cheney;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 表达式执行器
 *
 * @author cheney
 * @date 2019-12-06
 */
public abstract class BaseExpressionExecutor implements ExpressionExecutor {

    final static Pattern OPERATORS = Pattern.compile(".*([+\\-*/%?><=|&!]).*");

    // 表达式
    protected String express;

    // 方法名
    protected String functionName;

    // 参数
    protected List<BaseExpressionParser.Arg> args;

    BaseExpressionExecutor(String express, String functionName, List<BaseExpressionParser.Arg> args) {
        this.express = express;
        this.functionName = functionName;
        this.args = args;
    }

    /**
     * 从env中提取arg对应的参数
     *
     * @param args 表达式参数
     * @param env  实际参数
     * @return 参数数组
     */
    protected Object[] loadArgs(List<BaseExpressionParser.Arg> args, Map<String, Object> env) {
        return CollectionUtils.isEmpty(args) ? null : args.stream().map(arg -> {
            Object value = arg.getValue();
            if (arg.isConstant()) {
                return value;
            } else if (arg.isFunc()) {
                BaseExpressionParser.ParseResult parseResult = (BaseExpressionParser.ParseResult) value;
                return executeFunc(parseResult.getFuncName(), parseResult.getArgs(), env);
            } else {
                Object envArg = env.get((String) value);
                if (envArg != null) {
                    return envArg;
                } else if (OPERATORS.matcher((String) value).matches()) {
                    // 结合Aviator,将含运算符的arg丢给Aviator执行
                    return AviatorExpressionParser.getInstance().parseExpression((String) value).execute(env);
                }
                return null;
            }
        }).toArray();
    }

    public abstract Object execute(Map<String, Object> env);

    /**
     * 提供额外的表达式执行方法
     *
     * @param functionName 方法名
     * @param args         参数
     * @param env          变量
     * @return 表达式执行结果
     */
    protected abstract Object executeFunc(String functionName, List<BaseExpressionParser.Arg> args, Map<String, Object> env);

}
