#!/bin/bash
# args: commonProperties testSpecificProperties uberJar testClassName
echo "before"
export
set -o allexport
source $1
source $2
set +o allexport
echo "after"
export

java -cp $3 org.junit.runner.JUnitCore $4
