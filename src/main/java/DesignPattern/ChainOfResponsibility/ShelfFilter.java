package DesignPattern.ChainOfResponsibility;

import java.util.Date;

/**
 * Created by cheny on 2018/1/8.
 */
public class ShelfFilter extends FoodFilterChain {

    public ShelfFilter() {
    }

    public ShelfFilter(FoodFilterChain next) {
        super(next);
    }

    @Override
    public boolean doFilter(Food food) {
        if ((food.getCreateDate().getTime() + food.getShelf() * 24 * 60 * 60 * 1000) < (new Date()).getTime()) {
            System.out.println("食品已过保质期");
            return false;
        }
        return !hasNext() || getNext().doFilter(food);
    }
}
