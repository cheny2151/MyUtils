package DesignPattern.observer;

import java.util.EventListener;

/**
 * 观察者模式：监听器模式
 * Listener 监听器接口
 */
public interface HomeWorkListener extends EventListener {

    void doHomework(Homework homework);

    void submitHomework();
}
