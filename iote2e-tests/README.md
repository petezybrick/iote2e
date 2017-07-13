
java -cp iote2e-stream-1.0.0.jar com.pzybrick.iote2e.stream.kafkademo.KafkaStringDemo 15 "iote2e-zoo2:2181" "iote2e-group-sandbox" "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox" "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"

java -cp iote2e-stream-1.0.0.jar com.pzybrick.iote2e.stream.kafkademo.KafkaAvroDemo 1000 "iote2e-zoo2:2181" "iote2e-group-sandbox" "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox" "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"

**Start local unit test environment**
This single batch file will do the steps below - start/stop Kafka/Zookeeper, Cassandra and Ignite
Start
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/scripts
	unit-test-env-start.sh arguments: $1=Kafka installation path, $2=Cassandra installation path, $3=Ignite installation path $4=Ignite config file path/name
	run start script: ./unit-test-env-start.sh /home/pete/development/server/kafka_2.10-0.10.0.0 /home/pete/development/server/apache-cassandra-3.9 /home/pete/development/server/apache-ignite-fabric-1.8.0-bin /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/config_ignite/ignite-iote2e-local-peer-false.xml
	open another terminal session/tab
	cd to scripts folder
Run  
	run tests, i.e. ./run-junit-tests-ksi.sh
Stop
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-scripts
	run stop script: ./unit-test-env-stop.sh /home/pete/development/server/kafka_2.10-0.10.0.0 /home/pete/development/server/apache-cassandra-3.9


**Start Kafka/Zookeeper local**
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
	./bin/kafka-topics.sh  --create --topic com.pzybrick.iote2e.ws.omh.ThreadEntryPointOmh-sandbox --zookeeper localhost:2181 --replication-factor 3  --partitions 3
	./bin/kafka-topics.sh --zookeeper localhost:2181 --list
env JMX_PORT=9999 ./bin/kafka-server-start.sh config/server-0.properties
env JMX_PORT=10000 ./bin/kafka-server-start.sh config/server-1.properties
env JMX_PORT=10001 ./bin/kafka-server-start.sh config/server-2.properties

**Start Cassandra single node local**
start another terminal tab or session
cd to cassandra folder: cd /home/pete/development/server/apache-cassandra-3.9
Settings to minimize Cassandra memory usage for development - if not set, Cassandra will use memory as if your machine is only running Cassandra
export MAX_HEAP_SIZE=1G
export HEAP_NEWSIZE=200M
./bin/cassandra -f

**Run local Ignite**
*open a terminal session or tab*
cd /home/pete/development/server/apache-ignite-fabric-1.8.0-bin
export IGNITE_HOME="/home/pete/development/server/apache-ignite-fabric-1.8.0-bin"
export IGNITE_VERSION="1.8.0"
export DEFAULT_CONFIG="/home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/config_ignite/ignite-iote2e-local-peer-false.xml"
export JVM_OPTS="-Xms1g -Xmx2g -server -XX:+AggressiveOpts -XX:MaxMetaspaceSize=256m"

./bin/ignite.sh "/home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/config_ignite/ignite-iote2e-local-peer-false.xml"
*then run the jUnit tests via IDE, i.e. Eclipse*



Start/Stop/Update Docker Environment
Start
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/scripts
	run the start script: ./docker-env-start.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker
Stop
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/scripts
	run the stop script: ./docker-env-stop.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker
Update
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/scripts
	run the update script: ./docker-env-up.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker
Remove
	open terminal session
	cd to scripts folder: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/scripts
	run the remove script: ./docker-env-rm.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker
	
Run Spark unit tests under Docker
- start docker environment as per above
- bring up the Spark console - open browser, http://localhost:8080
- ensure that the iote2e-stream application has been rebuilt via maven, which will copy to docker shared jars folder
- cd to local spark folder, i.e. `cd /home/pete/development/server/spark-2.0.2-bin-hadoop2.7`
- submit iote2e-stream spark job	

**Reset actuator state before running any tests**
- docker exec -it iote2e-demomgr1 /bin/bash
- cd /tmp/iote2e-shared/scripts
- ./reset-pyclient-actuator-state.sh iote2e-cassandra1 iote2e all

**Batch layer - writes to db**
./bin/spark-submit \
  --class com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer \
  --deploy-mode cluster \
  --master spark://localhost:6066 \
  --executor-memory 8G \
  --executor-cores 2 \
  --total-executor-cores 6 \
  /tmp/iote2e-shared/jars/iote2e-stream-1.0.0.jar \
  master_spark_run_docker_batch_config iote2e-cassandra1 iote2e

**Speed layer - runs rules**
./bin/spark-submit \
  --class com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer \
  --deploy-mode cluster \
  --master spark://localhost:6066 \
  --executor-memory 8G \
  --executor-cores 2 \
  --total-executor-cores 8 \
  /tmp/iote2e-shared/jars/iote2e-stream-1.0.0.jar \
  master_spark_run_docker_speed_config iote2e-cassandra1 iote2e


