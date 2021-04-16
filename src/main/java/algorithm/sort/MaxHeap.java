package algorithm.sort;

import com.alibaba.fastjson.JSON;

/**
 * 二叉堆
 *
 * @author cheney
 * @date 2020-03-06
 */
public class MaxHeap<T extends Comparable<T>> extends BaseHeap<T> {

    public MaxHeap(T[] array) {
        this(array, array.length);
    }

    public MaxHeap(T[] array, int size) {
        super(array, size);
        heapSort.sortDesc();
    }

    public boolean push(T val) {
        T[] sortArray = heapSort.getArray();
        T max = sortArray[0];
        if (val.compareTo(max) > 0) {
            return false;
        }
        sortArray[0] = val;
        heapSort.fixMaxHeap(0);
        return true;
    }

    public T[] getResult(){
        return heapSort.getArray();
    }

    public static void main(String[] args) {
        Integer[] integers = {2, 7, 9, 3, 46, 10};
        MaxHeap<Integer> heap = new MaxHeap<>(integers, 5);
        System.out.println(JSON.toJSONString(heap.getResult()));
        heap.push(47);
        System.out.println(JSON.toJSONString(heap.getResult()));
        heap.push(8);
        System.out.println(JSON.toJSONString(heap.getResult()));
    }

}
