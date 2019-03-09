package algorithm.sort;

import org.junit.Test;

public class MainTest {

    @Test
    public void main(){
        int[] array = new int[100000];
        for (int i =0 ;i<array.length;i++){
            array[i] = (int) (Math.random()* 100000);
        }
        Sort sort = new QuickSort();
        long start = System.currentTimeMillis();
        sort.sort(array);
        long end = System.currentTimeMillis();
        StringBuilder out = new StringBuilder("[");
        for (int val : array) {
            out.append(val).append(",");
        }
        String s = out.substring(0, out.length() - 1) + "]";
        System.out.println(s);
        System.out.println("排序用时："+(end-start));
    }

}
