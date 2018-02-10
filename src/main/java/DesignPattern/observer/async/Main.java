package DesignPattern.observer.async;

import org.junit.Test;

public class Main {

    @Test
    public void test() throws InterruptedException {
        Observable<Integer> observable = Observable.create(Observable.Action.create(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        }));
        observable.asyncAction();
        observable.subscribe(System.out::println);
        observable.subscribe((future) -> System.out.println("second" + future));
        Thread.sleep(1100);
        observable.subscribe((future) -> System.out.println("three" + future));
        Thread.sleep(100);
    }

}
