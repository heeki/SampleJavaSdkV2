package cloud.heeki;

import cloud.heeki.helpers.DynamoDbHelper;
import cloud.heeki.helpers.S3Helper;

public class App {
    public static void runS3() {
        String bucket = System.getenv("P_S3_BUCKET");
        String prefix = System.getenv("P_S3_PREFIX");
        String file = System.getenv("P_S3_FILE");
        S3Helper s3h = new S3Helper(bucket, prefix);
        s3h.executeS3Transfer(file);
    }

    public static void runDDB() {
        String table = System.getenv("O_DDB_TABLE");
        DynamoDbHelper ddbh = new DynamoDbHelper(table);
        ddbh.executeDynamoDBActions("hello world!");
        ddbh.close();
    }

    public static void main(String[] args) {
        runS3();
        runDDB();
    }
}