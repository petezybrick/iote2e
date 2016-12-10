
java -cp iote2e-ruleproc-1.0.0.jar com.pzybrick.iote2e.ruleproc.kafkademo.KafkaStringDemo 15 "iote2e-zoo2:2181" "iote2e-group-sandbox" "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox" "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"

java -cp iote2e-ruleproc-1.0.0.jar com.pzybrick.iote2e.ruleproc.kafkademo.KafkaAvroDemo 1000 "iote2e-zoo2:2181" "iote2e-group-sandbox" "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox" "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"

Setup to run unit tests with Iote2eRequests inbound from Kafka
Note: can't use Kafka under Docker to run jUnit tests, the hostname returned is based on the hostname in the docker
Here is a solid writeup: http://www.michael-noll.com/blog/2013/03/13/running-a-multi-broker-apache-kafka-cluster-on-a-single-node/ 
Create the 3x server-n.properties files as per the above link
Start local Zookeeper and Kafka w/ 3 brokers
./bin/zookeeper-server-start.sh config/zookeeper.properties


jUnit environment vars
KAFKA_GROUP_UNIT_TEST iote2e-group-sandbox
KAFKA_TOPIC_UNIT_TEST com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox
KAFKA_BOOTSTRAP_SERVERS_UNIT_TEST localhost:9091,localhost:9092,localhost:9093
KAFKA_ZOOKEEPER_UNIT_TEST localhost:2182
KAFKA_STREAM_CONSUMER_NUM_THREADS_UNIT_TEST 3

REQUEST_CONFIG_JSON_FILE_KAFKA


Spark Kafka Consumer
https://github.com/dibbhatt/kafka-spark-consumer
Follow the directions to build the jar and install in local maven repo
>>> that ran into dependency hell, cloned into workspace and changed pom to point to spark 2.0.2 and scala versions



