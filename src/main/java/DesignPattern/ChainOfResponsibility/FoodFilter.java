package DesignPattern.ChainOfResponsibility;

/**
 * 行为设计模式:责任链模式
 * 过滤器接口
 * Created by cheny on 2018/1/7.
 */
public interface FoodFilter {

    /**
     * 过滤食物
     */
    boolean doFilter(Food food);

}
