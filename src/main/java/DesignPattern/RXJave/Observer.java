package DesignPattern.RXJave;

/**
 * 观察者接口
 *
 * @param <T>
 */
public interface Observer<T> {

    void onNext(T var1);

    void onCompleted();

    void onError(Throwable t);

}
