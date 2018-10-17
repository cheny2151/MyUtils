package DesignPattern.future;

public interface Future<T> {

    boolean isComplete();

    boolean isSuccess();

    T getResult();

    void addListener(Listener listener);

    void complete();

    void sub();

    void setResult(T result);

}
