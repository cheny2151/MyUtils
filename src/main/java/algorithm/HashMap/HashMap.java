package algorithm.HashMap;

import java.util.Objects;

/**
 * Created by cheny on 2018/2/25.
 * <p>
 * <a href="https://yikun.github.io/2015/04/01/Java-HashMap%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86%E5%8F%8A%E5%AE%9E%E7%8E%B0/" />
 * 实现一个散列算法
 */
public class HashMap<K, V> {

    /**
     * 默认容量
     */
    final static int DEFAULT_CAPACITY = 1 << 4;

    /**
     * 最高容量
     * HashMap的table容量为何要2^n：
     * 计算index时（node位于哪个桶），当容量一定是2^n时，h & (length - 1) == h % length，
     * 它俩是等价不等效的，位运算效率非常高
     */
    final static int MAX_CAPACITY = 1 << 30;

    /**
     * 加载因子
     * 当size>0.75*capacity时扩充容量
     */
    final static float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 桶
     */
    transient Node<K, V>[] table;

    /**
     * 存储个数
     */
    transient int size;

    /**
     * 容量
     */
    int capacity;

    /**
     * 临界值
     */
    int threshold;

    /**
     * 负载因子
     */
    final float loadFactor;

    public HashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        synchThreshold();
    }

    public HashMap(float loadFactor) {
        this.capacity = DEFAULT_CAPACITY;
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("illegal load factor:" + loadFactor);
        }
        this.loadFactor = loadFactor;
        synchThreshold();
    }

    public HashMap(int initCapacity, float loadFactor) {
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("illegal load factor:" + loadFactor);
        }
        this.loadFactor = loadFactor;
        if (initCapacity < 0) {
            throw new IllegalArgumentException("illegal init capacity:" + initCapacity);
        }
        if (initCapacity > MAX_CAPACITY) {
            this.capacity = MAX_CAPACITY;
        }
        this.capacity = tableSizeFor(initCapacity);
        synchThreshold();
    }

    /**
     * 同步临界值threshold
     */
    private void synchThreshold() {
        this.threshold = (int) (this.capacity * this.loadFactor);
    }

    /**
     * 取最接近且大于入参的2的次幂的值
     */
    private static int tableSizeFor(int initCapacity) {
        initCapacity--;
        initCapacity |= initCapacity >>> 1;
        initCapacity |= initCapacity >>> 2;
        initCapacity |= initCapacity >>> 4;
        initCapacity |= initCapacity >>> 8;
        initCapacity |= initCapacity >>> 16;
        return initCapacity < 0 ? 1 : initCapacity > MAX_CAPACITY ? MAX_CAPACITY : initCapacity + 1;
    }

    private static int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int tableIndexFor(int hash) {
        return hash & (capacity - 1);
    }

    /**
     * 内部类：节点，存放数据
     *
     * @param <K>
     * @param <V>
     */
    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final Node<K, V> getNext() {
            return next;
        }

        public final V setValue(V newValue) {
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof Node) {
                Node<?, ?> node = (Node<?, ?>) o;
                return Objects.equals(this.key, node.getKey()) && Objects.equals(this.value, node.getValue());
            }
            return false;
        }

        public final int hashCode() {
            return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
        }
    }

    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    final V putVal(int hash, K key, V value) {
        Node<K, V> tarNode;
        if (table == null) {
            table = (Node<K, V>[]) new Node[capacity];
        }
        if ((tarNode = table[tableIndexFor(hash)]) != null) {
            while (tarNode.next != null) {
                tarNode = tarNode.next;
            }
            tarNode.next = new Node<>(hash, key, value, null);
        }else {
            tarNode = new Node<>(hash, key, value, null);
        }
        return null;
    }


}
