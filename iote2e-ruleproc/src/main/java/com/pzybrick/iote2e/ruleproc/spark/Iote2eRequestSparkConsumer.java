package com.pzybrick.iote2e.ruleproc.spark;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.pzybrick.iote2e.common.utils.ArgMap;

import consumer.kafka.MessageAndMetadata;
import consumer.kafka.ReceiverLauncher;

public class Iote2eRequestSparkConsumer {
	private static final Logger logger = LogManager.getLogger(Iote2eRequestSparkConsumer.class.getName());
	private ArgMap argMap;
    private SparkConf conf;
    private JavaStreamingContext ssc;
	
	
    public static void main(String[] args) throws Exception {
    	//Iote2eRequestSparkConsumer iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
    	//iote2eRequestSparkConsumer.process(args);
    	
    	Iote2eRequestSparkConsumer iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
    	RunProcess runProcess = new RunProcess( iote2eRequestSparkConsumer, args);
    	runProcess.start();
    	try {
    		Thread.sleep(5000);
    	} catch( Exception e ) {}
    	iote2eRequestSparkConsumer.stop();
    	runProcess.join();

    }
    
    private static class RunProcess extends Thread {
    	private Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
    	private String[] args;
    	
    	public RunProcess( Iote2eRequestSparkConsumer iote2eRequestSparkConsumer, String[] args ) {
    		this.args = args;
    		this.iote2eRequestSparkConsumer = iote2eRequestSparkConsumer;
    	}
		@Override
		public void run() {
			try {
	    		iote2eRequestSparkConsumer.process(args);
			} catch( Exception e ) {
				logger.error(e.getMessage(), e);
			}
		}
    	
    }
    	
    public void process(String[] args) throws Exception {
    	argMap = new ArgMap(args);
    	logger.info("Starting, argMap: " + argMap.dump() );
    	String appName = argMap.get("appName"); 
    	String master = argMap.get("master"); 
    	Integer numThreads = argMap.getInteger("numThreads", 3); 
    	Integer durationMs = argMap.getInteger("durationMs", 1000); 
    	String kafkaGroup = argMap.get("kafkaGroup"); 
    	String kafkaTopic = argMap.get("kafkaTopic"); 
    	String zookeeperHosts = argMap.get("zookeeperHosts"); 
    	Integer zookeeperPort = argMap.getInteger("zookeeperPort", 2181);  
    	String zookeeperBrokerPath = argMap.get("zookeeperBrokerPath"); 
    	String kafkaConsumerId = argMap.get("kafkaConsumerId"); 
    	String zookeeperConsumerConnection = argMap.get("zookeeperConsumerConnection"); 
    	String zookeeperConsumerPath = argMap.get("zookeeperConsumerPath"); 


        conf = new SparkConf()
                .setAppName(appName);
        if( master != null ) conf.setMaster( master );
        ssc = new JavaStreamingContext(conf, new Duration(durationMs));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(kafkaTopic, new Integer(numThreads));
        Properties kafkaProps = new Properties();
        kafkaProps.put("group.id", kafkaGroup);
        // Spark Kafka Consumer https://github.com/dibbhatt/kafka-spark-consumer
        kafkaProps.put("zookeeper.hosts", zookeeperHosts);
        kafkaProps.put("zookeeper.port", String.valueOf(zookeeperPort) );
        kafkaProps.put("zookeeper.broker.path", zookeeperBrokerPath );
        kafkaProps.put("kafka.topic", kafkaTopic);
        kafkaProps.put("kafka.consumer.id", kafkaConsumerId );
        kafkaProps.put("zookeeper.consumer.connection", zookeeperConsumerConnection);
        kafkaProps.put("zookeeper.consumer.path", zookeeperConsumerPath);
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
			logger.info("Starting Iote2eRequestSparkConsumer");
			ssc.start();
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			System.exit(8);
		}

		try {
			logger.info("Started Iote2eRequestSparkConsumer");
			ssc.awaitTermination();
	    	logger.info("Stopped Spark");
		} catch( InterruptedException e1 ) {
			logger.warn(e1.getMessage());
		} catch( Exception e2 ) {
			logger.error(e2.getMessage(),e2);
			System.exit(8);
		}
		
    }
    
    public void stop() throws Exception {
    	logger.info("Stopping Spark...");
    	ssc.stop(true);
    }
}
