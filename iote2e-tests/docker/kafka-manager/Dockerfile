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
	
# Kafka Manager
RUN mkdir /opt/kafka-manager 
COPY kafka-manager-1.3.2.1.zip /tmp/kafka-manager.zip
RUN unzip /tmp/kafka-manager.zip -d /opt/kafka-manager/ && \
    rm /tmp/kafka-manager.zip

EXPOSE 9000
ENV ZK_HOSTS="iote2e-zoo1:2181,iote2e-zoo2:2181,iote2e-zoo3:2181"

CMD ["bin/kafka-manager"]