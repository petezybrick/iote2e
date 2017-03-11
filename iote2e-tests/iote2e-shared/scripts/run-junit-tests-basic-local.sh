#!/bin/bash
export MASTER_CONFIG_JSON_KEY="master_basic_unit_test_local_config"
./run-junit-tests-common.sh TestCommonLocal.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.local.TestLocalHandlerTempToFan com.pzybrick.iote2e.tests.local.TestLocalHandlerHumidityToMister  com.pzybrick.iote2e.tests.TestLocalHandlerLed"
