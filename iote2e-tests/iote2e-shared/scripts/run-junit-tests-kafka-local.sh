#!/bin/bash
./run-junit-tests-common.sh TestCommonLocalhost.properties TestKafkaHandler.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.kafka.TestKafkaHandlerTempToFan"

./run-junit-tests-common.sh TestCommonLocalhost.properties TestKafkaHandler.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.kafka.TestKafkaHandlerHumidityToMister"

./run-junit-tests-common.sh TestCommonLocalhost.properties TestKafkaHandler.properties \
../jars/iote2e-tests-1.0.0.jar \
"com.pzybrick.iote2e.tests.kafka.TestKafkaHandlerLed"
