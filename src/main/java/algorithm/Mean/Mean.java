package algorithm.Mean;

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
        ArrayInfo arrayInfo = new ArrayInfo(array, 0);

        return positioning(arrayInfo);
    }


    /**
     * 游标在中间位之前的处理
     *
     * @return 新的游标index
     */
    private int positioning(ArrayInfo arrayInfo) {
        //中间位
        int len = arrayInfo.getLength();
        int[] array = arrayInfo.getArray();
        int cursor = arrayInfo.getCursor();

        int[] temp = new int[len << 1];
        int tempMidIndex = len - 1;
        int midNum = temp[tempMidIndex] = array[cursor];
        int leftIndex = len - 1;
        int rightIndex = len - 1;
        int leftCount;
        int rightCount;

        //开始中分
        //与midNum相等的个数
        int collision = 0;
        for (int i = arrayInfo.getStartIndex(); i <= arrayInfo.getEndIndex(); i++) {
            //跳过游标
            if (cursor == i) {
                continue;
            }

            int current = array[i];

            if (current > midNum) {
                temp[++rightIndex] = current;
            } else if (current < midNum) {
                temp[--leftIndex] = current;
            } else {
                //与midNum相等的处理逻辑：全部放到左边
                leftCount = tempMidIndex - leftIndex;
                System.arraycopy(temp, leftIndex, temp, leftIndex - 1, leftCount);
                temp[tempMidIndex - 1] = current;
                leftIndex--;
                collision++;
            }
        }

        //左右两边个数
        leftCount = tempMidIndex - leftIndex;
        rightCount = rightIndex - tempMidIndex;

        //目前游标左边的个数
        int currentLeftCount = leftCount + arrayInfo.getStartIndex();
        //目前游标右边的个数
        int currentRightCount = rightCount + arrayInfo.getLength() - (arrayInfo.getEndIndex() + 1);
        if (checkMid(currentLeftCount, currentRightCount, collision, arrayInfo)) {
            System.arraycopy(temp, leftIndex, array, arrayInfo.getStartIndex(),
                    arrayInfo.getEndIndex() + 1 - arrayInfo.getStartIndex());
            //找到中数
            return midNum;
        } else if (currentLeftCount > currentRightCount) {
            //左边个数大于右边个数，中数在左
            System.arraycopy(temp, leftIndex, array, arrayInfo.getStartIndex(),
                    arrayInfo.getEndIndex() + 1 - arrayInfo.getStartIndex());
            int newEndIndex = arrayInfo.getEndIndex() - rightCount - 1 - collision;
            arrayInfo.setEndIndex(newEndIndex);
            arrayInfo.setCursor(newEndIndex);
            return checkStartAfterEnd(arrayInfo) ? array[len / 2] : positioning(arrayInfo);
        } else {
            //右边个数大于左边个数，中数在右
            System.arraycopy(temp, leftIndex, array, arrayInfo.getStartIndex(),
                    arrayInfo.getEndIndex() + 1 - arrayInfo.getStartIndex());
            int newStartIndex = arrayInfo.getStartIndex() + leftCount + 1;
            arrayInfo.setStartIndex(newStartIndex);
            arrayInfo.setCursor(newStartIndex);
            return checkStartAfterEnd(arrayInfo) ? array[len / 2] : positioning(arrayInfo);
        }

    }

    private boolean checkStartAfterEnd(ArrayInfo arrayInfo) {
        return arrayInfo.getStartIndex() >= arrayInfo.getEndIndex();
    }

    /**
     * 判断是否定位到中数
     *
     * @param leftCount  左边的个数
     * @param rightCount 右边的个数
     * @return
     */
    private boolean checkMid(int leftCount, int rightCount, int collision, ArrayInfo arrayInfo) {
        int c = leftCount - rightCount;
        int abs = Math.abs(c);
        if (collision == 0 || c <= 0) {
            //由于重复的数全部放在左边，所以有重复的数并且右边大于左边时候，一定不会命中;
            if (arrayInfo.isOdd())
                return c == 0;
            else
                return abs == 1;
        } else {
            //检查重复的个数是否可以命中中间位
//            if (arrayInfo.isOdd())
//                return 2 * collision >= c;
//            else {
            int midIndex = arrayInfo.getLength() / 2 + 1;
            return midIndex >= arrayInfo.getCursor() - collision
                    && midIndex <= arrayInfo.getCursor();
//            }
        }
    }

    private int getMidLen(int len) {
        return (int) Math.ceil(((double) len) / 2);
    }

    @Test
    public void test() {
        int mid = 5000000;
        int[] array = new int[2 * mid + 1];
        array[mid] = mid;
        for (int i = 0; i < mid; i++) {
            int t = mid + (int) (Math.random() * 10) + (int) (Math.random() * 50);
            int t2 = mid - (int) (Math.random() * 10) - (int) (Math.random() * 50);
            if ((i & 1) == 1) {
                array[i] = t + (int) (100 * Math.random()) + (int) (10 * Math.random());
                array[mid + 1 + i] = t2 - (int) (100 * Math.random()) - (int) (10 * Math.random());
            } else {
                array[i] = t2 - (int) (100 * Math.random()) - (int) (10 * Math.random());
                array[mid + 1 + i] = t + (int) (100 * Math.random()) + (int) (10 * Math.random());
            }
        }
//        for (int i : array) {
//            System.out.print(i + " ");
//        }
        long x = System.currentTimeMillis();
        System.out.println(x);
        int midNum = getMidNum(array);
        System.out.println(System.currentTimeMillis() - x);
//        System.out.println();
//        for (int i = 0; i < array.length; i++) {
//            if (i == getMidLen(array.length) - 1)
//                System.out.print(array[i] + "* ");
//            else
//                System.out.print(array[i] + " ");
//        }
//        System.out.println();
        System.out.println(midNum);
    }

    @Test
    public void test2() {
        int[] array = {19, 103, 19, 56, 25, 69, 9, 94, 44, 97, 23, 72, 6, 72, 10, 90, 19, 92, 8, 79, 36, 83, -8, 80, 10, 78, 13, 78, 32, 103, 18, 65, 42, 89, 1, 73, 37, 87, 19, 53, 0, 75, 2, 103, 44, 98, 39, 73, 21, 73, 50, 79, 4, 75, 41, 98, 9, 98, 30, 62, -4, 93, 15, 92, 44, 96, 0, 71, 38, 59, 5, 70, 3, 104, 12, 62, 35, 63, 18, 74, 5, 65, 30, 68, 8, 86, 34, 56, 10, 76, 11, 57, 46, 79, 25, 64, 38, 74, 7, 54, 29};
        for (int i : array) {
            System.out.print(i + " ");
        }
        int midNum = getMidNum(array);
        System.out.println();
        for (int i = 0; i < array.length; i++) {
            if (i == getMidLen(array.length) - 1)
                System.out.print(array[i] + "* ");
            else
                System.out.print(array[i] + " ");
        }
        System.out.println();
        System.out.println(midNum);
    }

    @Test
    public void test3() {
        String temp = "19 103 19 56 25 69 9 94 44 97 23 72 6 72 10 90 19 92 8 79 36 83 -8 80 10 78 13 78 32 103 18 65 42 89 1 73 37 87 19 53 0 75 2 103 44 98 39 73 21 73 50 79 4 75 41 98 9 98 30 62 -4 93 15 92 44 96 0 71 38 59 5 70 3 104 12 62 35 63 18 74 5 65 30 68 8 86 34 56 10 76 11 57 46 79 25 64 38 74 7 54 29";
        String replace = temp.replace(" ", ",");
        System.out.println(replace);
    }

}
