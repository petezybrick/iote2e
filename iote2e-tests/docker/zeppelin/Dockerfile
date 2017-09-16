FROM ubuntu:14.04.5
MAINTAINER Pete Zybrick <pzybrick@gmail.com>
RUN apt-get update \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

# Users with other locales should set this in their derivative image
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
#ENV LC_ALL en_US.UTF-8

RUN apt-get update \
 && apt-get -yq install nano \
 && apt-get -yqq install ssh \
 && apt-get install -y curl unzip \
    python3 python3-setuptools \
 && ln -sf /usr/bin/python3 /usr/bin/python \
 && easy_install3 pip py4j \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*     

# http://blog.stuart.axelbrooke.com/python-3-on-spark-return-of-the-pythonhashseed
ENV PYTHONHASHSEED 0
ENV PYTHONIOENCODING UTF-8
ENV PIP_DISABLE_PIP_VERSION_CHECK 1

# JAVA
ARG JAVA_MAJOR_VERSION=8
ARG JAVA_UPDATE_VERSION=92
ARG JAVA_BUILD_NUMBER=14
ENV JAVA_HOME /usr/jdk1.${JAVA_MAJOR_VERSION}.0_${JAVA_UPDATE_VERSION}

ENV PATH $PATH:$JAVA_HOME/bin
RUN curl -sL --retry 3 --insecure \
  --header "Cookie: oraclelicense=accept-securebackup-cookie;" \
  "http://download.oracle.com/otn-pub/java/jdk/${JAVA_MAJOR_VERSION}u${JAVA_UPDATE_VERSION}-b${JAVA_BUILD_NUMBER}/server-jre-${JAVA_MAJOR_VERSION}u${JAVA_UPDATE_VERSION}-linux-x64.tar.gz" \
  | gunzip \
  | tar x -C /usr/ \
  && ln -s $JAVA_HOME /usr/java \
  && rm -rf $JAVA_HOME/man

# Zeppelin
ENV ZEPPELIN_VERSION 0.7.0
copy zeppelin-${ZEPPELIN_VERSION}-bin-all.tgz zeppelin-${ZEPPELIN_VERSION}-bin-all.tgz
RUN tar -xzvf zeppelin-${ZEPPELIN_VERSION}-bin-all.tgz -C /usr/share 

RUN mv /usr/share/zeppelin-${ZEPPELIN_VERSION}-bin-all /usr/share/zeppelin-${ZEPPELIN_VERSION} \
   && chown -R root:root /usr/share/zeppelin-${ZEPPELIN_VERSION}
COPY zeppelin-site.xml  /usr/share/zeppelin-${ZEPPELIN_VERSION}/conf/zeppelin-site.xml
COPY iote2e-start-zeppelin.sh  /usr/share/zeppelin-${ZEPPELIN_VERSION}/bin/iote2e-start-zeppelin.sh
RUN chmod +x /usr/share/zeppelin-${ZEPPELIN_VERSION}/bin/iote2e-start-zeppelin.sh
COPY mysql-connector-java-5.1.41-bin.jar  /usr/share/zeppelin-${ZEPPELIN_VERSION}/interpreter/jdbc/mysql-connector-java-5.1.41-bin.jar

ENV TERM xterm

CMD ["/usr/share/zeppelin-${ZEPPELIN_VERSION}/bin/iote2e-start-zeppelin.sh"]