package cache.buffer;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Entity Buffer--内存实现
 *
 * @author cheney
 * @date 2020-08-31
 */
public class MemoryEntityBuffer<T> extends BaseEntityBuffer<T> {

    private final ReentrantLock lock = new ReentrantLock();

    private Map<String, T> cache;

    public MemoryEntityBuffer(Class<T> entityClass, boolean underline) {
        super(entityClass, underline);
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public void cache(T entity) {
        String id = extractId(entity);
        cache.put(id, entity);
    }

    @Override
    public void refresh(List<T> entities) {
        boolean isLock = false;
        try {
            isLock = this.lock.tryLock();
            if (isLock) {
                cache.clear();
                if (CollectionUtils.isEmpty(entities)) {
                    return;
                }
                Map<String, T> entityMap = entities.stream().collect(Collectors.toMap(this::extractId, e -> e));
                cache.putAll(entityMap);
            }
        } finally {
            if (isLock) lock.unlock();
        }
    }

    @Override
    public List<T> getAllCache() {
        return List.copyOf(cache.values());
    }

    @Override
    public void remove(T entity) {
        if (entity == null) {
            return;
        }
        cache.remove(extractId(entity));
    }

    @Override
    public Optional<T> get(T entityWithId) {
        return Optional.ofNullable(cache.get(extractId(entityWithId)));
    }

    @Override
    public Optional<T> getById(Object key) {
        return Optional.ofNullable(cache.get(key == null ? "null" : key.toString()));
    }

}
