#!/bin/bash
echo "Stopping Cassandra"
kill `cat $2/casspid.txt`
# $1=Kafka installation path, $2=Cassandra installation path
$1/bin/kafka-server-stop.sh $1/config/server-0.properties
$1/bin/kafka-server-stop.sh $1/config/server-1.properties
$1/bin/kafka-server-stop.sh $1/config/server-2.properties
echo "Sleeping for 15 seconds for Kafka servers to stop"
sleep 15
echo "Stopping Zookeepr"
$1/bin/zookeeper-server-stop.sh $1/config/zookeeper.properties
sleep 7
ps -ef | grep kafka