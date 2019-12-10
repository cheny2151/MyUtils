package expression.cheney;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * Aviator表达式解析器
 *
 * @author cheney
 * @date 2019-12-10
 */
public class AviatorExpressionParser implements ExpressionParser {

    private AviatorEvaluatorInstance aviatorEvaluator;

    private static AviatorExpressionParser AviatorExpressionParser;

    private AviatorExpressionParser() {
        aviatorEvaluator = AviatorEvaluator.getInstance();
        init();
    }

    @Override
    public ExpressionExecutor parseExpression(String expression) {
        return new AviatorExpressionExecutor(aviatorEvaluator.compile(expression, true));
    }

    private void init() {
        aviatorEvaluator.addFunction(new IfsAviatorFunction());
    }

    private static class IfsAviatorFunction extends AbstractVariadicFunction {

        @Override
        public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
            if (args != null && args.length > 0 && (args.length & 1) == 0) {
                for (int i = 0; i < args.length; i++) {
                    if ((Boolean) args[i++].getValue(env)) {
                        return args[i];
                    }
                }
                return new AviatorString(null);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public String getName() {
            return "ifs";
        }
    }

    public static AviatorExpressionParser getInstance() {
        if (AviatorExpressionParser == null) {
            synchronized (AviatorExpressionParser.class) {
                if (AviatorExpressionParser == null) {
                    AviatorExpressionParser = new AviatorExpressionParser();
                }
            }
        }
        return AviatorExpressionParser;
    }

}
