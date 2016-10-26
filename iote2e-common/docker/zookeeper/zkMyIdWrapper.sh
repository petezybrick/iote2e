#!/bin/bash
mkdir -p /var/run/zookeeper
echo "1" > /var/run/zookeeper/myid
/opt/zookeeper/bin/zkServer.sh $1