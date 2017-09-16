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

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
 

/**
 * The Class ConsumerDemoThread.
 */
public class ConsumerDemoThread implements Runnable {
    
    /** The kafka stream. */
    private KafkaStream kafkaStream;
    
    /** The thread number. */
    private int threadNumber;
    
    /** The consumer demo master. */
    private ConsumerDemoMaster consumerDemoMaster;
 
    /**
     * Instantiates a new consumer demo thread.
     *
     * @param kafkaStream the kafka stream
     * @param threadNumber the thread number
     * @param consumerDemoMaster the consumer demo master
     */
    public ConsumerDemoThread(KafkaStream kafkaStream, int threadNumber, ConsumerDemoMaster consumerDemoMaster) {
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
        	System.out.println(summary);
        }
        System.out.println("Shutting down Thread: " + threadNumber);
    }
}