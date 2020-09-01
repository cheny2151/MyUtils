package cache.holder;

import cache.buffer.EntityBuffer;
import cache.factory.DefaultEntityBufferFactory;
import cache.factory.EntityQueryerChooser;
import cache.factory.EntityBufferFactory;
import cache.queryer.EntityQueryer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认Entity Buffer持有者实现类
 *
 * @author cheney
 * @date 2020-08-31
 */
public class DefaultEntityBufferHolder implements EntityBufferHolder {

    /**
     * Entity Buffer工厂
     */
    private EntityBufferFactory entityBufferFactory;

    /**
     * Entity Buffer Map
     */
    private final Map<Class<?>, EntityBuffer<?>> entityBufferMap;

    /**
     * 实体查询器
     */
    private EntityQueryer entityQueryer;

    public DefaultEntityBufferHolder() {
        this(new DefaultEntityBufferFactory(), new EntityQueryerChooser().getEntityQueryer());
    }

    public DefaultEntityBufferHolder(EntityQueryer entityQueryer) {
        this(new DefaultEntityBufferFactory(), entityQueryer);
    }

    public DefaultEntityBufferHolder(EntityBufferFactory entityBufferFactory, EntityQueryer entityQueryer) {
        this.entityBufferFactory = entityBufferFactory;
        this.entityQueryer = entityQueryer;
        this.entityBufferMap = new ConcurrentHashMap<>();
    }


    @Override
    public <T> void refreshCache(Class<T> clazz) {
        if (!entityBufferMap.containsKey(clazz)) {
            EntityBuffer<T> entityBuffer = entityBufferFactory.createBuffer(clazz);
            entityBufferMap.put(clazz, entityBuffer);
        }
        EntityBuffer<T> entityBuffer = getEntityBuffer(clazz);
        List<T> data = entityQueryer.query(entityBuffer.getBufferInfo());
        entityBuffer.refresh(data);
    }

    @Override
    public <T> List<T> getAllCache(Class<T> clazz) {
        EntityBuffer<T> entityBuffer = getEntityBuffer(clazz);
        return entityBuffer == null ? Collections.emptyList() : entityBuffer.getAllCache();
    }

    @Override
    public <T> Optional<T> getById(Object id, Class<T> clazz) {
        EntityBuffer<T> entityBuffer = getEntityBuffer(clazz);
        return entityBuffer == null ? Optional.empty() : entityBuffer.getById(id);
    }

    @Override
    public <T> Optional<T> get(T entityWithId) {
        if (entityWithId == null) {
            throw new IllegalArgumentException();
        }
        Class<T> tClass = (Class<T>) entityWithId.getClass();
        EntityBuffer<T> entityBuffer = getEntityBuffer(tClass);
        return entityBuffer == null ? Optional.empty() : entityBuffer.get(entityWithId);
    }

    @SuppressWarnings("unchecked")
    private <T> EntityBuffer<T> getEntityBuffer(Class<T> clazz) {
        return (EntityBuffer<T>) entityBufferMap.get(clazz);
    }
}
