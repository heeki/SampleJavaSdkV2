package cloud.heeki;

import cloud.heeki.helpers.DynamoDbHelper;
import cloud.heeki.helpers.S3Helper;

public class App {
    public static void main(String[] args) {
        // S3Helper s3h = new S3Helper();
        // s3h.executeS3Transfer("A001_C013_121586.mov");
        String table = System.getenv("O_DDB_TABLE");
        DynamoDbHelper ddbh = new DynamoDbHelper(table);
        ddbh.executeDynamoDBActions("hello world!");
        ddbh.close();
    }
}