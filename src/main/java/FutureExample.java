import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Thread name = " + Thread.currentThread().getName());

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread name2 = " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hi";
        });

        future.thenAccept(result -> System.out.println(result));

                future.get();
//        Thread.sleep(2000);
    }
}
