
java -cp iote2e-ruleproc-1.0.0.jar com.pzybrick.iote2e.ruleproc.kafkademo.KafkaStringDemo 15 "iote2e-zoo2:2181" "iote2e-group-sandbox" "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox" "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"

java -cp iote2e-ruleproc-1.0.0.jar com.pzybrick.iote2e.ruleproc.kafkademo.KafkaAvroDemo 1000 "iote2e-zoo2:2181" "iote2e-group-sandbox" "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox" "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"

Setup to run unit tests with Iote2eRequests inbound from Kafka
Note: can't use Kafka under Docker to run jUnit tests, the hostname returned is based on the hostname in docker
Here is a solid writeup: http://www.michael-noll.com/blog/2013/03/13/running-a-multi-broker-apache-kafka-cluster-on-a-single-node/ 
Create the 3x server-n.properties files as per the above link
Start local Zookeeper and Kafka w/ 3 brokers
cd to the Kafka folder: cd /home/pete/development/server/kafka_2.10-0.10.0.0
create 4x tabs in terminal
./bin/zookeeper-server-start.sh config/zookeeper.properties
Start each server in separate console/process
Note: must create the properties files ahead of time as per the above URL
Note: if the topic doesn't already exist, then creat it now:
	open another terminal session, cd to kafka folder
	./bin/kafka-topics.sh  --create --topic com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox --zookeeper localhost:2181 --replication-factor 3  --partitions 3
	./bin/kafka-topics.sh --zookeeper localhost:2181 --list
env JMX_PORT=9999 ./bin/kafka-server-start.sh config/server-0.properties
env JMX_PORT=10000 ./bin/kafka-server-start.sh config/server-1.properties
env JMX_PORT=10001 ./bin/kafka-server-start.sh config/server-2.properties
Start Cassandra single node local
start another terminal tab or session
cd to cassandra folder: cd /home/pete/development/server/apache-cassandra-3.9
./bin/cassandra -f

Start/stop environment for standalone jUnit tests
Start
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/iote2e-scripts
	run start script: ./unit-test-env-start.sh /home/pete/development/server/kafka_2.10-0.10.0.0 /home/pete/development/server/apache-cassandra-3.9
	open another terminal session/tab
	cd to scripts folder
Run
	run tests, i.e. ./run-junit-tests-ksi.sh
Stop
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-scripts
	run stop script: ./unit-test-env-stop.sh /home/pete/development/server/kafka_2.10-0.10.0.0 /home/pete/development/server/apache-cassandra-3.9

Start/Stop/Update Docker Environment
Start
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/scripts
	run the start script: ./docker-env-start.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker
Stop
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/scripts
	run the stop script: ./docker-env-stop.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker
Update
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/scripts
	run the stop script: ./docker-env-up.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker
Remove
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/scripts
	run the stop script: ./docker-env-rm.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker

export CASSANDRA_CONTACT_POINT=iote2e-cassandra1
export CASSANDRA_KEYSPACE_NAME=iote2e
java -cp jars/iote2e-tests-1.0.0.jar com.pzybrick.iote2e.tests.common.ConfigInitialLoad ../config_initial_load


jUnit environment vars - localhost (not under docker)
KAFKA_GROUP_UNIT_TEST iote2e-group-sandbox
KAFKA_TOPIC_UNIT_TEST com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox
KAFKA_BOOTSTRAP_SERVERS_UNIT_TEST localhost:9091,localhost:9092,localhost:9093
KAFKA_ZOOKEEPER_UNIT_TEST localhost:2182
KAFKA_STREAM_CONSUMER_NUM_THREADS_UNIT_TEST 3

REQUEST_CONFIG_JSON_FILE_KAFKA


Spark Kafka Consumer
https://github.com/dibbhatt/kafka-spark-consumer
Follow the directions to build the jar and install in local maven repo
forked kafka-spark-consumer, modified pom for latest versions, got it to build successfully and install local repo

to test:
1. kafka local: start zookeeper and 3x brokers as per above
1. if this is the first run of the test data injection, then create the topic
xx. 
2. From Eclipse, start Iote2eRequestSparkConsumer.Iote2eRequestSparkConsumer with master local[*]
xx. env vars:
3. From Eclipse, start com.pzybrick.test.iote2e.scratchpad.InjectIote2eRequestsToKafka
xx. env vars
4. Monitor the Spark and Injector consoles from Eclipse, monitor Kafka/Zookeeper from terminal sessions
5. Injector will exit upon completion
6. Terminate Spark in Eclipse

Create jar with all test classes so they tests can be run from within Docker network
http://stackoverflow.com/questions/10307652/how-to-include-test-classes-in-jar-created-by-maven-shade-plugin
critical: 
  mvn clean compile test-compile assembly:single
  	>>> creates shaded/uber jar with test classes
  to run: java -cp <PATH>/iote2e/iote2e-ruleproc/target/iote2e-ruleproc-1.0.0-all-tests.jar org.junit.runner.JUnitCore <TEST_PKG>.<TEST_CLASS>
  


