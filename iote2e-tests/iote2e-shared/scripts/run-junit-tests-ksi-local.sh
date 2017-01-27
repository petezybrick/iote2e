#!/bin/bash
./run-junit-tests-common.sh TestCommonLocalhost.properties TestKsiHandler.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.ksi.TestKsiHandlerTempToFan"

./run-junit-tests-common.sh TestCommonLocalhost.properties TestKsiHandler.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.ksi.TestKsiHandlerHumidityToMister"

./run-junit-tests-common.sh TestCommonLocalhost.properties TestKsiHandler.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.ksi.TestKsiHandlerLed"
