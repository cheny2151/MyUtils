package expression.cheney.test;

import expression.cheney.AviatorExpressionParser;
import expression.cheney.ExpressionExecutor;
import expression.cheney.ExpressionParser;
import expression.cheney.ReflectExpressionParser;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheney
 * @date 2019-12-07
 */
public class Main {

    @Test
    public void test() {
        ExpressionParser expressionParser = ReflectExpressionParser.getInstance();
        ExpressionExecutor expressionExecutor = expressionParser.parseExpression("date_format(date,getString('test','test',1+2))");
        HashMap<String, Object> env = new HashMap<>();
        env.put("date", new Date());
        System.out.println(expressionExecutor.execute(env));
    }


    @Test
    public void test2() {
        Map<String, Object> args = new HashMap<>();
        args.put("a", BigDecimal.valueOf(1));
        args.put("b", BigDecimal.valueOf(2));
        args.put("c", "A");
        args.put("a1", BigDecimal.valueOf(2));
        args.put("b1", BigDecimal.valueOf(1));
        args.put("c1", "B");
        AviatorExpressionParser aviatorExpressionParser = AviatorExpressionParser.getInstance();
        ExpressionExecutor executor = aviatorExpressionParser.parseExpression("ifs(a>b-a,c,a1>b1,c1)");
        Object execute = executor.execute(args);
        System.out.println(execute);
    }

    @Test
    public void test3() {
        Map<String, Object> env = new HashMap<>();
        env.put("a", BigDecimal.valueOf(1));
        env.put("b", BigDecimal.valueOf(2));
        env.put("c", 10);
        env.put("a1", BigDecimal.valueOf(2));
        env.put("b1", BigDecimal.valueOf(1));
        env.put("c1", 100);
        ExpressionParser expressionParser = ReflectExpressionParser.getInstance();
        ExpressionExecutor expressionExecutor = expressionParser.parseExpression("ifs(!(a>b-a),c1-c,a1>b1,c-c1)");
        System.out.println(expressionExecutor.execute(env).getClass());
    }

}
