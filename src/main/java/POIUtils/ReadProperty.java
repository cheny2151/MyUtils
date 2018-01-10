package POIUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 存放有@ExcelData注解的字段信息
 */
public class ReadProperty<T> {

    private String name;

    private Class<?> propertyClass;

    private Field field;

    private Method writerMethod;

    public ReadProperty(String name, Class<?> propertyClass, Field field, Method writerMethod) {
        this.name = name;
        this.propertyClass = propertyClass;
        this.field = field;
        this.writerMethod = writerMethod;
    }

    public String getName() {
        return name;
    }

    public Class<?> getPropertyClass() {
        return propertyClass;
    }

    public Field getField() {
        return field;
    }

    public Method getWriterMethod() {
        return writerMethod;
    }

    public synchronized void writerUnkownTypeValue(T t, Object value) throws InvocationTargetException, IllegalAccessException {

        this.getWriterMethod().invoke(t, value);
    }

}
