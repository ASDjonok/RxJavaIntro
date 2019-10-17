import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.query.*;

public class CBExample {

    public static void main(String... args) throws Exception {

        // Initialize the Connection
        Cluster cluster = CouchbaseCluster.create("localhost");
        cluster.authenticate("Administrator", "adminadmin");
        Bucket bucket = cluster.openBucket("my-bucket");

        // Create a JSON Document
        JsonObject arthur;

        for (int i = 0; i < 100000; i++) {
            arthur = JsonObject.create()
                    .put("name", "Arthur "+i)
                    .put("email", "kingarthur@couchbase.com")
                    .put("interests", JsonArray.from("Holy Grail", "African Swallows"+i));

            // Store the Document
            bucket.upsert(JsonDocument.create("u:king_arthur" + i, arthur));
        }

        // Load the Document and print it
        // Prints Content and Metadata of the stored Document
        System.out.println(bucket.get("u:king_arthur"));

        // Create a N1QL Primary Index (but ignore if it exists)
        bucket.bucketManager().createN1qlPrimaryIndex(true, false);

        // Perform a N1QL Query
        N1qlQueryResult result = bucket.query(
                N1qlQuery.parameterized("SELECT name FROM `my-bucket` WHERE $1 IN interests",
                        JsonArray.from("African Swallows"))
        );

        // Print each found Row
        for (N1qlQueryRow row : result) {
            // Prints {"name":"Arthur"}
            System.out.println(row);
        }
    }
}