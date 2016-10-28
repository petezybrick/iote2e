package com.pzybrick.learn.avro2kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.pzybrick.avro.schema.User;
import com.pzybrick.learn.utils.LogTool;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;


public class AvroProducer {
    public static void main(String[] args) {
    	LogTool.initConsole();
        long events = Long.parseLong(args[0]);
        Random rnd = new Random();
 
        Properties props = new Properties();
        //props.put("metadata.broker.list", "hp-lt-ubuntu-1:9092");
        props.put("bootstrap.servers", "hp-lt-ubuntu-1:9092");
        props.put("producer.type", "sync");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        //props.put("partition.assignment.strategy", "range");
        //props.put("partitioner.class", "com.pzybrick.kafka1.training.KafkaPartitionerTest");
        props.put("request.required.acks", "1");
        props.put("group.id", "group1");
 
        //Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(User.getClassSchema());
        Injection<GenericRecord, byte[]> recordInjectionUser = GenericAvroCodecs.toBinary(User.getClassSchema());
        
        KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);
        long keyNum = System.currentTimeMillis();
 
        for (long nEvents = 0; nEvents < events; nEvents++) { 
        	   System.out.println("creating event "+nEvents);
               String key = String.valueOf(keyNum);
               
               User user = User.newBuilder().setName("name"+keyNum).setFavoriteColor("color"+keyNum).setFavoriteNumber((int)keyNum).build();
               byte[] bytes = recordInjectionUser.apply(user);
               ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>("pzmultiptn1", key, bytes);
               producer.send(data);
               keyNum++;
        }
        producer.close();
    }
}