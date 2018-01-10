package DesignPattern.ChainOfResponsibility;

/**
 * Created by cheny on 2018/1/8.
 */
public abstract class FoodFilterChain implements FoodFilter {

    private FoodFilterChain next;

    public FoodFilterChain() {
    }

    public FoodFilterChain(FoodFilterChain next) {
        this.next = next;
    }

    public FoodFilter getNext() {
        return next;
    }

    public void setNext(FoodFilterChain next) {
        this.next = next;
    }

    public boolean hasNext() {
        return next != null;
    }

}
