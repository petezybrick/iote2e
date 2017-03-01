- create my.cnf with bind-address 0.0.0.0
- docker build --memory=16gb -t mysql:5.7.17 .

docker create --name=mysql56 -p 3366:3306 iote2e_mysql:5.6

5.7.17