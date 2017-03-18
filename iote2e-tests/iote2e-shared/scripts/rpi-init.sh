#!/bin/bash
pip install --upgrade pip
pip install enum
pip install websocket-client
Install Avro for Python
wget http://apache.spinellicreations.com/avro/avro-1.8.1/py/avro-1.8.1.tar.gz
tar xvf avro-1.8.1.tar.gz
cd avro-1.8.1
python setup.py install
rm -f avro-1.8.1.tar.gz