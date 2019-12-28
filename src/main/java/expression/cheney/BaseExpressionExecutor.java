package expression.cheney;

import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static expression.cheney.BaseExpressionParser.Arg.CONSTANT;
import static expression.cheney.BaseExpressionParser.Arg.FUNC;
import static expression.cheney.CharConstants.*;

/**
 * 表达式执行器
 *
 * @author cheney
 * @date 2019-12-06
 */
public abstract class BaseExpressionExecutor implements ExpressionExecutor {

    // 系统创建的环境值key
    public static final String NEW_ENV_KEY = "_ENV_KEY";
    // 表达式
    protected String express;

    // 方法名
    protected BaseExpressionParser.ParseResult parseResult;

    BaseExpressionExecutor(String express, BaseExpressionParser.ParseResult parseResult) {
        this.express = express;
        this.parseResult = parseResult;
    }

    /**
     * 从env中提取arg对应的参数
     *
     * @param args 表达式参数
     * @param env  实际参数
     * @return 参数数组
     */
    @SuppressWarnings("unchecked")
    protected Object[] loadArgs(List<BaseExpressionParser.Arg> args, Map<String, Object> env) {
        return CollectionUtils.isEmpty(args) ? null : args.stream().map(arg -> {
            Object value = arg.getValue();
            short type = arg.getType();
            if (value == null) {
                return null;
            } else if (type == CONSTANT) {
                return value;
            } else if (type == FUNC) {
                BaseExpressionParser.ParseResult parseResult = (BaseExpressionParser.ParseResult) value;
                return executeFunc(parseResult.getFuncName(), parseResult.getArgs(), env);
            } else if (type == BaseExpressionParser.Arg.COMBINATION) {
                // 函数嵌套运算
                ArrayList<BaseExpressionParser.Arg> funcArgs = (ArrayList<BaseExpressionParser.Arg>) value;
                String operatorExpression = "";
                int newEnvIndex = 0;
                for (BaseExpressionParser.Arg funcArg : funcArgs) {
                    Object argValue = funcArg.getValue();
                    short funcArgType = funcArg.getType();
                    if (funcArgType == FUNC) {
                        BaseExpressionParser.ParseResult parseResult = (BaseExpressionParser.ParseResult) argValue;
                        Object expression = executeFunc(parseResult.getFuncName(), parseResult.getArgs(), env);
                        // 函数执行结果作为env的值，缓存最终形成的表达式解析结果
                        String newEnvKey = NEW_ENV_KEY + newEnvIndex++;
                        env.put(newEnvKey, expression);
                        operatorExpression += newEnvKey;
                    } else if (funcArgType == CONSTANT) {
                        // 常量则加上'
                        operatorExpression += APOSTROPHE_STRING + argValue + APOSTROPHE_STRING;
                    } else {
                        // 运算符或者原始类型，直接拼接
                        operatorExpression += argValue;
                    }
                }
                return executeOperation(operatorExpression, env, true);
            } else {
                String valueStr = (String) value;
                Object envArg = env.get(valueStr);
                if (envArg != null) {
                    return envArg;
                } else if (CONTAINS_OPERATOR_PATTERN.matcher(valueStr).find()) {
                    // 结合Aviator,将含运算符的arg丢给Aviator执行
                    return executeOperation(valueStr, env, true);
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
    protected abstract Object executeOperation(String expression, Map<String, Object> env, boolean cache);

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
