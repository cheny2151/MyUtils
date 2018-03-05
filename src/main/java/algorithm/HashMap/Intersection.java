package algorithm.HashMap;

import java.lang.reflect.Array;

/**
 * 散列算法实现取两个数组的交集
 */
public class Intersection {


    private Intersection() {
    }

    /**
     * 散列节点
     *
     * @param <V>
     */
    private static class Node<V> {

        private int hash;

        private V value;

        private Node<V> next;

        public Node(int hash, V value, Node<V> next) {
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }

    private final static int MAX_LENGTH = 1 << 30;

    @SuppressWarnings("unchecked")
    public static <T> T[] exclusive(T[] forExclusive, T[] toExclusive, Class<T> resultType) {
        int forLength = forExclusive.length;
        int tableSize = tableForSize(forLength);
        T[] result = (T[]) Array.newInstance(resultType, forLength);
        Node<T>[] tables = new Node[tableSize];
        Node<T> node;
        boolean isExist = false;
        int hash, index, size = 0, count = 0;
        for (T t : forExclusive) {
            if ((node = tables[index = (hash = hash(t)) & (tableSize - 1)]) != null) {
                while (true) {
                    if (node.hash == hash && node.value.equals(t)) {
                        isExist = true;
                        break;
                    }
                    if (node.next == null) {
                        break;
                    }
                    node = node.next;
                }
                if (isExist) {
                    isExist = false;
                    continue;
                }
                node.next = new Node<>(hash, t, null);
            } else {
                tables[index] = new Node<>(hash, t, null);
            }
            if (++size > 0.75 * tables.length) {
                resize();
            }
        }
        for (T t : toExclusive) {
            if ((node = tables[(hash = hash(t)) & (tables.length - 1)]) != null) {
                do {
                    if (node.hash == hash && node.value.equals(t)) {
                        result[count++] = t;
                    }
                } while ((node = node.next) != null);
            }
        }
        if (count == 0) {
            return null;
        } else if (count != tableSize) {
            T[] result_2 = (T[]) Array.newInstance(resultType, count);
            System.arraycopy(result, 0, result_2, 0, count);
            return result_2;
        }
        return result;
    }

    private static void resize() {
        
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
