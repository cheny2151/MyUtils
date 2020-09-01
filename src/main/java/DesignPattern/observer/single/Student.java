package DesignPattern.observer.single;

import cache.annotation.CacheEntity;
import cache.annotation.CacheField;
import cache.annotation.CacheFilter;
import cache.annotation.CacheId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式：监听器模式
 * Listener 监听器实现类
 */
@CacheEntity(tableName = "test",sqlFilter = {@CacheFilter("del_flag = 0"),@CacheFilter("status = 1")})
public class Student implements HomeWorkListener,Serializable {

    private static final long serialVersionUID = -1374954042835581442L;

    @CacheId
    private String id;

    @CacheField
    private String name;

    private String sex;

    private List<Homework> homework = new ArrayList<>();

    public Student() {
    }

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

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
