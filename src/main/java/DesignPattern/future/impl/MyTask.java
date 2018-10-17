package DesignPattern.future.impl;

import DesignPattern.future.Future;
import DesignPattern.future.Listener;
import DesignPattern.future.Task;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

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
            future.setResult(task.execute());
            future.complete();
        } catch (Exception e) {
        }
        if (future.isComplete()) {
            future.sub();
        }
    }

    public Future<T> getFuture() {
        return future;
    }

    public T getResultBlock() {
        if (future.isComplete()) {
            return future.getResult();
        }
        CountDownLatch latch = new CountDownLatch(1);
        future.addListener(latch::countDown);
        while (!future.isComplete()) {

            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return future.getResult();
    }


    public class MyFuture implements Future<T> {

        private Vector<Listener> listeners = new Vector<>();

        private boolean complete = false;

        private T result;

        @Override
        public boolean isComplete() {
            return complete;
        }

        @Override
        public T getResult() {
            return result;
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

        public void setResult(T result) {
            this.result = result;
        }
    }

}
