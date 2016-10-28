package com.pzybrick.learn.kafka;

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
 
public class ConsumerDemoMaster {
	private static final Log log = LogFactory.getLog(ConsumerDemoMaster.class);
    private final ConsumerConnector consumer;
    private final String topic;
    private  ExecutorService executor;
 
    public ConsumerDemoMaster(String a_zookeeper, String a_groupId, String a_topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
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

	private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        //props.put("autocommit.enable", "false");
 
        return new ConsumerConfig(props);
    }
 
    public static void main(String[] args) {
    	LogTool.initConsole();
        String zooKeeper = args[0];
        String groupId = args[1];
        String topic = args[2];
        int threads = Integer.parseInt(args[3]);
        
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