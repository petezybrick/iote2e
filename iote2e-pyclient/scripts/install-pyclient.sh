#!/bin/bash
rm -rf iote2epyclient-1.0.0
tar -xvzf iote2epyclient-1.0.0.tar.gz
cd iote2epyclient-1.0.0
sudo python setup.py install
cd ..
sudo rm -rf iote2epyclient-1.0.0
