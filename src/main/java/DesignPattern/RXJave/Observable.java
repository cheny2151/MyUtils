package DesignPattern.RXJave;

public class Observable<T> {

    private OnSubscribe<T> onSubscribe;

    public Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    public void subscribe(AbstractSubscriber<T> subscriber) {
        try {
            subscriber.onStart();
            onSubscribe.call(subscriber);
            subscriber.onCompleted();
        } catch (Exception e) {
            subscriber.onError(e);
        }
    }

    /**
     * 命令模式 命令subscriber执行
     *
     * @param <T>
     */
    public interface OnSubscribe<T> {
        void call(AbstractSubscriber<T> subscriber);
    }

}
