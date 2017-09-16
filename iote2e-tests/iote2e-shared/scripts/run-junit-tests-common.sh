#!/bin/bash
# args: commonProperties testSpecificProperties uberJar testClassName
set -o allexport
source $1
# source $2
set +o allexport

java -cp $2 org.junit.runner.JUnitCore $3
