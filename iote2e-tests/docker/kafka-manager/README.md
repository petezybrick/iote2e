pull Kafka Manager: https://github.com/yahoo/kafka-manager
cd to kafka-manager root dir
./sbt clean dist
Ran for about 15 minutes
Your package is ready in /home/pete/development/gitrepo/kafka-manager/target/universal/kafka-manager-1.3.2.1.zip
docker build -t kafka-manager:1.3.2.1 .
