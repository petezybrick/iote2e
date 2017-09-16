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
package com.pzybrick.learn.avro2kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.learn.utils.LogTool;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
 

/**
 * The Class AvroConsumerMaster.
 */
public class AvroConsumerMaster {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(AvroConsumerMaster.class);
    
    /** The consumer. */
    private final ConsumerConnector consumer;
    
    /** The topic. */
    private final String topic;
    
    /** The executor. */
    private  ExecutorService executor;
 
    /**
     * Instantiates a new avro consumer master.
     *
     * @param a_zookeeper the a zookeeper
     * @param a_groupId the a group id
     * @param a_topic the a topic
     */
    public AvroConsumerMaster(String a_zookeeper, String a_groupId, String a_topic) {
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
            executor.submit(new AvroConsumerThread(stream, threadNumber));
            threadNumber++;
        }
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
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        return new ConsumerConfig(props);
    }
 
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
    	LogTool.initConsole();
        String zooKeeper = args[0];
        String groupId = args[1];
        String topic = args[2];
        int threads = Integer.parseInt(args[3]);
        
        String[] args2 = {"20"};
        AvroProducer.main(args2);
        try { Thread.sleep(1000L); } catch(Exception e) {}
 
        AvroConsumerMaster avroConsumerMaster = new AvroConsumerMaster(zooKeeper, groupId, topic);
        avroConsumerMaster.run(threads);
 
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
 
        }
        avroConsumerMaster.shutdown();
    }
}