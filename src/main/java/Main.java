import io.reactivex.Observable;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Observable.just("Hello world!")
                .subscribe(a -> System.out.println(a));

    }
}
