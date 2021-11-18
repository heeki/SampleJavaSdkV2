include etc/environment.sh

mvn: mvn.package mvn.exec
mvn.package:
	mvn clean package
mvn.exec:
	rm -f tmp/A001_C013_121586.mov
	mvn exec:java

ddb: ddb.package ddb.deploy
ddb.package:
	sam package -t ${DDB_TEMPLATE} --output-template-file ${DDB_OUTPUT} --s3-bucket ${S3BUCKET}
ddb.deploy:
	sam deploy -t ${DDB_OUTPUT} --stack-name ${DDB_STACK} --parameter-overrides ${DDB_PARAMS} --capabilities CAPABILITY_NAMED_IAM
ddb.describe:
	aws dynamodb describe-table --table-name ${O_DDB_TABLE} | jq