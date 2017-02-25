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
				* print iote2epyclient.version
		* Should display "1.0.0" - if not, then research the error
		
##Running Simulators
###Temp to Fan
python -m iote2epyclient.launch.clientlauncher 'ProcessSimTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi_999' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'


scp -r /home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/ ubuntu@192.168.1.3:/home/ubuntu/iote2epyclient/avro-schemas/
scp /home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf ubuntu@192.168.1.3:/home/ubuntu/iote2epyclient/log-configs/client_consoleonly.conf

python -m iote2epyclient.launch.clientlauncher 'ProcessSimTempToFan' 'temp1' '/home/ubuntu/iote2epyclient/avro-schemas/avro/' 'ws://192.168.1.7:8090/iote2e/' 'pzybrick1' 'rpi_999' '/home/ubuntu/iote2epyclient/log-configs/client_consoleonly.conf' 'temp1'

###Humidity to Mister
python -m iote2epyclient.launch.clientlauncher 'ProcessSimHumidityToMister' 'humidity1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi_999' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'humidity1'

python -m iote2epyclient.launch.clientlauncher 'ProcessSimHumidityToMister' 'humidity1' '/home/ubuntu/iote2epyclient/avro-schemas/avro/' 'ws://192.168.1.7:8090/iote2e/' 'pzybrick1' 'rpi_999' '//home/ubuntu/iote2epyclient/log-configs/client_consoleonly.conf' 'humidity1'

###LedGreen
python -m iote2epyclient.launch.clientlauncher 'ProcessSimTempToFan' 'switch0' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi_999' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'switch0'

##Installation RPi
install Python
	sudo apt-get update
	sudo apt-get python-minimal
	sudo apt install python-pip
	sudo pip install --upgrade pip
	dup pip install enum
Install Avro for Python
	Note that the steps below assume Release 1.8.1, if you are using a newer/older version then adjust accordingly
	Review the steps on the Avro Python installation page, i.e. https://avro.apache.org/docs/1.8.1/gettingstartedpython.html
	mkdir ~/avro-1.8.1
	cd avro-1.8.1
	Download the Avro install tgz.  Ensure it is the same version as the dependency in the pom.xml in the iote2e-schema project, currently 1.8.8
		A download mirror can be found on the Avro downloads page, update the below URL with the mirror's name and execute the wget
		wget http://apache.spinellicreations.com/avro/avro-1.8.1/py/avro-1.8.1.tar.gz
	tar xvf avro-1.8.1.tar.gz
	cd avro-1.8.1
	sudo python setup.py install
	python
		import avro -> should not throw exception
		exit()
Install Python Websocket client support
	pip install websocket-client