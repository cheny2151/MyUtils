package algorithm.HashMap;

import org.junit.Test;

import java.lang.reflect.Array;

import static algorithm.HashMap.Intersection.HashNodes.LOAD_FACTOR;

/**
 * 散列算法实现取两个数组的交集
 */
public class Intersection {


//    private Intersection() {
//    }

    /**
     * 散列节点
     *
     * @param <V>
     */
    final static class Node<V> {

        private int hash;

        private V value;

        private Node<V> next;

        public Node(int hash, V value, Node<V> next) {
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * hash桶
     */
    final static class HashNodes<V> {

        final static int MAX_LENGTH = 1 << 30;

        final static float LOAD_FACTOR = 0.75f;

        int size;

        Node<V>[] tables;

        int threshold;

        @SuppressWarnings("unchecked")
        public HashNodes(int length) {
            if (length <= 0) throw new IllegalArgumentException("illegal length" + length);
            int cap = tableForSize(length);
            this.tables = (Node<V>[]) new Node[cap];
            this.threshold = (int) (cap * LOAD_FACTOR);
        }

        public void insert(V v) {
            int hash, index, cap = tables.length;
            Node<V>[] tables = this.tables;
            Node<V> tar, next;
            if ((next = tables[index = ((hash = hash(v)) & (cap - 1))]) != null) {
                do {
                    tar = next;
                    if (tar.hash == hash && tar.equals(v)) {
                        return;
                    }
                    next = tar.next;
                } while (next != null);
                tar.next = new Node<>(hash, v, null);
            } else {
                tables[index] = new Node<>(hash, v, null);
            }
            if (++size > threshold) {
                resize();
            }
        }

        /**
         * 取交集算法可以不需要size算法,初始化大小为(forExclusive.length * (1 + LOAD_FACTOR))即可,因为传入的数组大小即tables的cap是固定的
         */
        @SuppressWarnings({"unchecked", "Duplicates"})
        private void resize() {
            int oldLen = tables.length;
            int newLen;
            if ((newLen = oldLen << 1) > MAX_LENGTH) {
                return;
            }
            Node<V>[] oldTab = tables;
            Node<V> oldNode, nextNode;
            Node<V>[] newTab = (Node<V>[]) new Node[newLen];
            tables = newTab;
            threshold = (int) (LOAD_FACTOR * newLen);
            for (int i = 0; i < oldLen; i++) {
                if ((nextNode = oldTab[i]) != null) {
                    Node<V> hiNode = null, loNode = null;
                    do {
                        oldNode = nextNode;
                        nextNode = nextNode.next;
                        oldNode.next = null;
                        if ((oldNode.hash & oldLen) == 0) {
                            if (loNode == null) {
                                loNode = oldNode;
                                newTab[i] = loNode;
                            } else {
                                loNode.next = oldNode;
                                loNode = oldNode;
                            }
                        } else {
                            if (hiNode == null) {
                                hiNode = oldNode;
                                newTab[i + oldLen] = hiNode;
                            } else {
                                hiNode.next = oldNode;
                                hiNode = oldNode;
                            }
                        }
                    } while (nextNode != null);
                }
            }
        }

        /**
         * hash散列算法求交集
         * 将要比较的数组的值散列到对应桶中，遍历桶链，寻找相同的值
         *
         * @param toExclusive 要比较和数组
         * @param resultType  数组内部类似
         * @return
         */
        @SuppressWarnings("unchecked")
        public V[] intersect(V[] toExclusive, Class<V> resultType) {
            Node<V>[] tables = this.tables;
            Node<V> target;
            int cap = tables.length;
            V[] result = (V[]) Array.newInstance(resultType, size);
            int count = 0;
            for (V v : toExclusive) {
                if ((target = tables[(hash(v) & (cap - 1))]) != null) {
                    do {
                        if (target.hash == hash(v) && target.value.equals(v)) {
                            result[count++] = v;
                        }
                    } while ((target = target.next) != null);
                }
            }
            if (count != size) {
                V[] result_2 = (V[]) Array.newInstance(resultType, count);
                System.arraycopy(result, 0, result_2, 0, count);
                return result_2;
            }
            return result;
        }

        private static int tableForSize(int length) {
            length = length - 1;
            length |= length >>> 1;
            length |= length >>> 2;
            length |= length >>> 4;
            length |= length >>> 8;
            length |= length >>> 16;
            return length < 0 ? 0 : (length > MAX_LENGTH ? MAX_LENGTH : length + 1);
        }

        private static int hash(Object val) {
            int hash;
            return (val == null) ? 0 : (hash = val.hashCode()) ^ (hash >>> 16);
        }

    }


    @SuppressWarnings("unchecked")
    public static <T> T[] exclusive(T[] forExclusive, T[] toExclusive, Class<T> resultType) {
        //check
        if (forExclusive == null || forExclusive.length == 0 || toExclusive == null || toExclusive.length == 0) {
            return null;
        }
        //初始化大小为(forExclusive.length * (1 + LOAD_FACTOR)) 则不用size
        HashNodes<T> nodes = new HashNodes<>((int) (forExclusive.length * (1 + LOAD_FACTOR)));
        for (T t : forExclusive) {
            nodes.insert(t);
        }
        return nodes.intersect(toExclusive, resultType);
    }

    @Test
    public void test() {
        Integer[] int1 = new Integer[100000];
        Integer[] int2 = new Integer[50000];
        for (int i = 1; i < 100001; i++) {
            int1[i - 1] = i;
            if ((i & 1) == 0) {
                int2[50000 - (i >> 1)] = i;
            }
        }
        long time = System.currentTimeMillis();
        Integer[] exclusive = exclusive(int2, int1, Integer.class);
        System.out.println("用时" + (System.currentTimeMillis() - time));
        System.out.println("交集个数" + exclusive.length);
    }


}
