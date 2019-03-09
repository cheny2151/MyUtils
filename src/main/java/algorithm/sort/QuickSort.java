package algorithm.sort;

/**
 * 快速排序
 */
public class QuickSort implements Sort{

    public void sort(int[] array){
        sort(array,0,array.length-1);
    }

    /**
     * 思路 以某个数为基础值，小的放左边，大的放右边，以移动后基础值的index为中分
     * 递归lo->index-1和index+1->hi，直至所有递归的lo都小于hi结束
     */
    private void sort(int[] array,int lo,int hi){
        if (lo < hi){
            int j = core(array, lo, hi);
            sort(array,lo,j-1);
            sort(array,j+1,hi);
        }
    }

    /**
     * 以array[lo]为基础值做比较，将小于array[lo]放在左边，大于array[lo]的值移至右边，返回最后array[lo]值最后移动到的位置
     * @param array 数组
     * @param lo 头index
     * @param hi 尾index
     * @return 基础值的位置
     */
    private int core(int[] array, int lo, int hi){
        int i = lo;
        int j = hi+ 1;
        int compare = array[lo];
        while (true){
            while(array[++i] < compare && i != hi){
            }
            while(array[--j] > compare && j != lo){
            }
            if (i >= j)
                break;
            swap(array, i, j);
        }
        swap(array,lo,j);
        return j;
    }

}
