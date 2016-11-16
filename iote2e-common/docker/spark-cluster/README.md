download the tgz
cd to this folder
docker build -t spark:2.0.2 .

./bin/spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master spark://localhost:7077 \
  /home/pete/development/server/spark-2.0.2-bin-hadoop2.7/examples/jars/spark-examples_2.11-2.0.2.jar \
  1000




