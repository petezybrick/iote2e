docker build -t ignite:1.7.0 .


docker rm iote2e-ignite1y
docker rm iote2e-ignite2y
docker rm iote2e-ignite1y

docker rmi ignite:1.8.0

docker build -t ignite:1.8.0 .

logging: cd /opt/ignite/apache-ignite-fabric-1.8.0-bin/work/log/ignite-xxx.log




