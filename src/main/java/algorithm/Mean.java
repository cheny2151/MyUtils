package algorithm;

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
        //游标
        int cursor = 0;

        return leftDeal(cursor, array);
    }


    /**
     * 游标在中间位之前的处理
     *
     * @param cursor 游标index
     * @param array  数组
     * @return 新的游标index
     */
    private int leftDeal(int cursor, int[] array) {
        int len = array.length;
        //中间位
        int midLen = len / 2;

        int[] temp = new int[len << 1];
        int cursorIndex = len - 1;
        int midNum = temp[cursorIndex] = array[cursor];
        int leftIndex = len - 1;
        int rightIndex = len - 1;
        int leftCount;
        int rightCount;

        //开始中分
        //与midNum相等的个数
        int collision = 0;
        //跳过第一次
        boolean jump = true;
        for (int current : array) {
            if (jump) {
                jump = false;
                continue;
            }
            if (current > midNum) {
                temp[++rightIndex] = current;
            } else if (current < midNum) {
                temp[--leftIndex] = current;
            } else {
                //与midNum相等的处理逻辑：平均分布到左右
                leftCount = cursorIndex - leftIndex;
                rightCount = rightIndex - cursorIndex;
                if ((collision & 1) == 0) {
                    System.arraycopy(temp, leftIndex, temp, leftIndex - 1, leftCount);
                    temp[cursorIndex - 1] = current;
                    leftIndex--;
                } else {
                    System.arraycopy(temp, cursorIndex + 1, temp, cursorIndex + 2, rightCount);
                    temp[cursorIndex + 1] = current;
                    rightIndex++;
                }
                collision++;
            }
        }

        //左右两边个数
        leftCount = cursorIndex - leftIndex;
        rightCount = rightIndex - cursorIndex;

        /*System.out.println("--" + leftCount);
        System.out.println("---" + rightCount);
        System.out.println("leftIndex" + leftIndex);
        System.out.println("rightIndex" + rightIndex);
        for (int i : temp) {
            System.out.print(i + " ");
        }
        System.out.println();*/

        if (checkMid(leftCount, rightCount, (len & 1) == 1)) {
            //找到中数
            return midNum;
        } else if (leftCount > rightCount) {
            //左边个数大于右边个数，中数在左，左移（右处理）
            System.arraycopy(temp, leftIndex, array, 0, len);
            return rightDeal(leftCount - 1, array);
        } else {
            //右边个数大于左边个数，中数在右，右移（左处理）
            System.arraycopy(temp, leftIndex, array, 0, len);
            return leftDeal(len - rightCount, array);
        }

    }

    /**
     * 判断是否定位到中数
     *
     * @param leftCount  左边的个数
     * @param rightCount 右边的个数
     * @param odd        是否奇数
     * @return
     */
    private boolean checkMid(int leftCount, int rightCount, boolean odd) {
        if (odd) {
            return leftCount == rightCount;
        } else {
            return Math.abs(leftCount - rightCount) == 1;
        }
    }

    /**
     * 游标在中间位之后的处理
     *
     * @param cursor 游标index
     * @param array  数组
     * @return 新的游标index
     */
    private int rightDeal(int cursor, int[] array) {

        return ++cursor;
    }

    private int getMidLen(int len) {
        return (int) Math.ceil(((double) len) / 2);
    }

    @Test
    public void test() {
        int mid = 10;
        int[] array = new int[2 * mid + 1];
        array[mid] = mid;
        for (int i = 0; i < mid; i++) {
            int t = mid + (int) (Math.random() * 10);
            if (t == 0) {
                t = 1;
            }
            int t2 = mid - (int) (Math.random() * 10);
            if (t2 == 0) {
                t2 = 1;
            }
            array[i] = t /*+ (int) (100 * Math.random()) + (int) (10 * Math.random())*/;
            array[mid + 1 + i] = t2 /*+ (int) (100 * Math.random()) + (int) (10 * Math.random())*/;
        }
        array[15] = array[0];
        array[7] = array[0];
        for (int i : array) {
            System.out.print(i + " ");
        }
        int midNum = getMidNum(array);
    }

}
