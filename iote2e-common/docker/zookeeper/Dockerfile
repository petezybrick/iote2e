FROM ubuntu:14.04.5
MAINTAINER Pete Zybrick pzybrick@gmail.com

RUN apt-get update \
	&& apt-get -yqq install ssh \
	&& apt-get install -y curl unzip \
	    python3 python3-setuptools \
	&& ln -sf /usr/bin/python3 /usr/bin/python \
	&& easy_install3 pip py4j \
	&& apt-get clean \
	&& rm -rf /var/lib/apt/lists/*     

# java
ARG JAVA_MAJOR_VERSION=8
ARG JAVA_UPDATE_VERSION=112
ARG JAVA_BUILD_NUMBER=15
ENV JAVA_HOME /usr/jdk1.${JAVA_MAJOR_VERSION}.0_${JAVA_UPDATE_VERSION}

ENV PATH $PATH:$JAVA_HOME/bin
RUN curl -sL --retry 3 --insecure \
  --header "Cookie: oraclelicense=accept-securebackup-cookie;" \
  "http://download.oracle.com/otn-pub/java/jdk/${JAVA_MAJOR_VERSION}u${JAVA_UPDATE_VERSION}-b${JAVA_BUILD_NUMBER}/server-jre-${JAVA_MAJOR_VERSION}u${JAVA_UPDATE_VERSION}-linux-x64.tar.gz" \
  | gunzip \
  | tar x -C /usr/ \
  && ln -s $JAVA_HOME /usr/java \
  && rm -rf $JAVA_HOME/man
	
# zookeeper
RUN wget -q -O - http://mirror.metrocast.net/apache/zookeeper/zookeeper-3.4.8/zookeeper-3.4.8.tar.gz | tar -xzf - -C /opt \
    && mv /opt/zookeeper-3.4.8 /opt/zookeeper 
#    && rm /opt/zookeeper.tar.gz
	
COPY zoo.cfg /opt/zookeeper/conf/zoo.cfg
COPY zkMyIdWrapper.sh /opt/zookeeper/bin/zkMyIdWrapper.sh
RUN chmod +x /opt/zookeeper/bin/zkMyIdWrapper.sh

EXPOSE 2181 2888 3888
VOLUME ["/tmp/iote2d"]
WORKDIR /opt/zookeeper

RUN mkdir /opt/zookeeper/logs
ENV ZOO_LOG_DIR /opt/zookeeper/logs
ENV ZOO_LOG4J_PROP INFO,CONSOLE,ROLLINGFILE
ENV MYID_SERVER_ID 1

# ENTRYPOINT ["/opt/zookeeper/bin/zkServer.sh"]
ENTRYPOINT ["/opt/zookeeper/bin/zkMyIdWrapper.sh"]
CMD ["start-foreground"]