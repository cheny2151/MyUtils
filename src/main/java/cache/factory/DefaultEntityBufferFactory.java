package cache.factory;

import cache.buffer.EntityBuffer;
import cache.buffer.MemoryEntityBuffer;
import cache.buffer.RedisEntityBuffer;

/**
 * 默认Entity Buffer工厂
 *
 * @author cheney
 * @date 2020-08-31
 */
public class DefaultEntityBufferFactory implements EntityBufferFactory {

    private BufferType bufferType;

    private boolean underline;

    public enum BufferType {
        DEFAULT,
        MEMORY,
        REDIS
    }

    public DefaultEntityBufferFactory(boolean underline) {
        this.bufferType = BufferType.DEFAULT;
        this.underline = underline;
    }

    public DefaultEntityBufferFactory(boolean underline, BufferType bufferType) {
        if (bufferType == null) {
            throw new IllegalArgumentException("bufferType can not be null");
        }
        this.bufferType = bufferType;
        this.underline = underline;
    }

    public <T> EntityBuffer<T> createBuffer(Class<T> clazz) {
        switch (this.bufferType) {
            case MEMORY: {
                return new MemoryEntityBuffer<>(clazz, underline);
            }
            case REDIS: {
                return new RedisEntityBuffer<>(clazz, underline);
            }
            default: {
                return getDefaultBuffer(clazz);
            }
        }
    }

    @Override
    public <T> EntityBuffer<T> getDefaultBuffer(Class<T> clazz) {
        return new MemoryEntityBuffer<>(clazz, underline);
    }

    public BufferType getBufferType() {
        return bufferType;
    }

    public void setBufferType(BufferType bufferType) {
        this.bufferType = bufferType;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }
}
