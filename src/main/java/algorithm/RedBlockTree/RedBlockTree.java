package algorithm.RedBlockTree;

/**
 * 红黑树
 */
public class RedBlockTree<T> {

    private final static boolean RED = true;

    private final static boolean BLOCK = false;

    Node<T> root;

    static final class Node<T> {

        Node<T> left;

        Node<T> right;

        Node<T> parent;

        T t;

        boolean color = BLOCK;

        public Node(Node<T> left, Node<T> right, Node<T> parent, T t) {
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.t = t;
        }

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }
    }

    /**
     * 对A左旋:
     * <p>
     * -     A                C
     * -   /  \             /  \
     * -  B    C   --->    A    E
     * -     /  \        /  \
     * -    D    E      B    D
     */
    private void rotateLeft(Node<T> tar) {
        if (tar != null) {
            Node<T> c = tar.right;
            if (c.left != null) {
                tar.right = c.left;
                c.left.parent = tar;
            }

            Node<T> parent = tar.parent;
            if (parent == null) {
                root = c;
            } else if (parent.left == tar) {
                c.parent = parent.left;
            } else {
                c.parent = parent.right;
            }

            tar.parent = c;
            c.left = tar;
        }
    }

}
