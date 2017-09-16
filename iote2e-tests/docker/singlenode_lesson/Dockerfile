FROM ubuntu:14.04.5

RUN apt-get update && \
	apt-get install -y openjdk-7-jre-headless  && \
	mkdir /home/pzuser && \
	groupadd -r pzuser && \
	useradd -g pzuser -d /home/pzuser -r pzuser && \ 
	chown -R pzuser:pzuser /home/pzuser && \
	apt-get install -y curl

USER pzuser
WORKDIR /home/pzuser
CMD /bin/bash