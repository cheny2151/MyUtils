package cache;

import DesignPattern.observer.single.Student;
import cache.holder.DefaultEntityBufferHolder;

import java.util.List;

/**
 * @author cheney
 * @date 2020-09-01
 */
public class Main {

    public static void main(String[] args) {
        DefaultEntityBufferHolder holder = new DefaultEntityBufferHolder();
        holder.refreshCache(Student.class);
        List<Student> allCache = holder.getAllCache(Student.class);
        System.out.println(allCache);
    }

}
