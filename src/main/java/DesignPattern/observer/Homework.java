package DesignPattern.observer;

import java.util.EventObject;

/**
 * 观察者模式：监听器模式
 * EVENT 事件
 */
public class Homework extends EventObject {

    private String homeWorkContent;

    private boolean isComplete = false;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public Homework(Teacher source, String homeWorkContent) {
        super(source);
        this.homeWorkContent = homeWorkContent;
    }

    public String getHomeWorkContent() {
        return homeWorkContent;
    }

    public void setHomeWorkContent(String homeWorkContent) {
        this.homeWorkContent = homeWorkContent;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
