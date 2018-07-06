package arrayAndCollectionUtils;

import org.junit.Test;

/**
 * 求数组中的中数
 */
public class Mean {

    private int getMidNum(int[] array) {

        int len = array.length;
        if (1 == len || 2 == len) {
            return array[0];
        }
        int midLen = getMidLen(len);
        int mid = array[0];
        int leftCount = 0;
        for (int temp : array) {
            if (leftCount < midLen) {
                if (temp > mid){

                }else {

                }
            } else {

            }
        }

        return mid;
    }

    private int getMidLen(int len) {
        return (int) (((double) len) / 2);
    }

    @Test
    public void test() {

    }

}
