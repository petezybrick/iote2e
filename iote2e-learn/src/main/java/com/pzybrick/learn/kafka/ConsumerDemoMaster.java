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
package com.pzybrick.learn.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
 

/**
 * The Class ConsumerDemoMaster.
 */
public class ConsumerDemoMaster {
    
    /** The consumer. */
    private final ConsumerConnector consumer;
    
    /** The topic. */
    private final String topic;
    
    /** The executor. */
    private  ExecutorService executor;
 
    /**
     * Instantiates a new consumer demo master.
     *
     * @param a_zookeeper the a zookeeper
     * @param a_groupId the a group id
     * @param a_topic the a topic
     */
    public ConsumerDemoMaster(String a_zookeeper, String a_groupId, String a_topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
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
     * @param a_numThreads the a num threads
     */
    public void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(a_numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
 
        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);
 
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
	 * @param a_zookeeper the a zookeeper
	 * @param a_groupId the a group id
	 * @return the consumer config
	 */
	private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
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
        String zooKeeper = "localhost:2181"; // args[0];
        String groupId = "group1"; // args[1];
        String topic = "pz-topic"; //args[2];
        int threads = 1; // Integer.parseInt(args[3]);
        
        ConsumerDemoMaster consumerDemoMaster = new ConsumerDemoMaster(zooKeeper, groupId, topic);
        consumerDemoMaster.run(threads);
        
        String[] args2 = {"20"};
        KafkaProducerTest.main(args2);
        try { Thread.sleep(1000L); } catch(Exception e) {}
 
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
 
        }
        consumerDemoMaster.shutdown();
    }
}