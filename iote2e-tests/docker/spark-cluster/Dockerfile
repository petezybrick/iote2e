FROM ubuntu:14.04.5
MAINTAINER Pete Zybrick <pzybrick@gmail.com>
# Thanks to GettyImages for the original base dockerfile

RUN apt-get update \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

#RUN apt-get update \
# && apt-get install -y locales \
# && dpkg-reconfigure -f noninteractive locales \
# && locale-gen C.UTF-8 \
# && /usr/sbin/update-locale LANG=C.UTF-8 \
# && echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen \
# && locale-gen \
# && apt-get clean \
# && rm -rf /var/lib/apt/lists/*


# Users with other locales should set this in their derivative image
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

RUN apt-get update \
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

copy spark-2.0.2-bin-hadoop2.7.tgz spark-2.0.2-bin-hadoop2.7.tgz

# SPARK
ENV SPARK_VERSION 2.0.2
ENV SPARK_PACKAGE spark-${SPARK_VERSION}-bin-hadoop2.7
ENV SPARK_HOME /usr/spark-${SPARK_VERSION}
ENV PATH $PATH:${SPARK_HOME}/bin
RUN tar -xzvf ${SPARK_PACKAGE}.tgz -C /usr/ 

RUN mv /usr/$SPARK_PACKAGE $SPARK_HOME \
   && chown -R root:root $SPARK_HOME
   

WORKDIR $SPARK_HOME
CMD ["bin/spark-class", "org.apache.spark.deploy.master.Master"]