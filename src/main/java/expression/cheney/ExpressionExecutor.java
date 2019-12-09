package expression.cheney;

import java.util.List;
import java.util.Map;

/**
 * @author cheney
 * @date 2019-12-06
 */
public abstract class ExpressionExecutor {

    // 表达式
    protected String express;

    // 方法名
    protected String functionName;

    // 参数
    protected List<ExpressionParser.Arg> args;

    ExpressionExecutor(String express, String functionName, List<ExpressionParser.Arg> args) {
        this.express = express;
        this.functionName = functionName;
        this.args = args;
    }

    public Object[] loadArgs(Map<String, Object> env) {
        return args.stream().map(e -> e.isConstant() ? e.getValue() : env.get((String) e.getValue()))
                .toArray();
    }

    public abstract Object execute(Map<String, Object> env);

}
