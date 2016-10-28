package com.pzybrick.learn.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
 
public class ConsumerDemoThread implements Runnable {
    private KafkaStream kafkaStream;
    private int threadNumber;
    private ConsumerDemoMaster consumerDemoMaster;
 
    public ConsumerDemoThread(KafkaStream stream, int threadNumber, ConsumerDemoMaster consumerDemoMaster) {
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
        	System.out.println(summary);
        }
        System.out.println("Shutting down Thread: " + threadNumber);
    }
}