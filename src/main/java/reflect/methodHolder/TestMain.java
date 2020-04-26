package reflect.methodHolder;

import org.junit.Test;
import reflect.ReflectUtils;
import reflect.methodHolder.factory.DefaultMethodHolderFactory;

/**
 * 测试类
 *
 * @author cheney
 * @date 2019-12-06
 */
public class TestMain {

    @Test
    public void test() {
        DefaultMethodHolderFactory holderFactory = new DefaultMethodHolderFactory();
        MethodHolder methodHolder = holderFactory.getMethodHolder(ReflectUtils.class, StaticMethodHolder.class);
        System.out.println(methodHolder.invoke("field", new Object(), DefaultMethodHolderFactory.class, "methodHolderCache"));
    }

}
