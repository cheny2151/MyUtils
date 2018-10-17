package DesignPattern.future;

public interface Task<T> {

    Future<T> execute();

}
