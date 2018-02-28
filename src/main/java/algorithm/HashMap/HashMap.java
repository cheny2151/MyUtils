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
        Node<K, V> tarNode, oldNode = null;
        int index;
        if (table == null) {
            table = (Node<K, V>[]) new Node[capacity];
        }
        if ((tarNode = table[(index = tableIndexFor(hash))]) != null) {
            while (true) {
                //如果key已经存在,则记录该节点
                if (tarNode.key.equals(key) && tarNode.hash == hash) {
                    oldNode = tarNode;
                    break;
                }
                //如果下个节点不存在，则退出循环
                if (tarNode.next == null) break;
                tarNode = tarNode.next;
            }
            //如果key已经存在，则重新赋值并立刻放回oldVal
            if (oldNode != null) {
                V oldVal = oldNode.value;
                oldNode.setValue(value);
                return oldVal;
            }
            //执行此语句的条件:key不存在且桶上已经存在节点,将新节点放入next
            tarNode.next = new Node<>(hash, key, value, null);
        } else {
            //执行此语句的条件:索引桶上不存在节点
            table[index] = new Node<>(hash, key, value, null);
        }
        size++;
        if (size > this.threshold) {
            resize();
        }
        return null;
    }

    /**
     * 重置桶数量和位置
     * 由于我们使用2的幂来扩容，则每个bin元素要么还是在原来的bucket中，要么在2的幂中。
     * 实现细节:假设oldCap = 16 -> 10000,oldCap&hash==0则还是在原来的bucket中，==1则在2的幂中（原位置i+oldCap）
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    final void resize() {
        Node<K, V>[] oldTab = table;
        Node<K, V> current, next;
        int oldCap = capacity;
        if (oldCap >= MAX_CAPACITY) return;
        capacity = capacity << 2;
        synchThreshold();
        table = (Node<K, V>[]) new Node[capacity];
        for (int i = 0; i < oldCap; i++) {
            if ((next = oldTab[i]) != null) {
                Node<K, V> hiNode = null, loNode = null;
                do {
                    current = next;
                    next = next.next;
                    current.next = null;
                    if ((current.hash & oldCap) == 0) {
                        //低位
                        if (loNode == null) {
                            loNode = current;
                        } else {
                            loNode.next = current;
                        }
                    } else {
                        //高位
                        if (hiNode == null) {
                            hiNode = current;
                        } else {
                            hiNode.next = current;
                        }
                    }
                } while (next != null);
                if (loNode != null) {
                    table[i] = loNode;
                }
                if (hiNode != null) {
                    table[i + oldCap] = hiNode;
                }
                //next GC
                oldTab[i] = null;
            }
        }
    }

    /**
     * 重置桶数量和位置
     * 图解：
     * node链:
     * --O------O-----O--
     * current next
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    final void resize_my() {
        Node<K, V>[] oldTable = this.table;
        Node<K, V> newNode, current;
        int tempIndex;
        this.capacity <<= 2;
        synchThreshold();
        this.table = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> next : oldTable) {
            if (next == null) continue;
            while (true) {
                //--------------------------------------------------------------------------------
                // temp引用桶的第一个节点,将node移动至下一个节点,然后切断temp（头节点）与下个节点的引用
                //--------------------------------------------------------------------------------
                current = next;
                next = next.next;
                current.next = null;
                //--------------------------------------------------------------------------------
                //                                  end
                //--------------------------------------------------------------------------------
                if ((newNode = this.table[tempIndex = tableIndexFor(current.hash)]) != null) {
                    while (newNode.next != null) {
                        newNode = newNode.next;
                    }
                    newNode.next = current;
                } else {
                    this.table[tempIndex] = current;
                }
                if (next == null) break;
            }
        }
    }


}
