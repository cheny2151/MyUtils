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

    public ArrayInfo(int[] array, int cursor) {
        this.array = array;
        this.cursor = cursor;
        this.length = array.length;
        this.isOdd = (this.length & 1) == 1;
        this.startIndex = 0;
        this.endIndex = length - 1;
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
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
