# IoTE2E - Internet of Things End to End

## Network Addresses

### Docker Network
docker network create --driver=bridge \
--subnet=192.168.21.0/24 --gateway=192.168.21.1 \
--ip-range=192.168.21.128/25 iote2enet

sudo docker network ls

### Docker Cluster Addressing
- All servers are created in clusters with fixed address blocks for easy identification of server groups
- All servers have a VOLUME specifying /tmp/iote2e. This is mapped against the local systems /tmp folder when the container is created in the Docker Compose, this helps to copy any files for configuration and/or experimentation
- The addresses start on .10 divisible boundaries, i.e. 192.168.21.10, 192.168.21.20, etc.
- Demo Manager instances: start address 192.168.21.10
- Spark cluster: start address 192.168.21.20
- Ignite cluster: start address 192.168.21.30
- Kafka cluster: start address 192.168.21.40 (zoo1-zoo3 .41-.43, Kafka starts .44)
- ClientMgrWebSocket cluster: start address 192.168.21.50
- RealtimeWebsocket Server cluster: start address 192.168.21.60
- MySQL database cluster: start address 192.168.21.70
- RiakTS database cluster: start address 192.168.21.80
- Cassandra cluster: start address 192.168.21.90


### Zookeeper
- this assumes Zookeeper 3.4.8, change as necessary if you use another version.
- cd to folder: iote2e-common/docker/zookeeper
- to create the image: docker build -t zookeeper:3.4.8 .
- use the docker-compose.yml to create the 2 containers
- optionally create a single container and exec into it to review it's configuration
docker create --name=zookeeper -p 2181:2181 zookeeper:3.4.8
docker start zookeeper
docker exec -it zookeeper /bin/bash


https://zookeeper.apache.org/doc/r3.1.2/zookeeperAdmin.html#sc_configuration
cluster: https://zookeeper.apache.org/doc/r3.1.2/zookeeperAdmin.html



Reset
remove iote2enet

