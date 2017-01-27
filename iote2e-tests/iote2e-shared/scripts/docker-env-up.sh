#!/bin/bash
# $1=iote2e-common/docker path 
docker-compose --file $1/cassandra/docker-compose.yml up -d
docker-compose --file $1/ignite/docker-compose.yml up -d
docker-compose --file $1/kafka-cluster/docker-compose.yml up -d
docker-compose --file $1/spark-cluster/docker-compose.yml up -d
docker-compose --file $1/demomgr/docker-compose.yml up -d

sleep 1
docker ps
