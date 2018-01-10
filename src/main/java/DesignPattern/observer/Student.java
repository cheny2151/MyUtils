package DesignPattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式：监听器模式
 * Listener 监听器实现类
 */
public class Student implements HomeWorkListener {

    private String id;

    private String name;

    private String sex;

    private List<Homework> homework = new ArrayList<>();

    public Student(String id, String name, String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    @Override
    public void doHomework(Homework homework) {
        System.out.println(id + "doing" + homework.getHomeWorkContent());
        addHomeWork(homework);
        if (Math.round(Math.random()) == 1) {
            homework.setComplete(true);
        }
    }

    @Override
    public void submitHomework() {
        for (Homework homework : this.homework) {
            if (homework.isComplete()) {
                System.out.println(id + "completed" + homework.getHomeWorkContent() + ",so submit...");
            } else {
                System.out.println(id + "not completed" + homework.getHomeWorkContent() + ",so waiting...");
            }
        }
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Homework> getHomework() {
        return homework;
    }

    public void setHomework(List<Homework> homework) {
        this.homework = homework;
    }

    public void addHomeWork(Homework homework) {
        this.homework.add(homework);
    }
}
