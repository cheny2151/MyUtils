package expression.cheney;

/**
 * 解析者接口
 *
 * @author cheney
 * @date 2019-12-10
 */
public interface Parser {

    ExpressionExecutor parseExpression(String expression);

}
