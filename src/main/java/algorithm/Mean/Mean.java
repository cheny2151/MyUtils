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
        for (int i = arrayInfo.getStartIndex(); i < arrayInfo.getEndIndex(); i++) {
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
                //与midNum相等的处理逻辑：平均分布到左右
                leftCount = tempMidIndex - leftIndex;
                rightCount = rightIndex - tempMidIndex;
                if ((collision & 1) == 0) {
                    System.arraycopy(temp, leftIndex, temp, leftIndex - 1, leftCount);
                    temp[tempMidIndex - 1] = current;
                    leftIndex--;
                } else {
                    System.arraycopy(temp, tempMidIndex + 1, temp, tempMidIndex + 2, rightCount);
                    temp[tempMidIndex + 1] = current;
                    rightIndex++;
                }
                collision++;
            }
        }

        //左右两边个数
        leftCount = tempMidIndex - leftIndex;
        rightCount = rightIndex - tempMidIndex;

        /*System.out.println("--" + leftCount);
        System.out.println("---" + rightCount);
        System.out.println("leftIndex" + leftIndex);
        System.out.println("rightIndex" + rightIndex);
        for (int i : temp) {
            System.out.print(i + " ");
        }
        System.out.println();*/


        if (checkMid(leftCount, rightCount, collision, arrayInfo)) {
            //找到中数
            return midNum;
        } else if (leftCount > rightCount) {
            //左边个数大于右边个数，中数在左
            System.out.println("getStartIndex" + arrayInfo.getStartIndex());
            System.out.println("len" + (arrayInfo.getEndIndex() + 1 - arrayInfo.getStartIndex()));
            System.out.println("getEndIndex" + arrayInfo.getEndIndex());
            System.arraycopy(temp, leftIndex, array, arrayInfo.getStartIndex(),
                    arrayInfo.getEndIndex() + 1 - arrayInfo.getStartIndex());
            int newEndIndex = arrayInfo.getEndIndex() - rightCount - 1;
            arrayInfo.setEndIndex(newEndIndex);
            arrayInfo.setCursor(newEndIndex);
            return positioning(arrayInfo);
        } else {
            //右边个数大于左边个数，中数在右
            System.out.println("getStartIndex" + arrayInfo.getStartIndex());
            System.out.println("getEndIndex" + arrayInfo.getEndIndex());
            System.out.println("len" + (arrayInfo.getEndIndex() + 1 - arrayInfo.getStartIndex()));
            System.arraycopy(temp, leftIndex, array, arrayInfo.getStartIndex(),
                    arrayInfo.getEndIndex() + 1 - arrayInfo.getStartIndex());
            int newStartIndex = arrayInfo.getStartIndex() + leftCount + 1;
            arrayInfo.setStartIndex(newStartIndex);
            arrayInfo.setCursor(newStartIndex);
            return positioning(arrayInfo);
        }

    }

    /**
     * 判断是否定位到中数
     *
     * @param leftCount  左边的个数
     * @param rightCount 右边的个数
     * @return
     */
    private boolean checkMid(int leftCount, int rightCount, int collision, ArrayInfo arrayInfo) {
        leftCount += arrayInfo.getStartIndex();
        rightCount += arrayInfo.getLength() - (arrayInfo.getEndIndex() + 1);
        int abs = Math.abs(leftCount - rightCount);
        if (arrayInfo.isOdd()) {
            if (collision == 0)
                return abs == 0;
            else
                return abs <= 1;
        } else {
            if (collision == 0)
                return abs == 1;
            else
                return abs <= 2;
        }
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
            int t2 = mid - (int) (Math.random() * 10);
            array[i] = t /*+ (int) (100 * Math.random()) + (int) (10 * Math.random())*/;
            array[mid + 1 + i] = t2 /*+ (int) (100 * Math.random()) + (int) (10 * Math.random())*/;
        }
        for (int i : array) {
            System.out.print(i + " ");
        }
        int midNum = getMidNum(array);
        System.out.println();
        System.out.println(midNum);
    }

}
