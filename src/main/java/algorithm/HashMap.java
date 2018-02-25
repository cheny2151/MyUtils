package algorithm;

import java.util.Objects;

/**
 * Created by cheny on 2018/2/25.
 */
public class HashMap<K, V> {

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

}
