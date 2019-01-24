package algorithm.skiplist;

/**
 * @author cheney
 */
public class SkipList<T extends Comparable> {

    private Node<T> first;

    private SkipList<T> index;

    private int size;

    private static class Node<T> {

        private T value;

        private Node<T> pre;

        private Node<T> later;

        public Node(T value, Node<T> pre, Node<T> later) {
            this.value = value;
            this.pre = pre;
            this.later = later;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getPre() {
            return pre;
        }

        public void setPre(Node<T> pre) {
            this.pre = pre;
        }

        public Node<T> getLater() {
            return later;
        }

        public void setLater(Node<T> later) {
            this.later = later;
        }
    }

    public SkipList(T[] values) {
        Node<T> pre = this.first = new Node<>(values[0], null, null);
        for (int i = 1; i < values.length; i++) {
            Node<T> temp = new Node<>(values[i], pre, null);
            pre.setLater(temp);
            pre = temp;
        }
    }

    public void add(T value) {
        if (this.size == 0) {
            this.first = new Node<>(value, null, null);
        }else {
            addVal(value);
        }
    }

    private void addVal(T value) {
        Node<T> pre = this.first;
    }

}
