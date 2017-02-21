#!/bin/bash
export MASTER_CONFIG_JSON_KEY="master_spark_unit_test_docker_config"
set -o allexport
source TestCommonDocker.properties
set +o allexport
java -cp ../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.simws.ResetPyClientActuatorState" "$1"

