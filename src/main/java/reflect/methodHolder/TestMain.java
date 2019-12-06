package reflect.methodHolder;

import org.junit.Test;
import reflect.ReflectUtils;

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
        MethodHolder methodHolder = holderFactory.getMethodHolder(ReflectUtils.class, StatusMethodHolder.class);
        System.out.println(methodHolder.invoke("field", new Object(), DefaultMethodHolderFactory.class, "methodHolderCache"));
    }

}
