package expression.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * aviator表达式使用例子
 *
 * @author cheney
 * @date 2019-12-06
 */
public class TestMain {

    @Test
    public void test1() {
        Map<String, Object> args = new HashMap<>();
        args.put("a", 1);
        args.put("b", 2);
        args.put("c", 3);
        Object execute = AviatorEvaluator.execute("a>b?a:b>c?b:c", args);
        System.out.println(execute);
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
        AviatorEvaluator.addFunction(new TestAviatorFunction());
        Object execute = AviatorEvaluator.execute("ifs(a>b,c,a1>b1,c1)", args);
        System.out.println(execute);
    }

    public static class TestAviatorFunction extends AbstractVariadicFunction {

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
}
