
java -cp iote2e-ruleproc-1.0.0.jar com.pzybrick.iote2e.ruleproc.kafkademo.KafkaStringDemo 15 "iote2e-zoo2:2181" "iote2e-group-sandbox" "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox" "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"

java -cp iote2e-ruleproc-1.0.0.jar com.pzybrick.iote2e.ruleproc.kafkademo.KafkaAvroDemo 10 "iote2e-zoo2:2181" "iote2e-group-sandbox" "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox" "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"

