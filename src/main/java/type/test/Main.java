package type.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import type.TypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author cheney
 * @date 2019/6/27
 */
public class Main {

    @Test
    public void main() {
        getType(new TypeReference<List<Map<String,Object>>>(){});
    }

    private <T> Type getType(TypeReference<T> typeReference) {
        Type actualType = typeReference.getActualType();
        if (actualType instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) actualType;
            System.out.println(parameterizedType.getRawType());
            System.out.println(JSON.toJSONString(parameterizedType.getActualTypeArguments()));
        }
        return actualType;
    }

}
