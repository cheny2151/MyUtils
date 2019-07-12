package type;

import jsonUtils.JsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 泛型信息接口
 *
 * @author cheney
 * @date 2019/6/27
 */
public abstract class TypeReference<T> {

    private Type actualType;

    private Map<TypeVariable, Type> typeMap;

    public TypeReference() {
        this.typeMap = new HashMap<>();
        this.actualType = getActualType(this.getClass());
    }

    private Type getActualType(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        Class<?> next = null;
        if (genericSuperclass instanceof ParameterizedType) {
            // genericSuperclass为ParameterizedType，尝试提取泛型参数
            ParameterizedType typeAsParameterized = (ParameterizedType) genericSuperclass;
            extractTypeMap(typeAsParameterized);
            next = (Class<?>) (typeAsParameterized).getRawType();
            if (TypeReference.class.equals(next)) {
                System.out.println("next:" + next);
                System.out.println("next TypeParameters:" + JsonUtils.toJson(next.getTypeParameters()));
                System.out.println("next TypeParameters size:" + next.getTypeParameters().length);
                // genericSuperclass命中TypeReference，尝试从typeMap中获取actualType
                TypeVariable typeParameter = next.getTypeParameters()[0];
                System.out.println("typeParameter:" + typeParameter);
                System.out.println(typeMap);
                typeMap.forEach((k, v) -> {
                    System.out.println(k + ":" + k.equals(typeParameter));
                });
                Optional<Map.Entry<TypeVariable, Type>> actualType =
                        typeMap.entrySet().stream().filter(entry -> entry.getKey().equals(typeParameter)).findFirst();
                if (actualType.isPresent()) {
                    this.actualType = actualType.get().getValue();
                    return this.actualType;
                }
            }
        } else if (genericSuperclass instanceof Class) {
            next = (Class<?>) genericSuperclass;
        }

        if (next == null) {
            throw new RuntimeException("TypeReference fail to get actualType");
        }

        return getActualType(next);
    }

    /**
     * 提取ParameterizedType的TypeArguments与ActualTypeArguments之间的映射关系
     *
     * @param classAsParameterized ParameterizedType
     */
    private void extractTypeMap(ParameterizedType classAsParameterized) {
        Type[] actualTypeArguments = classAsParameterized.getActualTypeArguments();
        Class<?> rawType = (Class<?>) classAsParameterized.getRawType();
        TypeVariable<? extends Class<?>>[] typeParameters = rawType.getTypeParameters();
        for (int i = 0; i < typeParameters.length; i++) {
            Type actualTypeArgument = actualTypeArguments[i];
            if (actualTypeArgument instanceof TypeVariable) {
                Optional<Type> typeChange = Optional.empty();
                for (Map.Entry<TypeVariable, Type> entry : typeMap.entrySet()) {
                    if (entry.getKey().equals(actualTypeArgument)) {
                        typeChange = Optional.of(entry.getValue());
                        break;
                    }
                }
                if (typeChange.isPresent()) {
                    actualTypeArgument = typeChange.get();
                }
            }
            typeMap.put(typeParameters[i], actualTypeArgument);
        }
    }

    public Type getActualType() {
        return actualType;
    }
}
