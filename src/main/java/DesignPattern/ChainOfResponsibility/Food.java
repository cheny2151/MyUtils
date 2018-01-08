package DesignPattern.ChainOfResponsibility;

import java.util.Date;

/**
 * Created by cheny on 2018/1/7.
 */
public abstract class Food {

    public Food() {
    }

    public Food(String name, Date createDate, Integer shelf) {
        this.name = name;
        this.createDate = createDate;
        Shelf = shelf;
    }

    /**
     * 食品名
     */
    private String name;

    /**
     * 生产日期
     */
    private Date createDate;

    /**
     * 保质期
     */
    private Integer Shelf;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getShelf() {
        return Shelf;
    }

    public void setShelf(Integer shelf) {
        Shelf = shelf;
    }

}
