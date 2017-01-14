#!/bin/bash
./run-junit-tests-common.sh TestCommonLocalhost.properties TestLocalHandler.properties \
../target/iote2e-ruleproc-1.0.0-all-tests.jar \
"com.pzybrick.test.iote2e.ruleproc.local.TestLocalHandlerTempToFan com.pzybrick.test.iote2e.ruleproc.local.TestLocalHandlerHumidityToMister  com.pzybrick.test.iote2e.ruleproc.local.TestLocalHandlerLed"
