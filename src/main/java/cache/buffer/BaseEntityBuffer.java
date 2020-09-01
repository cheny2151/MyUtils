package cache.buffer;

import cache.annotation.CacheEntity;
import cache.annotation.CacheField;
import cache.annotation.CacheFilter;
import cache.annotation.CacheId;
import cache.buffer.model.BufferInfo;
import cache.exception.AnnotationTypeMissException;
import org.apache.commons.collections.CollectionUtils;
import reflect.ReflectUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Entity Buffer基础实现
 *
 * @author cheney
 * @date 2020-08-31
 */
public abstract class BaseEntityBuffer<T> implements EntityBuffer<T> {

    /**
     * 缓存实体类型
     */
    private final Class<T> entityClass;

    /**
     * 缓存信息
     */
    private BufferInfo<T> bufferInfo;

    public BaseEntityBuffer(Class<T> entityClass, boolean underline) {
        this.entityClass = entityClass;
        parse(entityClass, underline);
    }

    private void parse(Class<T> entityClass,boolean underline) {
        // 解析CacheEntity
        CacheEntity cacheEntity = entityClass.getDeclaredAnnotation(CacheEntity.class);
        if (cacheEntity == null) {
            throw new AnnotationTypeMissException(CacheEntity.class, entityClass);
        }
        String tableName = cacheEntity.tableName();
        CacheFilter[] cacheFilters = cacheEntity.sqlFilter();
        String sqlCondition = null;
        if (cacheFilters.length > 0) {
            StringBuilder sqlBuilder = new StringBuilder();
            for (CacheFilter filter : cacheFilters) {
                sqlBuilder.append(filter.value()).append(" and ");
            }
            sqlCondition = sqlBuilder.substring(0, sqlBuilder.length() - 5);
        }
        // CacheId
        Set<String> cacheIds = ReflectUtils.getAllFieldHasAnnotation(entityClass, CacheId.class).keySet();
        if (CollectionUtils.isEmpty(cacheIds)) {
            throw new AnnotationTypeMissException(CacheId.class, entityClass);
        }
        String[] idFields = cacheIds.toArray(new String[0]);
        // CacheField
        Set<String> cacheFields = ReflectUtils.getAllFieldHasAnnotation(entityClass, CacheField.class).keySet();
        if (CollectionUtils.isEmpty(cacheFields)) {
            throw new AnnotationTypeMissException(CacheField.class, entityClass);
        }
        // allFields = id + fields
        Method[] idReadMethods = Stream.of(idFields).map(f -> ReflectUtils.getReadMethod(entityClass, f)).toArray(Method[]::new);
        Set<String> allCacheFieldSet = new HashSet<>(cacheFields);
        allCacheFieldSet.addAll(cacheIds);
        String[] allFields = allCacheFieldSet.toArray(new String[0]);
        this.bufferInfo = new BufferInfo<>(entityClass, tableName, idFields, allFields, sqlCondition, idReadMethods, underline);
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public BufferInfo<T> getBufferInfo() {
        return bufferInfo;
    }

    protected String extractId(T entity) {
        return Stream.of(bufferInfo.getIdReadMethods())
                .map(m -> String.valueOf(ReflectUtils.readValue(entity, m)))
                .collect(Collectors.joining("_"));
    }

}
