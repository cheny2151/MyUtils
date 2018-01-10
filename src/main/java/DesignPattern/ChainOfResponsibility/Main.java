package DesignPattern.ChainOfResponsibility;

import java.util.Date;

/**
 * Created by cheny on 2018/1/7.
 */
public class Main {

    public static void main(String[] args) {

        Apple apple = new Apple();

        apple.setShelf(1);
        apple.setName("red apple");
        apple.setCreateDate(new Date());
        apple.setColor("red");

        boolean start = DefaultFoodFilterChain.start(apple);
        System.out.println(start);
        DefaultFoodFilterChain.add(new InformationFilter());
        System.out.println(DefaultFoodFilterChain.start(apple));
    }

}
