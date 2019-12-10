package expression.cheney;

import com.googlecode.aviator.Expression;

import java.util.Map;

/**
 * @author cheney
 * @date 2019-12-10
 */
public class AviatorExpressionExecutor implements ExpressionExecutor {

    private Expression expression;

    public AviatorExpressionExecutor(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object execute(Map<String, Object> env) {
        return expression.execute(env);
    }

    public Expression getExpression() {
        return expression;
    }
}
