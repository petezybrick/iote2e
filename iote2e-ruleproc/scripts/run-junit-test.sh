#!/bin/bash
echo "before"
export
set -o allexport
source $1
set +o allexport
echo "after"
export

java -cp target/iote2e-ruleproc-1.0.0-all-tests.jar org.junit.runner.JUnitCore $2
