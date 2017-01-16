#!/bin/bash
# $1=Kafka installation path, $2=Cassandra installation path
echo "Starting Cassandra"
$2/bin/cassandra -p $2/casspid.txt
$1/bin/zookeeper-server-start.sh $1/config/zookeeper.properties &
echo "Sleeping for 5 seconds for Zookeeper to start"
sleep 5
$1/bin/kafka-server-start.sh $1/config/server-0.properties &
$1/bin/kafka-server-start.sh $1/config/server-1.properties &
$1/bin/kafka-server-start.sh $1/config/server-2.properties &

