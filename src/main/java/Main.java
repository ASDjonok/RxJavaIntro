/*import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;*/

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class Main {
    public static void main(String[] args) {
        Scheduler computation = Schedulers.computation();

        long startTimeMillis = System.currentTimeMillis();

        /*Observable.range(1, 10)
                .map(Main::myDouble)
                .subscribe(Main::print);*/

        Observable.range(1, 10)
                .flatMap(integer -> Observable.just(integer)
                        .map(Main::myDouble)
                        .subscribeOn(computation)
                )
                /*.toBlocking()
                .subscribe(Main::print);*/
                .blockingForEach(Main::print);

        System.out.println("Time: " + (System.currentTimeMillis() - startTimeMillis));
    }

    private static void print(int i) {
        System.out.println("Thread name = " + Thread.currentThread().getName());
        System.out.println(i);
    }

    private static int myDouble(int i) {
        try {
            System.out.println("MyDouble: Thread name = " + Thread.currentThread().getName());
            Thread.sleep(1000);
            return i * 2;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
