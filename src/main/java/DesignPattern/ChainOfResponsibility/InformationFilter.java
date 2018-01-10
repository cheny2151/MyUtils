package DesignPattern.ChainOfResponsibility;

/**
 * Created by cheny on 2018/1/8.
 */
public class InformationFilter extends FoodFilterChain {

    public InformationFilter() {
    }

    public InformationFilter(FoodFilterChain next) {
        super(next);
    }

    @Override
    public boolean doFilter(Food food) {
        System.out.println("info running...");
        if (food.getCreateDate() == null || food.getName() == null || "".equals(food.getName()) || food.getShelf() == null) {
            System.out.println("食品缺少必要信息");
            return false;
        }
        return !hasNext() || getNext().doFilter(food);
    }

}
