package com.pzybrick.iote2e.ruleproc.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
 
public class KafkaStringDemo {
	private static final Logger logger = LogManager.getLogger(KafkaStringDemo.class);
    private final ConsumerConnector consumer;
    private final String topic;
    private  ExecutorService executor;
 
    public KafkaStringDemo(String zookeeper, String groupId, String topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(zookeeper, groupId));
        this.topic = topic;
    }
 
    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted during shutdown, exiting uncleanly");
        }
   }
 
    public void run(int numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
 
        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(numThreads);
 
        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerDemoThread(stream, threadNumber, this));
            threadNumber++;
        }
    }
 
    public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public ConsumerConnector getConsumer() {
		return consumer;
	}

	public String getTopic() {
		return topic;
	}

	private static ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        //props.put("autocommit.enable", "false");
 
        return new ConsumerConfig(props);
    }
 
    public static void main(String[] args) {
    	int numMsgs = Integer.parseInt(args[0]);
        String zooKeeper = args[1];  // "iote2e-zoo2:2181"; 
        String groupId = args[2];  // "group1"; 
        String topic = args[3];    // "com.pzybrick.iote2e.schema.avro.SourceSensorValue-sandbox"; 
        String bootstrapServers = args[4]; //"iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092"
        int threads = 3; 
        
        KafkaStringDemo kafkaStringDemo = new KafkaStringDemo(zooKeeper, groupId, topic);
        kafkaStringDemo.run(threads);
        
        produceTestMsgs( numMsgs, bootstrapServers, topic );
        try { Thread.sleep(1000L); } catch(Exception e) {}
 
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
 
        }
        kafkaStringDemo.shutdown();
    }
    
	
	private static void produceTestMsgs( long numEvents, String bootstrapServers, String topic ) {
		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers );
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("partition.assignment.strategy", "RoundRobin");
		props.put("request.required.acks", "1");
		props.put("group.id", "group1");

		Map<String, Object> map = new HashMap<String, Object>();

		KafkaProducer<String, Object> producer = new KafkaProducer<String, Object>(props);
		long keyNum = System.currentTimeMillis();
		long msgOffset = 0;

		for (int i = 0; i < numEvents; i++) {
			logger.info(">>> Producing: " + i);
			String key = String.valueOf(keyNum);
			String value = "some data " + msgOffset++;
			ProducerRecord<String, Object> data = new ProducerRecord<String, Object>(topic, key, value);
			producer.send(data);
			keyNum++;
		}
		producer.close();
	}
	
	
	public class ConsumerDemoThread implements Runnable {
	    private KafkaStream kafkaStream;
	    private int threadNumber;
	    private KafkaStringDemo consumerDemoMaster;
	 
	    public ConsumerDemoThread(KafkaStream kafkaStream, int threadNumber, KafkaStringDemo consumerDemoMaster) {
	        this.threadNumber = threadNumber;
	        this.kafkaStream = kafkaStream;
	        this.consumerDemoMaster = consumerDemoMaster;
	    }
	 
	    public void run() {
	        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
	        while (it.hasNext()) {
	        	MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
	        	String key = new String(  messageAndMetadata.key() );
	        	String message = new String(  messageAndMetadata.message() );
	        	String summary = 
	        			"Thread " + threadNumber + 
	        			", topic=" + messageAndMetadata.topic() + 
	        			", partition=" + messageAndMetadata.partition() + 
	        			", key=" + key + 
	        			", message=" + message + 
	        			", offset=" + messageAndMetadata.offset() + 
	        			", timestamp=" + messageAndMetadata.timestamp() + 
	        			", timestampType=" + messageAndMetadata.timestampType();
	        	logger.info(">>> Consumed: " + summary);
	        }
	        logger.info(">>> Shutting down Thread: " + threadNumber);
	    }
	}
}