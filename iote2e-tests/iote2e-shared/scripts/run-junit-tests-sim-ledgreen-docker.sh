#!/bin/bash
export MASTER_CONFIG_JSON_KEY="master_spark_unit_test_docker_config"
export CASSANDRA_CONTACT_POINT="iote2e-cassandra1"
export CASSANDRA_KEYSPACE_NAME="iote2e"
java -cp ../jars/iote2e-tests-1.0.0.jar "com.pzybrick.iote2e.tests.sim.SimLedGreen"
