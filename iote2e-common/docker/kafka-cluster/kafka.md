kafka 0.10.1.0, scala 2.11, zookeeper 3.4.8 
create/find zookeeper


http://alvinhenrick.com/2014/08/18/apache-storm-and-kafka-cluster-with-docker/

+ create docker network
sudo docker network create --driver=bridge \
--subnet=192.168.21.0/24 --gateway=192.168.21.1 \
--ip-range=192.168.21.128/25 iote2enet

sudo docker network ls

- zookeeper
 - review the zoo.cfg - note the ip addresses

bin/kafka-console-producer.sh --broker-list localhost:9092 --topic pz-topic 

bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic kafka-monitor-topic --from-beginning

java -cp iote2e-common-1.0.0.jar com.pzybrick.learn.kafka.ConsumerDemoMaster

kafka monitor
https://github.com/linkedin/kafka-monitor 
cd to kafka-monitor installation
./bin/end-to-end-test.sh --topic test --broker-list localhost:9091,localhost:9092,localhost:9092 --zookeeper localhost:2181,localhost:2182,localhost:2183
localhost:9091,localhost:9092,localhost:9092
    networks:
      default:
        ipv4_address: 192.168.21.45
    depends_on:
      - iote2e-zoo1
      - iote2e-zoo2
      - iote2e-zoo3
    environment:
      KAFKA_LOG_DIRS: /kafka
      KAFKA_BROKER_ID: 2
      KAFKA_ADVERTISED_PORT: 9092
      KAFKA_LOG_RETENTION_HOURS: 168
      KAFKA_LOG_RETENTION_BYTES: 100000000
      KAFKA_ZOOKEEPER_CONNECT: iote2e-zoo1:2181,iote2e-zoo2:2181,iote2e-zoo3:2181