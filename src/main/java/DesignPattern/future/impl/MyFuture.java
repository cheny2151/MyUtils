package DesignPattern.future.impl;

import DesignPattern.future.Future;
import DesignPattern.future.Listener;

import java.util.Vector;

public class MyFuture<T> implements Future<T> {

    private Vector<Listener> listeners = new Vector<>();

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public T getResult() {
        return null;
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

}
