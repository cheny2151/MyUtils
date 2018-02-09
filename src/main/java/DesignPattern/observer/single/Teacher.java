package DesignPattern.observer.single;

import java.io.Serializable;
import java.util.Vector;

/**
 * 观察者模式：监听器模式
 * source 事件源
 */
public class Teacher implements Serializable {

    private Vector<HomeWorkListener> homeWorkListeners = new Vector<>();

    public void addStudent(Student student) {
        homeWorkListeners.add(student);
    }

    public void publishHomework(String homeWorkContent) {
        Homework homework = new Homework(this, homeWorkContent);
        notifyEvent(homework);
        for (HomeWorkListener homeWorkListener : homeWorkListeners) {
            homeWorkListener.doHomework(homework);
        }
    }

    public void comeBackHomework() {
        for (HomeWorkListener homeWorkListener : homeWorkListeners) {
            homeWorkListener.submitHomework();
        }
    }

    private void notifyEvent(Homework homework) {

    }

}
