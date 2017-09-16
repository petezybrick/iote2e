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
	
# Ignite
ENV IGNITE_VERSION 1.8.0
RUN mkdir /opt/ignite && \
	wget -q http://apache.osuosl.org//ignite/"$IGNITE_VERSION"/apache-ignite-fabric-"$IGNITE_VERSION"-bin.zip -O /tmp/apache-ignite-fabric-"$IGNITE_VERSION"-bin.zip && \
    unzip /tmp/apache-ignite-fabric-"$IGNITE_VERSION"-bin.zip -d /opt/ignite/ && \
    rm /tmp/apache-ignite-fabric-"$IGNITE_VERSION"-bin.zip

COPY iote2e-ignite.xml /opt/ignite/apache-ignite-fabric-"$IGNITE_VERSION"-bin/config/iote2e-ignite.xml
ENV IGNITE_HOME=/opt/ignite/apache-ignite-fabric-"$IGNITE_VERSION"-bin
ENV IGNITE_VERSION="$IGNITE_VERSION"
ENV DEFAULT_CONFIG=config/iote2e-ignite.xml
ENV JVM_OPTS="-Xms1g -Xmx2g -server -XX:+AggressiveOpts -XX:MaxMetaspaceSize=256m"
ENTRYPOINT ["/bin/sh"]
CMD ["-c", "$IGNITE_HOME/bin/ignite.sh"]
