package com.pzybrick.learn.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerTest {
	public static void main(String[] args) {
		long events = Long.parseLong(args[0]);
		Random rnd = new Random();

		Properties props = new Properties();
		//props.put("metadata.broker.list", "hp-lt-ubuntu-1:9092");
		props.put("bootstrap.servers", "iote2e-kafka1:9092,iote2e-kafka2:9092,iote2e-kafka3:9092");
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// props.put("partition.assignment.strategy", "range");
		// props.put("partitioner.class",
		// "com.pzybrick.kafka1.training.KafkaPartitionerTest");
		props.put("request.required.acks", "1");
		props.put("group.id", "group1");

		Map<String, Object> map = new HashMap<String, Object>();

		KafkaProducer<String, Object> producer = new KafkaProducer<String, Object>(props);
		long keyNum = System.currentTimeMillis();
		long msgOffset = 0;

		for (long nEvents = 0; nEvents < events; nEvents++) {
			System.out.println("creating event " + nEvents);
			String key = String.valueOf(keyNum);
			String value = "some data " + msgOffset++;
			ProducerRecord<String, Object> data = new ProducerRecord<String, Object>("pz-topic", key, value);
			producer.send(data);
			keyNum++;
		}
		producer.close();
	}
}