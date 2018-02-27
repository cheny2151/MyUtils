package ComparatorTest;

public class MyInt implements Comparable<MyInt> {

    private Integer i;

    public int compareTo(MyInt o) {
        return -1;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }
}
