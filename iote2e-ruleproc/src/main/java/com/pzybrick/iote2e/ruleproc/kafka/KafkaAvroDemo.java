package com.pzybrick.iote2e.ruleproc.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
 
public class KafkaAvroDemo {
	private static final Log log = LogFactory.getLog(KafkaAvroDemo.class);
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;
 
    public KafkaAvroDemo(String zookeeper, String groupId, String topic) {
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
        
        KafkaAvroDemo kafkaAvroDemo = new KafkaAvroDemo(zooKeeper, groupId, topic);
        kafkaAvroDemo.run(threads);
        
        try {
        	produceTestMsgs( numMsgs, bootstrapServers, topic );
        } catch( Exception e ) {
        	log.error(e.getMessage(),e);
        	System.exit(8);
        }
        
        try { Thread.sleep(1000L); } catch(Exception e) {}
 
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
 
        }
        kafkaAvroDemo.shutdown();
    }
    
	
	private static void produceTestMsgs( long numEvents, String bootstrapServers, String topic ) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers );
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		props.put("partition.assignment.strategy", "RoundRobin");
		props.put("request.required.acks", "1");
		props.put("group.id", "group1");

		Map<String, Object> map = new HashMap<String, Object>();

		KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);
		long keyNum = System.currentTimeMillis();
		long msgOffset = 0;
		Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		for (int i = 0; i < numEvents; i++) {
			log.info(">>> Producing Iote2eRequest: " + i);
			Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
			pairs.put("testSensorNamea_"+i, "testSensorValuea_"+i);
			pairs.put("testSensorNameb_"+i, "testSensorValueb_"+i);
			Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
			metadata.put("testMetadataNamea_"+i, "testMetadataValuea_"+i);
			metadata.put("testMetadataNameb_"+i, "testMetadataValueb_"+i);
			Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder()
					.setLoginName("testLoginName_"+i)
					.setSourceName("testSourceName_"+i)
					.setSourceType("testSourceType_"+i)
					.setMetadata(metadata)
					.setRequestUuid("testRequestUuid_"+i)
					.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601())
					.setOperation(OPERATION.SENSORS_VALUES)
					.setPairs(pairs)
					.build();
			String key = String.valueOf(keyNum);
			ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>(topic, key, iote2eRequestReuseItem.toByteArray(iote2eRequest));
			producer.send(data);
			keyNum++;
		}
		producer.close();
	}
	
	
	public class ConsumerDemoThread implements Runnable {
	    private KafkaStream kafkaStream;
	    private int threadNumber;
	    private KafkaAvroDemo consumerDemoMaster;
	 
	    public ConsumerDemoThread(KafkaStream kafkaStream, int threadNumber, KafkaAvroDemo consumerDemoMaster) {
	        this.threadNumber = threadNumber;
	        this.kafkaStream = kafkaStream;
	        this.consumerDemoMaster = consumerDemoMaster;
	    }
	 
	    public void run() {
	    	Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
	        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
	        while (it.hasNext()) {
				MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
				String key = new String(messageAndMetadata.key());
				try {
		        	String summary = 
		        			"Thread " + threadNumber + 
		        			", topic=" + messageAndMetadata.topic() + 
		        			", partition=" + messageAndMetadata.partition() + 
		        			", key=" + key + 
		        			", offset=" + messageAndMetadata.offset() + 
		        			", timestamp=" + messageAndMetadata.timestamp() + 
		        			", timestampType=" + messageAndMetadata.timestampType() + 
		        			", iote2eRequest=" + iote2eRequestReuseItem.fromByteArray(messageAndMetadata.message()).toString();
		        	log.info(">>> Consumed: " + summary);
				} catch( Exception e ) {
					log.error(e.getMessage(), e);
				}
	        }
	        log.info(">>> Shutting down Thread: " + threadNumber);
	    }
	}
}