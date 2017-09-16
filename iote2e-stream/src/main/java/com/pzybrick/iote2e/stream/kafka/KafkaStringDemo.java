/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.stream.kafka;

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
 

/**
 * The Class KafkaStringDemo.
 */
public class KafkaStringDemo {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(KafkaStringDemo.class);
    
    /** The consumer. */
    private final ConsumerConnector consumer;
    
    /** The topic. */
    private final String topic;
    
    /** The executor. */
    private  ExecutorService executor;
 
    /**
     * Instantiates a new kafka string demo.
     *
     * @param zookeeper the zookeeper
     * @param groupId the group id
     * @param topic the topic
     */
    public KafkaStringDemo(String zookeeper, String groupId, String topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(zookeeper, groupId));
        this.topic = topic;
    }
 
    /**
     * Shutdown.
     */
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
 
    /**
     * Run.
     *
     * @param numThreads the num threads
     */
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
 
    /**
     * Gets the executor.
     *
     * @return the executor
     */
    public ExecutorService getExecutor() {
		return executor;
	}

	/**
	 * Sets the executor.
	 *
	 * @param executor the new executor
	 */
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	/**
	 * Gets the consumer.
	 *
	 * @return the consumer
	 */
	public ConsumerConnector getConsumer() {
		return consumer;
	}

	/**
	 * Gets the topic.
	 *
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Creates the consumer config.
	 *
	 * @param zookeeper the zookeeper
	 * @param groupId the group id
	 * @return the consumer config
	 */
	private static ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        //props.put("autocommit.enable", "false");
 
        return new ConsumerConfig(props);
    }
 
    /**
     * The main method.
     *
     * @param args the arguments
     */
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
    
	
	/**
	 * Produce test msgs.
	 *
	 * @param numEvents the num events
	 * @param bootstrapServers the bootstrap servers
	 * @param topic the topic
	 */
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
	
	
	/**
	 * The Class ConsumerDemoThread.
	 */
	public class ConsumerDemoThread implements Runnable {
	    
    	/** The kafka stream. */
    	private KafkaStream kafkaStream;
	    
    	/** The thread number. */
    	private int threadNumber;
	    
    	/** The consumer demo master. */
    	private KafkaStringDemo consumerDemoMaster;
	 
	    /**
    	 * Instantiates a new consumer demo thread.
    	 *
    	 * @param kafkaStream the kafka stream
    	 * @param threadNumber the thread number
    	 * @param consumerDemoMaster the consumer demo master
    	 */
    	public ConsumerDemoThread(KafkaStream kafkaStream, int threadNumber, KafkaStringDemo consumerDemoMaster) {
	        this.threadNumber = threadNumber;
	        this.kafkaStream = kafkaStream;
	        this.consumerDemoMaster = consumerDemoMaster;
	    }
	 
	    /* (non-Javadoc)
    	 * @see java.lang.Runnable#run()
    	 */
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