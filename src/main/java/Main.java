import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /*Observable.just("Hello world!")
                .subscribe(a -> System.out.println(a));*/

        Observable.range(1, 10)
                .subscribeOn(Schedulers.computation())
                .subscribe(Main::print);
    }

    private static void print(int i) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Thread name = " + Thread.currentThread().getName());
        System.out.println(i);
    }
}
