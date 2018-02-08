package DesignPattern.RXJave;

import org.junit.Test;

public class Main {

    @Test
    public void test() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(AbstractSubscriber<Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onExecute(i);
                }
            }
        }).map(from -> from+"after").subscribe(new AbstractSubscriber<String>() {
            @Override
            public void onExecute(String var1) {
                System.out.println(var1);
            }
        });
    }


}
