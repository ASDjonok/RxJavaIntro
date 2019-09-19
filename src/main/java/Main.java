import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scheduler computation = Schedulers.computation();

        Observable.range(1, 10)
                .flatMap(integer -> Observable.just(integer)
                        .map(Main::myDouble)
                        .subscribeOn(computation)
                )
//                .map(Main::myDouble)
//                .subscribeOn(computation)
                .subscribe(Main::print);

        Thread.sleep(12000);
//        Thread.currentThread().join();
    }

    private static void print(int i) throws InterruptedException {
//        Thread.currentThread().join();
        Thread.sleep(1000);
        System.out.println("Thread name = " + Thread.currentThread().getName());
        System.out.println(i);
    }

    private static int myDouble(int i) throws InterruptedException {
//        Thread.currentThread().join();
        Thread.sleep(1000);
        System.out.println("MyDouble: Thread name = " + Thread.currentThread().getName());
        return i * 2;
    }
}
