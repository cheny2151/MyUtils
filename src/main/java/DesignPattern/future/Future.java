package DesignPattern.future;

public interface Future<T> {

    boolean isComplete();

    T getResult();

    void addListener(Listener listener);

    void complete();

    void sub();
}
