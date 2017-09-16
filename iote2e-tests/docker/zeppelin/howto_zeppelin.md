Zeppelin http://zeppelin.apache.org/docs/snapshot/install/install.html
review the dockerfile
cd to folder with dockerfile: cd /home/pete/development/gitrepo/iote2e/iote2e-tests/docker/zeppelin
docker build -t zeppelin:0.7.0 .

First run, make sure no errors
docker-compose up
ctrl-c to stop the container, can now use: docker-compose start/stop

Configure MySQL configuration
Note: if 'docker-compose rm' is run, then these steps must be repeated 
Connect to Zeppelin: http://localhost:8081
Add MySQL interpreter
	In the top right corner, click on the down arrow next to "anonymous" and select Interpreter
	Entries:
		Interpreter Name: iote2e-mysql
		Interpreter Group: jdbc
		default.driver: com.mysql.jdbc.Driver
		defaul.url: jdbc:mysql://iote2e-mysql-master:3306/db_iote2e_batch
		default.user: iote2e_batch
		default.password: Password*8
	Press Save
Create a Notebook: 
	Click on Notebook dropdown, select Create new note
	Note Name: iote2e Demo
	Default interpreter: iote2e-mysql
	Enter: select * from temperature
	Press: the "READY" arrow on the right
	Verify success, if not then research the error
	Enter queries against other tables, do a SHOW TABLES to list the available tables as these will change over time.


