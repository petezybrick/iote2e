#!/bin/bash
export MASTER_CONFIG_JSON_KEY="master_kafka_unit_test_docker_config"
./run-junit-tests-common.sh TestCommonDocker.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.kafka.TestKafkaHandlerTempToFan"

./run-junit-tests-common.sh TestCommonDocker.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.kafka.TestKafkaHandlerHumidityToMister"

./run-junit-tests-common.sh TestCommonDocker.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.kafka.TestKafkaHandlerLed"
