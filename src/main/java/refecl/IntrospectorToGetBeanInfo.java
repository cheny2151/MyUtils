package refecl;

import org.junit.Test;
import refecl.pojo.Admin;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Introspector可以获取BeanInfo(包括父类的信息)(当自己实现反射时，应该注意通过 for (Class<?> cl = start; cl != Object.class; cl = cl.getSuperclass())遍历除了Object所有的父类)
 * spring的beanUtils基于此实现，再加上cash
 * 注意：如果只有字段而没有对应名称的get||set 则不会生成PropertyDescriptor
 * 如果没有字段但是有get || set 会生成PropertyDescriptor
 * 所以PropertyDescriptor是用来读取get||set方法的工具类
 * <p>
 * PropertyDescriptor可以获取字段的读写方法
 */
public class IntrospectorToGetBeanInfo {

    @Test
    public void test() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(Admin.class);
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            System.out.println(propertyDescriptor.getReadMethod() == null ? "null" : propertyDescriptor.getReadMethod().getName());
            System.out.println(propertyDescriptor.getWriteMethod() == null ? "null" : propertyDescriptor.getWriteMethod().getName());
            System.out.println(propertyDescriptor.getName());
        }
    }

    @Test
    public void test2() throws IntrospectionException {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor("test2", Admin.class);
        System.out.println(propertyDescriptor.getReadMethod() == null ? "null" : propertyDescriptor.getReadMethod().getName());
        System.out.println(propertyDescriptor.getWriteMethod() == null ? "null" : propertyDescriptor.getWriteMethod().getName());
        System.out.println(propertyDescriptor.getName());
    }


}
