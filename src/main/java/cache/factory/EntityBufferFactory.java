package cache.factory;

import cache.buffer.EntityBuffer;

/**
 * @author cheney
 * @date 2020-08-31
 */
public interface EntityBufferFactory {

    <T> EntityBuffer<T> createBuffer(Class<T> clazz);

    <T> EntityBuffer<T> getDefaultBuffer(Class<T> clazz);
}