- review the returned json for "success":true
		17/02/04 12:41:01 INFO RestSubmissionClient: Server responded with CreateSubmissionResponse:
		{
		  "action" : "CreateSubmissionResponse",
		  "message" : "Driver successfully submitted as driver-20170204174101-0000",
		  "serverSparkVersion" : "2.0.2",
		  "submissionId" : "driver-20170204174101-0000",
		  "success" : true
		}

- at this point the iote2e-stream spark streaming job is running and reading from the stream that is connected to kafka, but no messages are being sent yet
- run SimTempToFan
	- this should turn the fan on at 80 degrees, and start the temperature decreasing
	- when the temp hits 77 degrees, it should turn the fan off and start the temperature increasing
	- connect to demomgr1: docker exec -it iote2e-demomgr1 /bin/bash
	- set env vars for Cassandra:
export CASSANDRA_CONTACT_POINT=iote2e-cassandra1
export CASSANDRA_KEYSPACE_NAME=iote2e
	- cd to shared scripts folder: 
cd /tmp/iote2e-shared/scripts
	- if any MasterConfig settings have changed, then reload - see steps below
	- run SimTempToFan: 
./run-junit-tests-simws-temptofan-docker.sh
	- run SimHumidityToMister: 
./run-junit-tests-simws-humiditytomister-docker.sh
./run-junit-tests-simws-ledgreen-docker.sh
Optionally tail the log in the iote2e-ws1 instance

./run-junit-tests-sim-temptofan-docker.sh
./run-junit-tests-sim-humiditytomister-docker.sh
./run-junit-tests-sim-ledgreen-docker.sh



** ConfigInitialLoad on Cassandra Docker instance
rebuild iote2e-tests, which will also copy to iote2e-tests/iote2e-shared/jars
docker exec -it iote2e-demomgr1 /bin/bash
export CASSANDRA_CONTACT_POINT=iote2e-cassandra1
export CASSANDRA_KEYSPACE_NAME=iote2e
cd /tmp/iote2e-shared
java -cp jars/iote2e-tests-1.0.0.jar com.pzybrick.iote2e.tests.common.ConfigInitialLoad config_initial_load
+ Verify
docker exec -it iote2e-cassandra1 /bin/bash
csqlsh iote2e-cassandra1
select config_name from config;


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
  to run: java -cp <PATH>/iote2e/iote2e-stream/target/iote2e-stream-1.0.0-all-tests.jar org.junit.runner.JUnitCore <TEST_PKG>.<TEST_CLASS>
  

**Python Cassandra**
sudo pip install cassandra-driver


**MySQL**
Initializing
1. start MySQL 5.6
	1.1 cd to docker/mysql folder
	1.2 docker-compose up -d
2. docker exec -it iote2e-mysql-master /bin/bash
3. mysql -u root -p
	3.1 password: Password*8
4. At the prompt, enter the following to create the iote2e_batch user and dbiote2e_batch database
	source /tmp/iote2e-shared/sql/mysql_create_batch_db_user.sql
5. Enter the following statement to create the tables:
	source /tmp/iote2e-shared/sql/mysql_create_batch_tables.sql
6. Exit from mysql command line, enter: exit
7. Exit from iote2e-mysql-master, enter: exit


**Pill Dispenser**
1. Truncate the pill_dispenser table
2. Populate pill_dispenser table as per the sql in mysql_create_pills_dispensed_tables.sql
3. Start the Python PillDispenser or PillDispenser simulator on RPi  
python -m iote2epyclient.launch.clientlauncher 'ProcessSimPillDispenser' 'pill_image1' '/home/pete/iote2epyclient/avro-schemas/avro/' 'ws://192.168.1.7:8090/iote2e/' 'pzybrick1' 'rpi-001' '/home/pete/iote2epyclient/log-configs/client_consoleonly.conf' ''
4. Bring up demomgr: docker exec -it iote2e-demomgr1 /bin/bash 
4. Start the PillDispenser - this will read the pill_dispenser table and start the sequence
	java -cp /tmp/iote2e-shared/jars/iote2e-tests-1.0.0.jar -Xms512m -Xmx512m com.pzybrick.iote2e.stream.pilldisp.PillDispenser master_spark_run_docker_batch_config iote2e-cassandra1 iote2e
	
	
**TODO: Master Network Setup**
I think it's easier for development to have fixed IP's and system names. I have a router at 192.168.1.1. 
Note that these exact settings might not work for you and you may need to adjust them. This is my setup:
	192.168.1.239 (pz-win7-1) - Win7 for presentations
	192.168.1.240 (hp-lt-ubunutu-1) Ubuntu - primary development/run machine, this is where Docker is running
	192.168.1.241 (rpi-001) RPi 1
	192.168.1.242 (rpi-002) RPi 2
	192.168.1.243 (rpi-003) RPi 3
	192.168.1.244 (rpi-004) RPi 4
Netmask: 255.255.255.0 (aka 24)
Gateway: 192.168.1.1
DNS Servers: 192.168.1.1
Search domains: home

Update the /etc/hosts file to reference the "other" systems, i.e. for rpi-002:
	127.0.0.1		localhost
	127.0.1.1		rpi-002
	192.168.1.239	pz-win7-1
	192.168.1.240	hp-lt-ubuntu-1
	192.168.1.241	rpi-001
	192.168.1.243	rpi-003
	192.168.1.244	rpi-004
	
	