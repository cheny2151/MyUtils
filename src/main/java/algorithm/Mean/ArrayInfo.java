package algorithm.Mean;

public class ArrayInfo {

    private int[] array;
    private int length;
    private boolean isOdd;
    private int cursor;
    //记录左边被排除的index
    private int startIndex;
    //记录右边被排除的index
    private int endIndex;
    //左边被排除的个数
    private int leftCount;
    //右边被排除的个数
    private int rightCount;

    public ArrayInfo(int[] array, int cursor) {
        this.array = array;
        this.cursor = cursor;
        this.length = array.length;
        this.isOdd = (this.length & 1) == 1;
        this.startIndex = 0;
        this.endIndex = length - 1;
        this.leftCount = 0;
        this.rightCount = 0;
    }

    public int[] getArray() {
        return array;
    }

    public int getLength() {
        return length;
    }

    public boolean isOdd() {
        return isOdd;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.leftCount = startIndex;
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.rightCount = this.length - endIndex - 1;
        this.endIndex = endIndex;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }
}
