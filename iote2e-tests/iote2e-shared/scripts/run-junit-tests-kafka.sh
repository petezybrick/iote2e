#!/bin/bash
./run-junit-tests-common.sh TestCommonLocalhost.properties TestKafkaHandler.properties \
../target/iote2e-tests-1.0.0.jar \
"com.pzybrick.test.iote2e.ruleproc.kafka.TestKafkaHandlerTempToFan"

./run-junit-tests-common.sh TestCommonLocalhost.properties TestKafkaHandler.properties \
../target/iote2e-tests-1.0.0.jar \
"com.pzybrick.test.iote2e.ruleproc.kafka.TestKafkaHandlerHumidityToMister"

./run-junit-tests-common.sh TestCommonLocalhost.properties TestKafkaHandler.properties \
../target/iote2e-tests-1.0.0.jar \
"com.pzybrick.test.iote2e.ruleproc.kafka.TestKafkaHandlerLed"
