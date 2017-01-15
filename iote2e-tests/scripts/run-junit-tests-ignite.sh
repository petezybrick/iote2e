#!/bin/bash
./run-junit-tests-common.sh TestCommonLocalhost.properties TestIgniteHandler.properties \
../target/iote2e-tests-1.0.0.jar \
"com.pzybrick.test.iote2e.ruleproc.ignite.TestIgniteHandlerTempToFan"

./run-junit-tests-common.sh TestCommonLocalhost.properties TestIgniteHandler.properties \
../target/iote2e-tests-1.0.0.jar \
"com.pzybrick.test.iote2e.ruleproc.ignite.TestIgniteHandlerHumidityToMister"

./run-junit-tests-common.sh TestCommonLocalhost.properties TestIgniteHandler.properties \
../target/iote2e-tests-1.0.0.jar \
"com.pzybrick.test.iote2e.ruleproc.ignite.TestIgniteHandlerLed"
