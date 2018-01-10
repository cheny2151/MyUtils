package DesignPattern.ChainOfResponsibility;

/**
 * Created by cheny on 2018/1/8.
 */
public class DefaultFoodFilterChain {

    private static FoodFilterChain end;

    private static FoodFilterChain getFirst() {
        return DefaultFoodFilterChainHolder.defaultFilter;
    }

    private static class DefaultFoodFilterChainHolder {
        private static FoodFilterChain defaultFilter;

        static {
            end = new ShelfFilter();
            defaultFilter = new InformationFilter(new CreateDateFilter(end));
        }
    }

    public static void add(FoodFilterChain foodFilter) {
        end.setNext(foodFilter);
        end = foodFilter;
    }

    public static boolean start(Food food) {
        return getFirst().doFilter(food);
    }


}
