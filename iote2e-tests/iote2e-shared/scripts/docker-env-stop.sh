#!/bin/bash
# $1=iote2e-common/docker path 
docker-compose --file $1/demomgr/docker-compose.yml stop
docker-compose --file $1/mysql/docker-compose.yml stop
docker-compose --file $1/ws/docker-compose.yml stop
docker-compose --file $1/spark-cluster/docker-compose.yml stop
docker-compose --file $1/kafka-cluster/docker-compose.yml stop
docker-compose --file $1/ignite/docker-compose.yml stop
docker-compose --file $1/cassandra/docker-compose.yml stop

sleep 1
docker ps
