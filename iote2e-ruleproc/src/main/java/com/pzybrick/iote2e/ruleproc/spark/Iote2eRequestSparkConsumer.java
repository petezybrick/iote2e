package com.pzybrick.iote2e.ruleproc.spark;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import consumer.kafka.MessageAndMetadata;
import consumer.kafka.ReceiverLauncher;

public class Iote2eRequestSparkConsumer {
    private static final Log log = LogFactory.getLog(Iote2eRequestSparkConsumer.class);
	
	
    public static void main(String[] args) {
    	Iote2eRequestSparkConsumer iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
    	iote2eRequestSparkConsumer.process(args);
    }
    	
    public void process(String[] args) {
		String zooKeeper = args[0];
		String groupId = args[1];
		String topic = args[2];
		int numThreads = Integer.parseInt(args[3]);

        SparkConf conf = new SparkConf()
                .setAppName("kafka-sandbox")
                .setMaster("local[*]");
        //JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(250));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(numThreads));
        Properties kafkaProps = new Properties();
        
        //kafkaProps.put("zookeeper.connect", zooKeeper);
        kafkaProps.put("group.id", "iote2e-group-sandbox");
        // Spark Kafka Consumer https://github.com/dibbhatt/kafka-spark-consumer
        
        kafkaProps.put("zookeeper.hosts", "hp-lt-ubuntu-1");
        kafkaProps.put("zookeeper.port", "2181");
        kafkaProps.put("zookeeper.broker.path", "/brokers");
        kafkaProps.put("kafka.topic", "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox");
        kafkaProps.put("kafka.consumer.id", "test-id");
        kafkaProps.put("zookeeper.consumer.connection", "hp-lt-ubuntu-1");
        kafkaProps.put("zookeeper.consumer.path", "/iote2erequest-sandbox");
        // consumer optional 
        kafkaProps.put("consumer.forcefromstart", "false");
        kafkaProps.put("consumer.fetchsizebytes", "1048576");
        kafkaProps.put("consumer.fillfreqms", "250");
        kafkaProps.put("consumer.backpressure.enabled", "true");
        
        kafkaProps.put("zookeeper.session.timeout.ms", "400");
        kafkaProps.put("zookeeper.sync.time.ms", "200");
        kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        
        Iote2eRequestSparkProcessor streamProcessor = new Iote2eRequestSparkProcessor();
        
        int numberOfReceivers = 3;

		JavaDStream<MessageAndMetadata> unionStreams = ReceiverLauncher.launch(
				ssc, kafkaProps, numberOfReceivers, StorageLevel.MEMORY_ONLY());
		
		unionStreams.foreachRDD(streamProcessor::processIote2eRequestRDD);
		try {
			log.info("Starting Iote2eRequestSparkConsumer");
			ssc.start();
		} catch( Exception e ) {
			log.error(e.getMessage(),e);
			System.exit(8);
		}

		try {
			log.info("Starting Iote2eRequestSparkConsumer");
			ssc.awaitTermination();
		} catch( InterruptedException e1 ) {
			log.warn(e1.getMessage());
		} catch( Exception e2 ) {
			log.error(e2.getMessage(),e2);
			System.exit(8);
		}
		
    }
}
