package expression.cheney;

import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
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

    // 运算符正则
    final static Pattern OPERATORS = Pattern.compile(".*([+\\-*/%?><=|&!]).*");

    // 数字正则
    final static Pattern NUMBER = Pattern.compile("\\d+(\\.?\\d+)?");

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
            if (value == null) {
                return null;
            } else if (arg.isConstant()) {
                return value;
            } else if (arg.isFunc()) {
                BaseExpressionParser.ParseResult parseResult = (BaseExpressionParser.ParseResult) value;
                return executeFunc(parseResult.getFuncName(), parseResult.getArgs(), env);
            } else {
                String valueStr = (String) value;
                Object envArg = env.get(valueStr);
                if (envArg != null) {
                    return envArg;
                } else if (OPERATORS.matcher(valueStr).matches()) {
                    // 结合Aviator,将含运算符的arg丢给Aviator执行
                    return executeOperation(valueStr, env);
                }
                return castToBasic(valueStr);
            }
        }).toArray();
    }

    /**
     * 尝试将变量转换基本类型数据
     *
     * @param valueStr 待转换的值
     * @return
     */
    private static Object castToBasic(String valueStr) {
        if ("null".equals(valueStr) || "nil".equals(valueStr)) {
            return null;
        } else if ("false".equals(valueStr) || "true".equals(valueStr)) {
            return Boolean.valueOf(valueStr);
        } else if (NUMBER.matcher(valueStr).matches()) {
            if (valueStr.contains(".")) {
                return new BigDecimal(valueStr);
            } else {
                return Integer.valueOf(valueStr);
            }
        }
        return null;
    }

    /**
     * 执行运算表达式
     *
     * @param expression 运算表达式
     * @param env        实际参数
     * @return 表达式执行结果
     */
    protected abstract Object executeOperation(String expression, Map<String, Object> env);

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
