package com.pzybrick.learn.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
 
public class ConsumerDemoThread implements Runnable {
	private static final Log log = LogFactory.getLog(ConsumerDemoThread.class);
    private KafkaStream kafkaStream;
    private int threadNumber;
    private ConsumerDemoMaster consumerDemoMaster;
 
    public ConsumerDemoThread(KafkaStream kafkaStream, int threadNumber, ConsumerDemoMaster consumerDemoMaster) {
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
        	log.info(">>> " + summary);
        }
        log.info(">>> Shutting down Thread: " + threadNumber);
    }
}