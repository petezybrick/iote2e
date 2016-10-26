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

