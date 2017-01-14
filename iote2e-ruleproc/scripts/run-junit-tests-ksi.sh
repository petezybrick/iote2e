#!/bin/bash
./run-junit-tests-common.sh TestCommonLocalhost.properties TestKsiHandler.properties \
../target/iote2e-ruleproc-1.0.0-all-tests.jar \
"com.pzybrick.test.iote2e.ruleproc.ksi.TestKsiHandlerTempToFan com.pzybrick.test.iote2e.ruleproc.ksi.TestKsiHandlerHumidityToMister  com.pzybrick.test.iote2e.ruleproc.ksi.TestKsiHandlerLed"
