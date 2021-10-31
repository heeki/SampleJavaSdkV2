mvn: mvn.package mvn.exec
mvn.package:
	mvn package
mvn.exec:
	rm -f tmp/A001_C013_121586.mov
	mvn exec:java