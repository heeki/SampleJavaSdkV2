package cloud.heeki.helpers;

import cloud.heeki.beans.Item;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;


import software.amazon.awssdk.awscore.retry.AwsRetryPolicy;
import software.amazon.awssdk.core.client.config.SdkClientConfiguration;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import software.amazon.awssdk.services.dynamodb.DynamoDbRetryPolicy;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputDescription;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;


public class DynamoDbHelper {
    private final Region region = Region.US_EAST_1;
    private final String table;
    private final SdkHttpClient apacheClient;
    private final DynamoDbClient dynamoClient;
    private final DynamoDbEnhancedClient enhancedClient;

    public DynamoDbHelper(String table) {
        this.table = table;
        this.apacheClient = ApacheHttpClient.builder().build();
        this.dynamoClient = DynamoDbClient.builder()
            .httpClient(apacheClient)
            .build();
        this.enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoClient)
            .build();
    }

    public void executeDynamoDBActions(String message) {
        this.putItem(message);
        this.describeDynamoDbTable();
        this.describeClient();
    }

    public void close() {
        this.dynamoClient.close();
    }

    private void describeDynamoDbTable() {
        DescribeTableRequest request = DescribeTableRequest.builder()
            .tableName(this.table)
            .build();
        try {
            TableDescription description = this.dynamoClient.describeTable(request).table();
            if (description != null) {
                System.out.format("table_name=%s\n", description.tableName());
                System.out.format("table_arn=%s\n", description.tableArn());
                System.out.format("table_status=%s\n", description.tableStatus());
                System.out.format("item_count=%d\n", description.itemCount().longValue());
                System.out.format("size_bytes=%d\n", description.tableSizeBytes().longValue());
                System.out.println("attributes:");
                for (AttributeDefinition a : description.attributeDefinitions()) {
                    System.out.format("  %s=%s\n", a.attributeName(), a.attributeType());
                }
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    private void putItem(String message) {
        try {
            String uuid = UUID.randomUUID().toString();
            DynamoDbTable<Item> mappedTable = this.enhancedClient.table(this.table, TableSchema.fromBean(Item.class));
            Item item = new Item();
            item.setId(uuid);
            item.setMessage(message);

            WriteBatch batch = WriteBatch.builder(Item.class)
                .mappedTableResource(mappedTable)
                .addPutItem(r -> r.item(item))
                .build();
            BatchWriteItemEnhancedRequest request = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(batch)
                .build();
            this.enhancedClient.batchWriteItem(request);
            System.out.format("put_item_uuid=%s\n", uuid);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    private void describeClient() {
        // SdkClientConfiguration config = this.dynamoClient.getGlobalInterceptors();
    }
}