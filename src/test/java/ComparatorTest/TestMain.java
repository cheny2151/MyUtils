package ComparatorTest;

import DesignPattern.future.Future;
import DesignPattern.future.impl.MyTask;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * public int compare(MyInt o1, MyInt o2) { }
 * 注意点：
 * 1,原位置o1是在o2后面;
 * 2,只有小于0才会交换位置
 */
public class TestMain {

    @Test
    public void test() {
        MyInt myInt = new MyInt();
        MyInt myInt2 = new MyInt();
        MyInt myInt3 = new MyInt();
        MyInt myInt4 = new MyInt();
        MyInt myInt5 = new MyInt();
        myInt.setI(10);
        myInt2.setI(30);
        myInt3.setI(20);
        myInt4.setI(50);
        myInt5.setI(6);
        ArrayList<MyInt> myInts = new ArrayList<MyInt>();
        myInts.add(myInt);
        myInts.add(myInt2);
        myInts.add(myInt3);
        myInts.add(myInt4);
        myInts.add(myInt5);
        for (MyInt myInta : myInts) {
            System.out.println(myInta.getI());
        }
        Collections.sort(myInts, new Comparator<MyInt>() {
            public int compare(MyInt o1, MyInt o2) {
                System.out.println(o1.getI() + "-----" + o2.getI());
                return -o2.getI() + o1.getI();
            }
        });
        for (MyInt myInta : myInts) {
            System.out.println("============" + myInta.getI());
        }
    }

    @Test
    public void test2(){
        MyTask<String> stringMyTask = MyTask.create(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "test";
        });
        Future<String> start = stringMyTask.start();
        System.out.println(start.isComplete());
        System.out.println(stringMyTask.getResultBlock());
        System.out.println(start.isComplete());
    }

}
