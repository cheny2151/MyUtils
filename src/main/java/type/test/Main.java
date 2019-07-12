package type.test;

import org.junit.Test;
import type.TypeReference;

import java.lang.reflect.Type;

/**
 * @author cheney
 * @date 2019/6/27
 */
public class Main {

    @Test
    public void main() {
        getType(new TypeTest());
    }

    private <T> T getType(TypeReference<T> typeReference) {
        Type actualType = typeReference.getActualType();
        System.out.println(actualType);
        return null;
    }

}
