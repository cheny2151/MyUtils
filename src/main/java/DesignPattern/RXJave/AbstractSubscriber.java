package DesignPattern.RXJave;

/**
 * 观察者抽象类
 *
 * @param <T>
 */
public abstract class AbstractSubscriber<T> implements Observer<T> {

    abstract void onStart();

}
