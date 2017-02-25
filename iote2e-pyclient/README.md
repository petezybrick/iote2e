#IoT End to End - Python Client

##Installation
* Open your IDE - for this example, PyDev 
* Download the this project from github into your IDE
* Run setup.py with the parms: sdist --formats-gztar
* Verify build succeeded: Gzip should be created: ./dist/awsext-1.1.tar.gz
* Copy iote2epyclient-1.0.0.tar.gz to target location (i.e. SCP to an RPi or EC2 instance)
* Login to target system and `cd` to target directory
* Execute the following commands:
sudo rm -rf iote2epyclient-1.0.0
tar -xvzf iote2epyclient-1.0.0.tar.gz
cd iote2epyclient-1.0.0
sudo python setup.py install
cd ..
sudo rm -rf iote2epyclient-1.0.0
* Verify successful installation
		* Start Python interactive and enter:
				* import iote2epyclient 
				* print iote2epyclient.Version
		* Should display "1.0.0" - if not, then research the error
		
##Running Simulators
###Temp to Fan
python -m iote2epyclient.launch.clientlauncher 'ProcessSimTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi_999' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'

###Humidity to Mister
python -m iote2epyclient.launch.clientlauncher 'ProcessSimHumidityToMister' 'humidity1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi_999' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'humidity1'

###LedGreen
python -m iote2epyclient.launch.clientlauncher 'ProcessSimTempToFan' 'switch0' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi_999' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'switch0'
