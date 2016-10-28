#!/bin/bash
mkdir -p /var/run/zookeeper
echo $MYID_SERVER_ID > /var/run/zookeeper/myid
/opt/zookeeper/bin/zkServer.sh $1