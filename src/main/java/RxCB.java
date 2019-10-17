import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
/*import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;*/

public class RxCB {
    static Bucket bucket;

    public static void main(String[] args) {

        // Initialize the Connection
        Cluster cluster = CouchbaseCluster.create("localhost");
        cluster.authenticate("Administrator", "adminadmin");
        bucket = cluster.openBucket("my-bucket");

        Scheduler computation = Schedulers.computation();

        long startTimeMillis = System.currentTimeMillis();

        /*Observable.range(1, 10)
                .map(Main::myDouble)
                .subscribe(Main::print);*/


        Observable.range(1, 10)
                .flatMap(integer -> Observable.just(integer)
                        .map(RxCB::dbGetAccess)
                        .subscribeOn(computation)
                )
                /*.toBlocking()
                .subscribe(Main::print);*/
                .blockingForEach(RxCB::print);

        System.out.println("Time: " + (System.currentTimeMillis() - startTimeMillis));
    }

    private static void print(String s) {
        System.out.println("Thread name = " + Thread.currentThread().getName());
        System.out.println(s);
    }

    private static String dbGetAccess(int i) {
        System.out.println("MyDouble: Thread name = " + Thread.currentThread().getName());
        // Perform a N1QL Query
        N1qlQueryResult result = bucket.query(
                N1qlQuery.parameterized("SELECT name FROM `my-bucket` WHERE $1 IN interests",
                        JsonArray.from("African Swallows"+i))
        );

        /*// Print each found Row
        for (N1qlQueryRow row : result) {
            // Prints {"name":"Arthur"}
            System.out.println(row);
        }*/
        return result.allRows().get(0).toString();
    }
}
