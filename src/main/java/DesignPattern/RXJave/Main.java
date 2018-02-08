package DesignPattern.RXJave;

import org.junit.Test;

public class Main {

    @Test
    public void test() {
        Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            for (int i = 0; i < 10; i++) {
                subscriber.onExecute(1);
            }
        }).map1(String::valueOf).subscribe(new AbstractSubscriber<String>() {
            @Override
            public void onExecute(String var1) {
                System.out.println(var1);
            }
        });
    }


}
