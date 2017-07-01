** Align the cameras
- align each camera
- restart each RPi


* Terminal for rpi-00x: Open terminal, create 3x tabs, ssh pete@rpi-00x in each session
* Terminal for launchers: Open termina, create 4x tabs
* Rebuild pyclient
* SSH pyclient into each target system
scp /home/pete/development/gitrepo/iote2e/iote2e-pyclient/dist/iote2epyclient-1.0.0.tar.gz pete@rpi-001:iote2epyclient-1.0.0.tar.gz
scp /home/pete/development/gitrepo/iote2e/iote2e-pyclient/dist/iote2epyclient-1.0.0.tar.gz pete@rpi-002:iote2epyclient-1.0.0.tar.gz
scp /home/pete/development/gitrepo/iote2e/iote2e-pyclient/dist/iote2epyclient-1.0.0.tar.gz pete@rpi-003:iote2epyclient-1.0.0.tar.gz
* Run install script in each rpi-00x session: 
sudo ~/development/gitrepo/iote2e/iote2e-pyclient/scripts/install-pyclient.sh

* Start docker sessions
open first launcher terminal tab
cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/scripts
./docker-env-start.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker

* Reset actuator state before running any tests
docker exec -it iote2e-demomgr1 /bin/bash
cd /tmp/iote2e-shared/scripts
./reset-pyclient-actuator-state.sh iote2e-cassandra1 iote2e all

* Open browser
Spark: http://localhost:8080
Zeppelin: http://localhost:8081

* Submit Spark jobs for temperature and pill dispenser
cd to local spark folder
cd /home/pete/development/server/spark-2.0.2-bin-hadoop2.7

* Batch layer - writes to db
./bin/spark-submit \
  --class com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer \
  --deploy-mode cluster \
  --master spark://localhost:6066 \
  --executor-memory 8G \
  --executor-cores 2 \
  --total-executor-cores 6 \
  /tmp/iote2e-shared/jars/iote2e-stream-1.0.0.jar \
  master_spark_run_docker_batch_config iote2e-cassandra1 iote2e

* Speed layer - runs rules
./bin/spark-submit \
  --class com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer \
  --deploy-mode cluster \
  --master spark://localhost:6066 \
  --executor-memory 8G \
  --executor-cores 2 \
  --total-executor-cores 8 \
  /tmp/iote2e-shared/jars/iote2e-stream-1.0.0.jar \
  master_spark_run_docker_speed_config iote2e-cassandra1 iote2e
  
* Refresh Spark browser session, ensure up and running

* Temperature Runs
Truncate the temperature table from Zeppelin
Verify the query is there for rpi-001 - rpi-003
Launch each temperature run
python -m iote2epyclient.launch.clientlauncher 'ProcessTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi-001' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'

python -m iote2epyclient.launch.clientlauncher 'ProcessTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi-002' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'

python -m iote2epyclient.launch.clientlauncher 'ProcessTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi-003' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'

Query the temperature table, show graphs

** PillDispensers **
- Truncate the pill_dispenser table
- Populate pill_dispenser table
insert into pills_dispensed (pills_dispensed_uuid,login_name,source_name,actuator_name,dispense_state, state_pending_ts, num_to_dispense)
	select uuid() as pills_dispensed_uuid, subj.login_name, subj.source_name, 'pilldisp1' as actuator_name, 'PENDING' as dispense_state, now() as state_pending_ts, 
		case when avg(bp.diastolic) < 80 THEN 1 
			when avg(bp.diastolic) < 90 THEN 2 
			else 3 
		end as num_to_dispense
	from subject as subj, bp_series as bp 
	where bp.login_name=subj.login_name 
	group by subj.login_name;
	
query the table

- Start the Python PillDispenser on each RPi  
python -m iote2epyclient.launch.clientlauncher 'ProcessPillDispenser' 'pilldisp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi-001' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' ''

python -m iote2epyclient.launch.clientlauncher 'ProcessPillDispenser' 'pilldisp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'jdoe2' 'rpi-002' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' ''

python -m iote2epyclient.launch.clientlauncher 'ProcessPillDispenser' 'pilldisp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'sjones3' 'rpi-003' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' ''

- Bring up demomgr: docker exec -it iote2e-demomgr1 /bin/bash 
- Start the PillDispenser - this will read the pill_dispenser table and start the sequence
java -cp /tmp/iote2e-shared/jars/iote2e-tests-1.0.0.jar -Xms512m -Xmx512m com.pzybrick.iote2e.stream.pilldisp.PillDispenser master_spark_run_docker_batch_config iote2e-cassandra1 iote2e
	
- Stop and restart the python code on each rpi-00x
- run updated SQL
insert into pills_dispensed (pills_dispensed_uuid,login_name,source_name,actuator_name,dispense_state, state_pending_ts, num_to_dispense)
	select uuid() as pills_dispensed_uuid, subj.login_name, subj.source_name, 'pilldisp1' as actuator_name, 'PENDING' as dispense_state, now() as state_pending_ts,
		case when avg(bp.diastolic) < 80 THEN 1 
			when avg(bp.diastolic) < 90 AND subj.age > 50 THEN 3 
			when avg(bp.diastolic) < 90 THEN 2 
			else 3 
		end as num_to_dispense
	from subject as subj, bp_series as bp 
	where bp.login_name=subj.login_name 
	group by subj.login_name;


** Additional steps as necessary **
Reload the configuration tables in Cassandra, including rules and config files
cd /tmp/iote2e-shared
java -cp jars/iote2e-tests-1.0.0.jar com.pzybrick.iote2e.tests.common.ConfigInitialLoad config_initial_load

