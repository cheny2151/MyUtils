package expression.cheney;

/**
 * 解析者接口
 *
 * @author cheney
 * @date 2019-12-10
 */
public interface ExpressionParser {

    ExpressionExecutor parseExpression(String expression);

}
