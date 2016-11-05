docker build -t iote2e-ignite:1.7.0 .


docker rm iote2e-ignite1y
docker rm iote2e-ignite2y
docker rm iote2e-ignite1y

docker rmi iote2e-ignite:1.7.0

docker build -t iote2e-ignite:1.7.0 .

docker run -it --name=iote2e-ignite1 --hostname=iote2e-ignite1 --network iote2enet --ip 192.168.21.31 iote2e-ignite:1.7.0

docker create -it --name=iote2e-ignite1 --hostname=iote2e-ignite1 --network iote2enet --ip 192.168.21.31 iote2e-ignite:1.7.0

docker run -it --name=iote2e-ignite2y --hostname=iote2e-ignite2y --network iote2enet --ip 192.168.21.32 iote2e-ignite:1.7.0y


docker create --name=iote2e-ignite1 --hostname=iote2e-ignite1 iote2e-ignite:1.7.0x



