

### Zookeeper
- this assumes Zookeeper 3.4.8, change as necessary if you use another version.
- cd to folder: docker/zookeeper
- to create the image: docker build -t zookeeper:3.4.8 .
- use the docker-compose.yml to create the 2 containers
- optionally create a single container and exec into it to review it's configuration
docker create --name=zoo1 --hostname=zoo1 -p 2181:2181 zookeeper:3.4.8
docker start zoo3
docker exec -it zoo3 /bin/bash


https://zookeeper.apache.org/doc/r3.1.2/zookeeperAdmin.html#sc_configuration
cluster: https://zookeeper.apache.org/doc/r3.1.2/zookeeperAdmin.html





