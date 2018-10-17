package DesignPattern.future.impl;

import DesignPattern.future.Future;
import DesignPattern.future.Listener;
import DesignPattern.future.Task;

import java.util.Vector;

public class MyTask<T> implements Runnable {

    /**
     * 任务执行完返回的数据
     */
    private Future<T> future;

    private Task<T> task;

    public MyTask(Task<T> task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            future = task.execute();
            future.complete();
        } catch (Exception e) {
        }
        if (future.isComplete()) {
            future.sub();
        }
    }


    private Future<T> getFuture() {
        return future;
    }


    public class MyFuture<T> implements Future<T> {

        private Vector<Listener> listeners = new Vector<>();

        private boolean complete = false;

        @Override
        public boolean isComplete() {
            return complete;
        }

        @Override
        public T getResult() {
            return null;
        }

        @Override
        public void addListener(Listener listener) {
            listeners.add(listener);
        }

        public void complete() {
            this.complete = true;
        }

        @Override
        public void sub() {
            for (Listener listener : listeners) {
                listener.execute();
            }
        }
    }

}
