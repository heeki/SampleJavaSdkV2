include etc/environment.sh

mvn: mvn.package mvn.exec
mvn.package:
	mvn clean package
mvn.exec:
	rm -f tmp/${P_S3_FILE}
	mvn exec:java

ddb: ddb.package ddb.deploy
ddb.package:
	sam package -t ${DDB_TEMPLATE} --output-template-file ${DDB_OUTPUT} --s3-bucket ${S3BUCKET}
ddb.deploy:
	sam deploy -t ${DDB_OUTPUT} --stack-name ${DDB_STACK} --parameter-overrides ${DDB_PARAMS} --capabilities CAPABILITY_NAMED_IAM
ddb.describe:
	aws dynamodb describe-table --table-name ${O_DDB_TABLE} | jq

release: mvn.package spotbugs
spotbugs:
	java -jar ${SPOTBUGS_HOME}/lib/spotbugs.jar -textui ${P_TARGET_JAR} | tee target/spotsbugs.txt
pmd.json:
	${PMD_HOME}/bin/run.sh pmd -d ${P_SOURCE_DIR} -R rulesets/java/quickstart.xml -f json | tee target/pmd.json | jq
pmd.text:
	${PMD_HOME}/bin/run.sh pmd -d ${P_SOURCE_DIR} -R rulesets/java/quickstart.xml -f text