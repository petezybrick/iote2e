FROM cassandra:3.9
MAINTAINER Pete Zybrick <pzybrick@gmail.com>
# Thanks to GettyImages for the original base dockerfile

RUN apt-get update \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*
 
RUN tar -xzvf apache-cassandra-3.9-bin.tar.gz -C /usr/ \
 && export PATH=$PATH:/usr/apache-cassandra-3.9/bin
ENV PATH $PATH:/usr/apache-cassandra-3.9/bin

CMD ["cassandra", "-f", "-R"]