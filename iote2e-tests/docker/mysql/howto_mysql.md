- create my.cnf with bind-address 0.0.0.0
- docker build --memory=16gb -t iote2e_mysql:5.6 .

docker create --name=iote2e_mysql56 -p 3366:3306 iote2e_mysql:5.6

