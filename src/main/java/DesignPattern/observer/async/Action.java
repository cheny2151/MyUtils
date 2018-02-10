package DesignPattern.observer.async;

public class Action<T> implements Runnable {

    private Task<T> task;

    private T future;

    private Action(Task<T> task) {
        this.task = task;
    }

    public static <T> Action<T> create(Task<T> task) {
        return new Action<>(task);
    }

    @Override
    public void run() {
        future = task.execute();
    }

    public interface Task<T> {
        T execute();
    }

    public T getFuture() {
        return future;
    }

}
