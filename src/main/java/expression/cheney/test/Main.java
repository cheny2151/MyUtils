package expression.cheney.test;

import expression.cheney.ExpressionExecutor;
import expression.cheney.ReflectExpressionParser;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

/**
 * @author cheney
 * @date 2019-12-07
 */
public class Main {

    @Test
    public void test() {
        ReflectExpressionParser expressionParser = ReflectExpressionParser.getInstance();
        ExpressionExecutor expressionExecutor = expressionParser.parseExpression("date_format(date,getString())");
        HashMap<String, Object> env = new HashMap<>();
        env.put("date",new Date());
        System.out.println(expressionExecutor.execute(env));
    }

}
