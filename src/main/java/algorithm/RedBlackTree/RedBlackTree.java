package algorithm.RedBlackTree;

/**
 * 红黑树
 * <p>
 * 红黑树的特性:
 * （1）每个节点或者是黑色，或者是红色。
 * （2）根节点是黑色。
 * （3）每个叶子节点（NIL）是黑色。 [注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！]
 * （4）如果一个节点是红色的，则它的子节点必须是黑色的。
 * （5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。
 */
public class RedBlackTree<T extends Comparable> {

    private final static boolean RED = true;

    private final static boolean BLACK = false;

    Node<T> root;

    int size;

    static final class Node<T extends Comparable> {

        Node<T> left;

        Node<T> right;

        Node<T> parent;

        T value;

        boolean color = BLACK;

        public Node(Node<T> left, Node<T> right, Node<T> parent, T value) {
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
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
            c.parent = parent;
            if (parent == null) {
                root = c;
            } else if (parent.left == tar) {
                parent.left = c;
            } else {
                parent.right = c;
            }

            tar.parent = c;
            c.left = tar;
        }
    }

    /**
     * 对A右旋:
     * <p>
     * -     A                B
     * -   /  \             /  \
     * -  B    C   --->    D    A
     * -/  \                  /  \
     * D    E                E    C
     */
    private void rotateRight(Node<T> tar) {
        if (tar != null) {
            Node<T> b = tar.left;
            if (b.right != null) {
                tar.left = b.right;
                b.right.parent = tar;
            }

            Node<T> parent = tar.parent;
            b.parent = parent;
            if (parent == null) {
                root = b;
            } else if (parent.left == tar) {
                parent.left = b;
            } else {
                parent.right = b;
            }

            tar.parent = b;
            b.right = tar;
        }
    }

    /**
     * 插入
     */
    public void insert(T val) {
        if (val == null) throw new NullPointerException();
        Node<T> current, next;
        int compare;

        //root为null时
        if ((next = root) == null) {
            root = new Node<>(null, null, null, val);
            size++;
            return;
        }

        //找出插入的节点
        do {
            current = next;
            //noinspection unchecked
            compare = val.compareTo(current.value);
            if (compare > 0) {
                next = current.right;
            } else if (compare < 0) {
                next = current.left;
            } else return;
        } while (next != null);

        //插入
        Node<T> node = new Node<>(null, null, current, val);
        if (compare > 0) {
            current.right = node;
        } else {
            current.left = node;
        }
        size++;

        fixAfterInsert(node);

    }

    /**
     * 1:如果插入节点的父节点为黑色,则不用修复;
     * 2:如果插入的节点的父节点和叔节点都为红色,则将父叔节点染为黑色再将祖父节点染红。再以祖父节点为起点向上修复;
     * 3:如果插入的节点的父节点为红色，叔节点为黑色,则将父节点设为起点(支点)通过左旋或者右旋将节点调整为"一条直线";
     * 即(*代表红色):      A             A
     * -                /  \           / \
     * -              *B    C -->    *E   C (解释:E为插入的节点,ABE不为"同一直线",通过左旋调整为同一方向后再右旋;若插入的节点在C节点下同理对称)
     * -             /  \           / \
     * -            D   *E        *B
     * 4:如果插入的节点的父节点为红色,叔节点为黑色,并且在"一条直线"则先将祖父节点染为红色,父节点染为黑色再以祖父节点为支点通过对应的旋转完成修复。
     * 注意:按2-3-4的顺序判断,有3必有4.
     * 总结:修复的流程为:染色->左旋（右旋）->右旋（左旋）
     */
    private void fixAfterInsert(Node<T> cur) {
        cur.color = RED;

        while (cur != root && parentOf(cur).color == RED) {
            if (parentOf(cur) == leftOf(parentOf(parentOf(cur)))) {
                //left:插入的节点的父节点为祖父节点的左节点
                Node<T> r = rightOf(parentOf(parentOf(cur)));
                if (r.color == RED) {
                    //情况2
                    setBlack(parentOf(cur));
                    setBlack(r);
                    setRed(parentOf(parentOf(cur)));
                    cur = parentOf(parentOf(cur));
                } else {
                    if (cur == rightOf(parentOf(cur))) {
                        //情况3
                        cur = parentOf(cur);
                        rotateLeft(cur);
                    }
                    //情况4
                    setBlack(parentOf(cur));
                    setRed(parentOf(parentOf(cur)));
                    rotateRight(parentOf(parentOf(cur)));
                }
            } else {
                //right:插入的节点的父节点为祖父节点的右节点
                Node<T> l = leftOf(parentOf(parentOf(cur)));
                if (l.color == RED) {
                    //情况2
                    setBlack(parentOf(cur));
                    setBlack(l);
                    setRed(parentOf(parentOf(cur)));
                    cur = parentOf(parentOf(cur));
                } else {
                    if (cur == leftOf(parentOf(cur))) {
                        cur = parentOf(cur);
                        rotateRight(cur);
                    }
                    setBlack(parentOf(cur));
                    setRed(parentOf(parentOf(cur)));
                    rotateLeft(parentOf(parentOf(cur)));
                }
            }
        }
    }

    private void setBlack(Node<T> node) {
        if (node == null) throw new NullPointerException();
        node.color = BLACK;
    }

    private void setRed(Node<T> node) {
        if (node == null) throw new NullPointerException();
        node.color = RED;
    }

    private Node<T> rightOf(Node<T> node) {
        return node != null ? node.right : null;
    }

    private Node<T> leftOf(Node<T> node) {
        return node != null ? node.left : null;
    }

    private Node<T> parentOf(Node<T> node) {
        return node != null ? node.parent : null;
    }


}
