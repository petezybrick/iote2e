package com.pzybrick.learn.avro2kafkawave;

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

public class AvroConsumerWaveMaster {
	private static final Log log = LogFactory.getLog(AvroConsumerWaveMaster.class);
	private final ConsumerConnector consumer;
	private final String topic;
	private ExecutorService executor;

	public AvroConsumerWaveMaster(String a_zookeeper, String a_groupId, String a_topic) {
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(a_zookeeper, a_groupId));
		this.topic = a_topic;
	}

	public void shutdown() {
		if (consumer != null)
			consumer.shutdown();
		if (executor != null)
			executor.shutdown();
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
		for (final KafkaStream<byte[], byte[]> stream : streams) {
			executor.submit(new AvroConsumerWaveThread(stream, threadNumber));
			threadNumber++;
		}
	}

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

	public static void main(String[] args) {
		try {
			LogTool.initConsole();
			String zooKeeper = args[0];
			String groupId = args[1];
			String topic = args[2];
			int threads = Integer.parseInt(args[3]);

			AvroConsumerWaveMaster avroConsumerWaveMaster = new AvroConsumerWaveMaster(zooKeeper, groupId, topic);
			avroConsumerWaveMaster.run(threads);

			try {
				Thread.sleep(2000L);
			} catch (Exception e) {
			}
			new LaunchProducer().start();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException ie) {

			}
			avroConsumerWaveMaster.shutdown();
		} catch (Exception e) {
			log.error(e);
			log.error(e.getStackTrace().toString());
		}
	}
	
	private static class LaunchProducer extends Thread {

		@Override
		public synchronized void start() {
			AvroProducerWaveMaster avroProducerWaveMaster = new AvroProducerWaveMaster();
			avroProducerWaveMaster.startThreads(1, 10, 100);
		}
		
	}
}