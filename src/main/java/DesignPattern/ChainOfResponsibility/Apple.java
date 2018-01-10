package DesignPattern.ChainOfResponsibility;

import java.util.Date;

/**
 * Created by cheny on 2018/1/7.
 */
public class Apple extends Food {

    private String color;

    public Apple() {
    }

    public Apple(String name, Date createDate, Integer shelf) {
        super(name, createDate, shelf);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
