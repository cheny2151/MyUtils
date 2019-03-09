package algorithm.sort;

/**
 * 冒泡排序
 */
public class BubbleSort implements Sort{

    public void sort(int[] array){
        int len = array.length;
        for (int i = len -1;i>0;i--){
            for(int j = 0;j< i;j++){
                if(array[j]>array[j+1]){
                    swap(array,j,j+1);
                }
            }
        }
    }

}
