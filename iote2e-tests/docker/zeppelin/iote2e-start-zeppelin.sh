#!/bin/bash
eval /usr/share/zeppelin-0.7.0/bin/zeppelin-daemon.sh start
# hack to keep the container from exiting
tail -F -n0 /etc/hosts
