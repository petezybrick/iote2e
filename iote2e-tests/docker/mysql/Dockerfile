FROM ubuntu:14.04.5
MAINTAINER Pete Zybrick <pzybrick@gmail.com>

RUN apt-get update \
 && echo mysql-server-5.6 mysql-server/root_password password Password*8 | debconf-set-selections \
 && echo mysql-server-5.6 mysql-server/root_password_again password Password*8 | debconf-set-selections \
 && apt-get -y install mysql-server-5.6 \
 && apt-get clean

copy my.cnf /etc/mysql/my.cnf

EXPOSE 3306

CMD ["mysqld_safe"]