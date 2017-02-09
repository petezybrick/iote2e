package com.pzybrick.iote2e.ws.route;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;

public class RouteIote2eRequestToKafkaImpl implements RouteIote2eRequest {
	protected KafkaProducer<String, byte[]> kafkaProducer;
	protected Iote2eRequestReuseItem iote2eRequestReuseItem;
	protected String kafkaTopic;
	protected String kafkaGroup;
	
	public RouteIote2eRequestToKafkaImpl() throws Exception {
		MasterConfig masterConfig = MasterConfig.getInstance();
		kafkaTopic = masterConfig.getKafkaTopic();
		kafkaGroup = masterConfig.getKafkaGroup();
		Properties props = new Properties();
		props.put("bootstrap.servers", masterConfig.getKafkaBootstrapServers() );
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		kafkaProducer = new KafkaProducer<String, byte[]>(props);
	}

	public void routeToTarget( Iote2eRequest iote2eRequest ) throws Exception {
		String key = String.valueOf(System.currentTimeMillis());
		ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>(kafkaTopic, key, iote2eRequestReuseItem.toByteArray(iote2eRequest));
		kafkaProducer.send(data);
	} 
}
