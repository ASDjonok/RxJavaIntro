package futures;

import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        futureTest();
        completableFutureTest();

        Thread.sleep(2000);
    }

    private static void completableFutureTest() throws InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture =
                CompletableFuture.supplyAsync(Main::slowInit);
        integerCompletableFuture.thenAccept(integer -> System.out.println("YO! " + integer));

        CompletableFuture<Integer> integerCompletableFuture2 =
                CompletableFuture.supplyAsync(Main::slowInit2);

        /*CompletableFuture<Integer> integerCompletableFuture3 =
                integerCompletableFuture.thenCombine(integerCompletableFuture2,
                        Integer::sum);*/

        CompletableFuture<Integer> integerCompletableFuture4 =
                CompletableFuture.supplyAsync(Main::slowInit3);

        List<CompletableFuture<Integer>> completableFutures =
                Arrays.asList(integerCompletableFuture, integerCompletableFuture2,
                integerCompletableFuture4);

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(integerCompletableFuture, integerCompletableFuture2,
                integerCompletableFuture4);

        CompletableFuture<List<Integer>> listCompletableFuture = voidCompletableFuture.thenApply(v -> {
            return completableFutures.stream()
                    .map(completableFuture -> completableFuture.join())
                    .collect(Collectors.toList());
        });

        CompletableFuture<Optional<Integer>> optionalCompletableFuture = listCompletableFuture.thenApply(integers -> integers.stream().reduce(Integer::sum));
        System.out.println( "!!!!!" +
                optionalCompletableFuture.join().get());

        /*CompletableFuture<Integer> integerCompletableFuture5 =
                integerCompletableFuture3.thenCombine(integerCompletableFuture4,
                        Integer::sum);*/

//        integerCompletableFuture3.thenAccept(integer -> System.out.println("SUM! " + integer));

        for (int i = 0; i < 15; i++) {
            Thread.sleep(1000);
            System.out.println("Main");
        }

        integerCompletableFuture.join();
    }

    static void futureTest() throws ExecutionException, InterruptedException {
        Callable<Integer> integerCallable = Main::slowInit;

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Future<Integer> future = executorService.submit(integerCallable);
//        Future<Integer> future2 = executorService.submit(integerCallable);

        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            System.out.println("MAIN!!");
        }

        if (future.isDone()) {
            System.out.println(future.get());
        }
//        System.out.println(future2.get());


        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            System.out.println("MAIN2!!");
        }


        if (future.isDone()) {
            System.out.println(future.get());
        }

        System.out.println("AAAAAAAAAAAAAAAA!");

//        if (future.isDone()) {
            System.out.println(future.get());
//        }

        future.cancel(true);
//        future2.cancel(true);
        executorService.shutdown();
    }

    static int slowInit() {
        System.out.println("Started slowInit() " + Thread.currentThread().getName());
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

    static int slowInit2() {
        System.out.println("Started slowInit() " + Thread.currentThread().getName());
        try {
            for (int i = 0; i < 7; i++) {
                Thread.sleep(1000);
                System.out.println(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2;
    }

    static int slowInit3() {
        System.out.println("Started slowInit() " + Thread.currentThread().getName());
        try {
            for (int i = 0; i < 2; i++) {
                Thread.sleep(1000);
                System.out.println(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 3;
    }
}
