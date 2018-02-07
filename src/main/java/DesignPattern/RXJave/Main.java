package DesignPattern.RXJave;

import jdk.nashorn.internal.objects.annotations.Where;
import org.junit.Test;

public class Main {

    @Test
    public void test() {
        Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            for (int i = 0; i < 10; i++) {
                subscriber.onNext(i);
            }
        }).subscribe(new AbstractSubscriber<Integer>() {
            @Override
            void onStart() {
                System.out.println("start");
            }

            @Override
            public void onNext(Integer var1) {
                System.out.println(var1);
                if (var1==8){
                    throw new RuntimeException("...");
                }
            }

            @Override
            public void onCompleted() {
                System.out.println("文勇吃屎");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("error");
            }
        });
    }

}
