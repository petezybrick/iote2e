** Setup
- open Eclipse on win7
- open mtputty on win7, connect
- align each camera
- restart each RPi
- open squirrel on win7
- open 2x Tableau db's on win7
- fill the pill dispensers
- start docker containers
- reset actuator states
- start speed/batch in spark 
- delete emails

Before demo
- reset actuator states
- truncate temperature table
- truncate pd table
- delete emails

** Defer Questions **


* Terminal for rpi-00x: Open terminal, create 3x tabs, ssh pete@rpi-00x in each session
* Terminal for launchers: Open terminal, create 4x tabs
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
	* Stop docker sessions
	cd /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/scripts
	./docker-env-stop.sh /home/pete/development/gitrepo/iote2e/iote2e-tests/docker

* Reset actuator state before running any tests
docker exec -it iote2e-demomgr1 /bin/bash
cd /tmp/iote2e-shared/scripts
./reset-pyclient-actuator-state.sh iote2e-cassandra1 iote2e all

* Clear the temperature table

* Open browser
Spark: http://localhost:8080
Zeppelin: http://localhost:8081
NRT Temperature: file:///home/pete/development/gitrepo/iote2e/iote2e-ws/webContent/rt-temperature.html


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
- Review the Temperature Rule
Truncate the temperature table from Zeppelin
Verify the query is there for rpi-001 - rpi-003
Launch each temperature run
python -m iote2epyclient.launch.clientlauncher 'ProcessTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'pzybrick1' 'rpi-001' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'

python -m iote2epyclient.launch.clientlauncher 'ProcessTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'jdoe2' 'rpi-002' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'

python -m iote2epyclient.launch.clientlauncher 'ProcessTempToFan' 'temp1' '/home/pete/development/gitrepo/iote2e/iote2e-schema/src/main/avro/' 'ws://hp-lt-ubuntu-1:8090/iote2e/' 'sjones3' 'rpi-003' '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf' 'temp1'

Query the temperature table, show graphs

** PillDispensers **
- Truncate the pill_dispenser table
- Show the Subjects table
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


**Open mHealth**
* Assume: iote2e-ws is already up and running
* Submit the Spark Batch and Speed jobs
* Open Tableau and connect RT
cd to local spark folder
cd /home/pete/development/server/spark-2.0.2-bin-hadoop2.7

* Batch layer - writes to db
./bin/spark-submit \
  --class com.pzybrick.iote2e.stream.spark.OmhSparkConsumer \
  --deploy-mode cluster \
  --master spark://localhost:6066 \
  --executor-memory 8G \
  --executor-cores 2 \
  --total-executor-cores 6 \
  /tmp/iote2e-shared/jars/iote2e-stream-1.0.0.jar \
  master_spark_run_docker_batch_config iote2e-cassandra1 iote2e

* Speed layer - runs rules
./bin/spark-submit \
  --class com.pzybrick.iote2e.stream.spark.OmhSparkConsumer \
  --deploy-mode cluster \
  --master spark://localhost:6066 \
  --executor-memory 8G \
  --executor-cores 2 \
  --total-executor-cores 8 \
  /tmp/iote2e-shared/jars/iote2e-stream-1.0.0.jar \
  master_spark_run_docker_speed_config iote2e-cassandra1 iote2e
  
* Optionally run single OMH simulator to verify end to end
docker exec -it iote2e-demomgr1 /bin/bash
cd /tmp/iote2e-shared
java -cp jars/iote2e-tests-1.0.0.jar com.pzybrick.iote2e.tests.omh.RunOmhSim "data/simOmhUsers.csv" 1 5 60 "ws://iote2e-ws1:8092/omh/" 

* SQL to query and truncate the tables
select * from blood_pressure order by insert_ts;
select * from blood_glucose order by insert_ts;
select * from body_temperature order by insert_ts;
select * from heart_rate order by insert_ts;
select * from hk_workout order by insert_ts;
select * from respiratory_rate order by insert_ts;


truncate blood_pressure;
truncate blood_glucose;
truncate body_temperature;
truncate heart_rate;
truncate hk_workout;
truncate respiratory_rate;

* Copy OMH simulator jar to RPi's
* Launch simulators

** Big Data Black Box**
* SQL to query and truncate the tables
select * from flight_status order by insert_ts;
select * from engine_status order by insert_ts,engine_number;

delete from engine_status;
delete from flight_status;

select * from engine_status eng, flight_status flight where eng.flight_status_uuid=flight.flight_status_uuid and flight.flight_number='LH411' and oil_pressure>=90


* Assume: iote2e-ws is already up and running
* Submit the Spark Batch and Speed jobs
cd to local spark folder
cd /home/pete/development/server/spark-2.0.2-bin-hadoop2.7

* Batch layer - writes to db
./bin/spark-submit \
  --class com.pzybrick.iote2e.stream.spark.BdbbSparkConsumer \
  --deploy-mode cluster \
  --master spark://localhost:6066 \
  --executor-memory 8G \
  --executor-cores 2 \
  --total-executor-cores 6 \
  /tmp/iote2e-shared/jars/iote2e-stream-1.0.0.jar \
  master_spark_run_docker_batch_config iote2e-cassandra1 iote2e

* Speed layer - runs rules
./bin/spark-submit \
  --class com.pzybrick.iote2e.stream.spark.BdbbSparkConsumer \
  --deploy-mode cluster \
  --master spark://localhost:6066 \
  --executor-memory 8G \
  --executor-cores 2 \
  --total-executor-cores 8 \
  /tmp/iote2e-shared/jars/iote2e-stream-1.0.0.jar \
  master_spark_run_docker_speed_config iote2e-cassandra1 iote2e
  
Bring up the dashboard
embedded url: http://hp-lt-ubuntu-1:8082/iote2e-web/rt-engine-op-embed.html
  
* Run single BDBB simulator to verify end to end
docker exec -it iote2e-demomgr1 /bin/bash
cd /tmp/iote2e-shared
java -cp jars/iote2e-tests-1.0.0.jar com.pzybrick.iote2e.tests.bdbb.RunBdbbSim master_spark_run_docker_speed_config iote2e-cassandra1 iote2e  "ws://iote2e-ws1:8093/bdbb/" "data/simCourseFlight3.json"

** Additional steps as necessary **
Reload the configuration tables in Cassandra, including rules and config files
docker exec -it iote2e-demomgr1 /bin/bash
cd /tmp/iote2e-shared
java -cp jars/iote2e-tests-1.0.0.jar com.pzybrick.iote2e.tests.common.ConfigInitialLoad config_initial_load

Dump Pill Images
docker exec -it iote2e-demomgr1 /bin/bash
java -cp /tmp/iote2e-shared/jars/iote2e-tests-1.0.0.jar com.pzybrick.iote2e.tests.pilldisp.DumpPillsDispensedImages master_spark_run_docker_speed_config iote2e-cassandra1 iote2e



