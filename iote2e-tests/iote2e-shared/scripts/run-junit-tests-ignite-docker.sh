#!/bin/bash
export MASTER_CONFIG_JSON_KEY="master_ignite_unit_test_docker_config"
./run-junit-tests-common.sh TestCommonDocker.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.ignite.TestIgniteHandlerTempToFan"

./run-junit-tests-common.sh TestCommonDocker.properties TestIgniteHandler.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.ignite.TestIgniteHandlerHumidityToMister"

./run-junit-tests-common.sh TestCommonDocker.properties TestIgniteHandler.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.ignite.TestIgniteHandlerLed"
