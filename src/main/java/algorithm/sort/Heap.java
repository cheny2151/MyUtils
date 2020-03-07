package algorithm.sort;

import com.alibaba.fastjson.JSON;

/**
 * 二叉堆
 *
 * @author cheney
 * @date 2020-03-06
 */
public class Heap {

    /**
     * 目标数组
     */
    private int[] array;

    /**
     * 最大堆比较函数
     */
    private CompareFunction maxHeapCompare = (a, b) -> a > b;

    /**
     * 最小堆比较函数
     */
    private CompareFunction minHeapCompare = (a, b) -> a < b;

    public Heap(int[] array) {
        this.array = array;
    }

    public static void main(String[] args) {
//        int[] ints = {11, 10, 7, 9, 5, 6, 4, 8, 2, 3, 1};
        int[] ints = {9, 10, 7, 11, 6, 8, 4, 5, 2, 1, 3};
        Heap heapSort = new Heap(ints);
        heapSort.sortAsc();
        System.out.println(JSON.toJSONString(heapSort.array));
    }

    /**
     * 升序
     */
    public int[] sortAsc() {
        makeMaxHeap();
        for (int i = array.length - 1; i > 0; i--) {
            swap(0, i);
            makeHeap(0, i, maxHeapCompare);
        }
        return array;
    }

    /**
     * 降序
     */
    public int[] sortDesc() {
        makeMinHeap();
        for (int i = array.length - 1; i > 0; i--) {
            swap(0, i);
            makeHeap(0, i, minHeapCompare);
        }
        return array;
    }

    /**
     * 转化为最大堆
     */
    public int[] makeMaxHeap() {
        int firstIndex = firstNonLeafNodeIndex();
        for (int i = firstIndex; i >= 0; i--) {
            makeHeap(i, array.length, maxHeapCompare);
        }
        return array;
    }

    /**
     * 转化为最小堆
     */
    public int[] makeMinHeap() {
        int firstIndex = firstNonLeafNodeIndex();
        for (int i = firstIndex; i >= 0; i--) {
            makeHeap(i, array.length, minHeapCompare);
        }
        return array;
    }

    /**
     * 堆化
     *
     * @param index      执行堆化的位置
     * @param len        执行堆化的长度
     * @param comparator 对比方法
     */
    private void makeHeap(int index, int len, CompareFunction comparator) {
        int leftIdx, rightIdx, maxIdx;
        int rightVal, maxVal;
        maxIdx = leftIdx = leftNodeIndex(index);
        if (leftIdx >= len) {
            // 左节点大于目标len
            return;
        }
        maxVal = array[leftIdx];
        rightIdx = rightNodeIndex(index);
        rightVal = array[rightIdx];
        if (rightIdx < len && comparator.compare(rightVal, maxVal)) {
            // 右节点小于目标len并且值compare左节点为true
            maxIdx = rightIdx;
            maxVal = rightVal;
        }
        int curVal = array[index];
        if (comparator.compare(maxVal, curVal)) {
            swap(index, maxIdx);
            // 影响到子节点，继续堆化
            makeHeap(maxIdx, len, comparator);
        }
    }

    public int leftNodeIndex(int index) {
        return 2 * index + 1;
    }

    public int leftNodeVal(int index) {
        return array[2 * index + 1];
    }

    public int rightNodeIndex(int index) {
        return 2 * index + 2;
    }

    public int rightNodeVal(int index) {
        return array[2 * index + 2];
    }

    /**
     * 交换位置值
     *
     * @param i1 位置1
     * @param i2 位置2
     */
    private void swap(int i1, int i2) {
        array[i1] = array[i1] ^ array[i2];
        array[i2] = array[i1] ^ array[i2];
        array[i1] = array[i1] ^ array[i2];
    }

    /**
     * 第一个非叶子节点位置
     * 即为末尾叶子节点的父节点 = (末尾index-1)/2
     *
     * @return 数组index
     */
    private int firstNonLeafNodeIndex() {
        return (array.length - 2) >> 1;
    }

    private interface CompareFunction {
        boolean compare(int a, int b);
    }
}
