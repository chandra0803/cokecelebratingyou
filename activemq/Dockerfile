# the HC apps all together in one big pile
FROM ubuntu:14.04

MAINTAINER Adam Dehnel <adam.dehnel@biworldwide.com>

ENV ACTIVEMQ_VERSION=5.13.0
ENV ACTIVEMQ_BASE=/opt/apache-activemq-$ACTIVEMQ_VERSION
ENV JAVA_HOME=/usr/lib/jvm/java-8-oracle


# Set locales
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LC_CTYPE en_US.UTF-8

# Fix sh (makes sh use bash interpreter)
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

# Install dependencies
RUN apt-get update && \
apt-get install -y git build-essential curl wget software-properties-common

# Install JDK 8
RUN \
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
add-apt-repository -y ppa:webupd8team/java && \
apt-get update && \
apt-get install -y oracle-java8-installer wget unzip tar && \
rm -rf /var/lib/apt/lists/* && \
rm -rf /var/cache/oracle-jdk8-installer

# get and install ActiveMQ
RUN wget --quiet --no-cookies https://archive.apache.org/dist/activemq/$ACTIVEMQ_VERSION/apache-activemq-$ACTIVEMQ_VERSION-bin.tar.gz

RUN tar xzvf apache-activemq-$ACTIVEMQ_VERSION-bin.tar.gz -C /opt

# uncommenting these lines causes environment variables to be available to activemq
RUN sed -i "s@<Installationdir>/@${ACTIVEMQ_BASE}@" $ACTIVEMQ_BASE/bin/env
RUN sed -i 's/^# ACTIVEMQ_HOME=/ACTIVEMQ_HOME=/' $ACTIVEMQ_BASE/bin/env
RUN sed -i 's/^# ACTIVEMQ_BASE=/ACTIVEMQ_BASE=/' $ACTIVEMQ_BASE/bin/env
RUN sed -i 's/^# ACTIVEMQ_CONF=/ACTIVEMQ_CONF=/' $ACTIVEMQ_BASE/bin/env
RUN sed -i 's/^# ACTIVEMQ_DATA=/ACTIVEMQ_DATA=/' $ACTIVEMQ_BASE/bin/env
RUN sed -i 's/^# ACTIVEMQ_TMP=/ACTIVEMQ_TMP=/' $ACTIVEMQ_BASE/bin/env

EXPOSE 61616

# Specifying "console" causes activemq to stay up and running.  Specifying
# "start" causes activemq to run as a daemon, which causes the service
# (container) to shutdown as soon as the command is executed.
CMD ["/opt/apache-activemq-5.13.0/bin/activemq", "console"]
