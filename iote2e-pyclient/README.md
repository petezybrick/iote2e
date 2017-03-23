#IoT End to End - Python Client

##Installation
* Open your IDE - for this example, PyDev 
* Download the this project from github into your IDE
* Run setup.py with the parms: sdist --formats-gztar
* Verify build succeeded: Gzip should be created: ./dist/awsext-1.1.tar.gz
* Copy iote2epyclient-1.0.0.tar.gz to target location (i.e. SCP to an RPi or EC2 instance)
	scp /home/pete/development/gitrepo/iote2e/iote2e-pyclient/dist/iote2epyclient-1.0.0.tar.gz pete@192.168.1.6:/home/pete/iote2epyclient-1.0.0.tar.gz
	scp /home/pete/development/gitrepo/iote2e/iote2e-pyclient/dist/iote2epyclient-1.0.0.tar.gz pete@192.168.1.3:iote2epyclient-1.0.0.tar.gz
* Login to target system and `cd` to target directory
* Execute the following commands:
rm -rf iote2epyclient-1.0.0
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
		* Enter `exit()` to exit the python interpreter
		
##Running Simulators
###Prereqs###
RPi
Create folders on target system
	mkdir iote2epyclient
	mkdir iote2epyclient/avro-schemas
	mkdir iote2epyclient/log-configs
scp -r /home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/ pete@192.168.1.5:/home/pete/iote2epyclient/avro-schemas/
scp /home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf pete@192.168.1.5:/home/pete/iote2epyclient/log-configs/client_consoleonly.conf

###Temp to Fan
**Running on python under Docker, i.e. on iote2e-demomgr**
python -m iote2epyclient.launch.clientlauncher 'ProcessSimTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi-002' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'

**Running on RPi**
python -m iote2epyclient.launch.clientlauncher 'ProcessSimTempToFan' 'temp1' '/home/pete/iote2epyclient/avro-schemas/avro/' 'ws://192.168.1.7:8090/iote2e/' 'pzybrick1' 'rpi-002' '/home/pete/iote2epyclient/log-configs/client_consoleonly.conf' 'temp1'

python -m iote2epyclient.launch.clientlauncher 'ProcessTempToFan' 'temp1' '/home/pete/iote2epyclient/avro-schemas/avro/' 'ws://192.168.1.7:8090/iote2e/' 'pzybrick1' 'rpi-002' '/home/pete/iote2epyclient/log-configs/client_consoleonly.conf' 'temp1'

###Humidity to Mister
python -m iote2epyclient.launch.clientlauncher 'ProcessSimHumidityToMister' 'humidity1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://192.168.1.7:8090/iote2e/' 'pzybrick1' 'rpi_001' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'humidity1'

python -m iote2epyclient.launch.clientlauncher 'ProcessSimHumidityToMister' 'humidity1' '/home/pete/iote2epyclient/avro-schemas/avro/' 'ws://192.168.1.7:8090/iote2e/' 'pzybrick1' 'rpi_001' '//home/ubuntu/iote2epyclient/log-configs/client_consoleonly.conf' 'humidity1'

###LedGreen
python -m iote2epyclient.launch.clientlauncher 'ProcessSimTempToFan' 'switch0' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi-999' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'switch0'

python -m iote2epyclient.launch.clientlauncher 'ProcessLedGreen' 'switch0' '/home/pete/iote2epyclient/avro-schemas/avro/' 'ws://192.168.1.7:8090/iote2e/' 'pzybrick1' 'rpi-002' '/home/pete/iote2epyclient/log-configs/client_consoleonly.conf' 'switch0'

##Installation RPi
Install Ubuntu Mate 
	Download Ubuntu Mate: https://ubuntu-mate.org/raspberry-pi/
	Copy to microSDHC - I used Etcher on MacBook, worked great
	Plug the microSDHC into the RPi and power up
	Follow the prompts
	**CRITICAL** name each RPi distinctly, use rpi-001, rpi-002 to start, since this matches the SourceName's in rule_login_source_sensor.json
Start Ubuntu Mate
	Login for the first time
	Open terminal session
		sudo apt-get update
		sudo apt-get --purge autoremove
		sudo ufw enable
		sudo ufw allow 22
		sudo systemctl enable ssh.socket
		sudo systemctl restart ssh
	SCP the RPi initialization script to the RPi instance
		Enter: scp /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/scripts/rpi-init.sh pete@192.168.1.5:rpi-init.sh
	SSH into the RPi and run the init script
		ssh pete@192.168.1.5
		sudo ./rpi-init.sh
	Verify Avro installed
		python
			import avro -> should not throw exception
			exit()
	Optionally install gedit
		sudo apt install gedit
	Install git
		sudo apt install git
		Create a local repo, i.e. /home/pete/development/gitrepo
		cd to that local repo, i.e. cd /home/pete/development/gitrepo
		git init
		git config --global push.default simple
		git clone https://github.com/petezybrick/iote2e.git
		git pull https://github.com/petezybrick/iote2e.git
		git branch develop >>> really need this?
		git checkout develop
		git pull https://github.com/petezybrick/iote2e.git
	Install HAT Python support
		sudo apt-get update
		sudo apt-get upgrade
		sudo apt-get install sense-hat
		Reboot the RPi
		API Reference: https://pythonhosted.org/sense-hat/api/

sudo shutdown -P now

