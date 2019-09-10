package reflect.pojo;

/**
 * @author cheney
 * @date 2019-09-10
 */
public interface People {

    default void run(){
        System.out.println("running...");
    }

}
