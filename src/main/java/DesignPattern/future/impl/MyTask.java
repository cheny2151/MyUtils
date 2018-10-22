package DesignPattern.future.impl;

import DesignPattern.future.Future;
import DesignPattern.future.Listener;
import DesignPattern.future.Task;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class MyTask<T> implements Runnable {

    /**
     * 任务执行完返回的数据
     */
    private MyFuture future;

    private Task<T> task;

    private MyTask(Task<T> task) {
        this.task = task;
        this.future = new MyFuture();
    }

    public static <T> MyTask<T> create(Task<T> task) {
        return new MyTask<>(task);
    }

    @Override
    public void run() {
        try {
            future.setResult(task.execute());
            future.setSuccess(true);
        } catch (Exception e) {
            future.setSuccess(false);
        } finally {
            future.complete();
            future.sub();
        }
    }

    public Future<T> start() {
        Executors.newSingleThreadExecutor().submit(this);
        return future;
    }

    public Future<T> getFuture() {
        return future;
    }

    public T getResultBlock() {
        //已经完成
        if (future.isComplete()) {
            if (future.isSuccess()) {
                return future.getResult();
            } else {
                throw new RuntimeException("执行未成功");
            }
        }

        //未完成，阻塞并监听完成事件
        CountDownLatch latch = new CountDownLatch(1);
        future.addListener(latch::countDown);
        while (!future.isComplete()) {
            try {
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (future.isSuccess()) {
            return future.getResult();
        } else {
            throw new RuntimeException("执行未成功");
        }
    }


    public class MyFuture implements Future<T> {

        private Vector<Listener> listeners = new Vector<>();

        private boolean complete = false;

        private boolean success = false;

        private T result;

        @Override
        public boolean isComplete() {
            return complete;
        }

        @Override
        public boolean isSuccess() {
            return success;
        }

        @Override
        public T getResult() {
            return result;
        }

        @Override
        public void addListener(Listener listener) {
            listeners.add(listener);
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

        public void complete() {
            this.complete = true;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

}
