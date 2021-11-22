package cloud.heeki;

import cloud.heeki.helpers.DynamoDbHelper;
import cloud.heeki.helpers.S3Helper;

public class App {
    /** run sample execution for S3TransferManager */
    public void runS3() {
        String bucket = System.getenv("P_S3_BUCKET");
        String prefix = System.getenv("P_S3_PREFIX");
        String file = System.getenv("P_S3_FILE");
        S3Helper s3h = new S3Helper(bucket, prefix);
        s3h.executeS3Transfer(file);
    }

    /** run sample execution for DynamoDbEnhancedClient */
    public void runDDB() {
        String table = System.getenv("O_DDB_TABLE");
        DynamoDbHelper ddbh = new DynamoDbHelper(table);
        ddbh.executeDynamoDBActions("hello world!");
        ddbh.close();
    }

    /** main execution */
    public static void main(String[] args) {
        App app = new App();
        app.runS3();
        app.runDDB();
    }
}