package algorithm.skiplist;

import java.util.Random;

/**
 * @author cheney
 */
public class SkipList<T extends Comparable<T>> {

    private static int MAX_HIGH = 32;

    /**
     * 第0层头节点
     */
    private Node<T> head;

    /**
     * 第0层尾节点
     */
    private Node<T> tail;

    private int size;

    private int high;

    private static class Node<T extends Comparable<T>> {

        private T value;

        private int high;

        private Node<T> pre;

        private Node<T>[] next;

        Node(T value, int high) {
            this.value = value;
            this.high = high;
            this.next = new Node[high];
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

        public Node<T>[] getNext() {
            return next;
        }

        public void setNext(Node<T>[] next) {
            this.next = next;
        }

        public void setNext(Node<T> next, int high) {
            this.next[high] = next;
        }

        public int getHigh() {
            return high;
        }
    }

    public SkipList() {
        init();
    }

    public SkipList(T[] values) {

    }

    private void init() {
        this.size = this.high = 1;
        // 初始化head
        this.head = new Node<>(null, MAX_HIGH);
    }

    /**
     * 往跳表新增值
     *
     * @param value 新增值
     */
    public void add(T value) {
        int level = randomLevel();
        Node<T> n = new Node<>(value, level);
        Node<T> cur = head;
        Node<T> pre = null;
        for (int i = level - 1; i >= 0; i--) {
            Node<T> next;
            while ((next = cur.getNext()[i]) != null &&
                    next.value.compareTo(value) <= 0) {
                cur = next;
            }
            // 新节点插入到update之后，originNext之前
            pre = cur;
            Node<T> originNext = pre.getNext()[i];
            n.setNext(originNext, i);
            pre.setNext(n, i);
        }
        // 更新pre,tail
        n.setPre(pre);
        Node<T> next = n.getNext()[0];
        if (next == null) {
            tail = n;
        } else {
            next.setPre(n);
        }
        // 更新high,size
        if (level > high) {
            this.high = level;
        }
        size++;
    }

    public void remove(T t) {

    }

    /**
     * 通过调表查询是否存在数据
     *
     * @param target 查找的数据
     * @return 是否存在
     */
    public boolean contains(T target) {
        return get(target) != null;
    }

    /**
     * 通过调表查询数据
     *
     * @param target 查找的数据
     * @return 数据节点
     */
    private Node<T> get(T target) {
        int curLevel = high - 1;
        Node<T> cur = head;
        while (curLevel >= 0) {
            Node<T> next;
            while ((next = cur.getNext()[curLevel]) != null) {
                int compare = next.value.compareTo(target);
                if (compare < 0) {
                    cur = next;
                } else if (compare == 0) {
                    return cur;
                } else {
                    break;
                }
            }
            curLevel--;
        }
        return null;
    }

    /**
     * 抛硬币，每抛一次正面增加一层
     * 最大MAX_HIGH层
     *
     * @return 层级
     */
    private int randomLevel() {
        Random random = new Random();
        int level = 0;
        for (int i = 0; i < MAX_HIGH; i++) {
            if ((random.nextInt(2) & 1) == 1) {
                level++;
            }
        }
        return level;
    }

    @Override
    public String toString() {
        StringBuilder print = new StringBuilder();
        for (int i = high - 1; i >= 0; i--) {
            if (i == 0) {
                print.append("head--->");
            } else {
                print.append("\t\t");
            }
            Node<T> cur = this.head;
            int l = print.length();
            while ((cur = cur.getNext()[i]) != null) {
                print.append(cur.value).append("-----");
            }
            if (print.length() > l) {
                print.setLength(print.length() - 5);
            }
            if (i == 0) {
                print.append("--->tail");
            } else {
                print.append("\r\n");
            }
        }
        return print.toString();
    }

    public static void main(String[] args) {

        long l = System.currentTimeMillis();
        SkipList<Integer> skipList = new SkipList<>();
        for (int i = 0; i < 30000; i++) {
            skipList.add(i);
        }
        System.out.println(System.currentTimeMillis() - l);
        l = System.currentTimeMillis();
        System.out.println(skipList.contains(28888));
        System.out.println(System.currentTimeMillis() - l);

        /*SkipList<Integer> skipList = new SkipList<>();
        skipList.add(9);
        skipList.add(4);
        skipList.add(5);
        skipList.add(10);
        skipList.add(13);
        skipList.add(2);
        skipList.add(17);
        skipList.add(2);
        System.out.println(skipList.toString());*/
    }

}
