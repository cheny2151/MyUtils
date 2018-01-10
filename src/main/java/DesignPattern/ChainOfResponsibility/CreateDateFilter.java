package DesignPattern.ChainOfResponsibility;

import java.util.Date;

/**
 * Created by cheny on 2018/1/8.
 */
public class CreateDateFilter extends FoodFilterChain {

    public CreateDateFilter() {
    }

    public CreateDateFilter(FoodFilterChain next) {
        super(next);
    }

    @Override
    public boolean doFilter(Food food) {
        if (food.getCreateDate().after(new Date())) {
            System.out.println("食品制造时间不合法!!!");
            return false;
        }
        return !hasNext() || getNext().doFilter(food);
    }

}
