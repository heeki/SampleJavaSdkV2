## Sample AWS SDK Java v2 Enhanced Client Applications
The repository below implements two enhanced clients in the AWS SDK Java v2.
* [DynamoDB Enhanced Client](https://github.com/aws/aws-sdk-java-v2/tree/master/services-custom/dynamodb-enhanced)
* [S3 Transfer Mananager](https://github.com/aws/aws-sdk-java-v2/tree/master/services-custom/s3-transfer-manager)

## DynamoDB Enhanced Client
The [developer guide for the DynamoDB Enhanced Client](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-enhanced.html) was used as a reference when creating this sample application.

A sample `Item` bean was created to encapsulate basic functionality for interacting with a DynamoDB table. An [AWS SAM template](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-specification.html) was written to deploy the sample DynamoDB table. The name of the deployed table is stored in the `O_DDB_TABLE` environment variable.

A helper class was created for constructing the enhanced client, which is then instantiated with the `O_DDB_TABLE` environment variable and then used for interacting with the table.

## S3 Transfer Manager
The [developer guide for S3 Transfer Manager](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/transfer-manager.html) was used as a reference when creating this sample application.

A helper class was created for constructing the transfer manager client, which is then used for downloading large files from an S3 bucket, as identified by the `O_FILE` environment variable.

## Deploy Infrastructure
First deploy the `iac/dynamodb.yaml` template into your AWS account. To simplify the deployment, configure an `etc/environment.sh` configuration file as follows.

```bash
S3BUCKET=[name_of_your_s3_bucket]
PROFILE=[aws_cli_profile_with_credentials_to_access_your_account]

P_NAME=[name_of_the_dynamodb_table_to_create]
DDB_STACK=[name_of_the_cloudformation_stack_to_create]
DDB_TEMPLATE=iac/dynamodb.yaml
DDB_OUTPUT=iac/dynamodb_output.yaml
DDB_PARAMS="ParameterKey=pName,ParameterValue=${P_NAME}"
```

After setting up the environment file, you can execute `make ddb` to deploy the DynamoDB table into your account.

## Execute Sample Application
The application depends on four environment variables for execution.

```bash
export O_DDB_TABLE=[name_of_your_dynamodb_table]
export P_S3_BUCKET=[name_of_your_s3_bucket]
export P_S3_PREFIX=[s3_prefix_where_your_object_is_stored]
export P_S3_FILE=[name_of_your_s3_object]
```

After setting these environment variables, you can execute `make mvn` to package your application and execute the jar artifact.