#!/bin/bash
./run-junit-tests-common.sh TestCommonLocalhost.properties TestLocalHandler.properties \
../target/iote2e-tests-1.0.0.jar \
"com.pzybrick.test.iote2e.ruleproc.local.TestLocalHandlerTempToFan com.pzybrick.test.iote2e.ruleproc.local.TestLocalHandlerHumidityToMister  com.pzybrick.test.iote2e.ruleproc.local.TestLocalHandlerLed"
