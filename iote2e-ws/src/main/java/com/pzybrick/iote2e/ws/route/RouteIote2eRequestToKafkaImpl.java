package com.pzybrick.iote2e.ws.route;

import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.ws.socket.EntryPointIote2eRequest;

public class RouteIote2eRequestToKafkaImpl implements RouteIote2eRequest {
	private static final Logger logger = LogManager.getLogger(EntryPointIote2eRequest.class);
	protected KafkaProducer<String, byte[]> kafkaProducer;
	protected Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
	protected String kafkaTopic;
	protected String kafkaGroup;
	
	public RouteIote2eRequestToKafkaImpl() throws Exception {
		logger.debug("constructing, connecting to Kafka producer");
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
		logger.debug("kafkaProducer: {}", kafkaProducer.toString());
	}

	public void routeToTarget( Iote2eRequest iote2eRequest ) throws Exception {
		try {
			logger.debug(iote2eRequest.toString());
			String key = String.valueOf(System.currentTimeMillis());
			logger.debug("sending to kafka, key={}", key );
			ProducerRecord<String, byte[]> data = null;
			try {
				logger.debug("+++++++++++ before data key={}", key );
				data = new ProducerRecord<String, byte[]>(kafkaTopic, key, iote2eRequestReuseItem.toByteArray(iote2eRequest));
				logger.debug("+++++++++++ after data key={}", key );
			} catch( Exception e ) {
				logger.error(e.getMessage(), e);
				throw e;
			}
			try {
				// send is an async call
				logger.debug("+++++++++++ before future key={}", key );
				Future future = kafkaProducer.send(data);
				logger.debug("+++++++++++ after future key={}", key );
				// for this simple testing, treat the send like a synchronous call, wait for it to complete
				try {
					logger.debug("+++++++++++ before recordMetadata key={}", key );
					RecordMetadata recordMetadata = (RecordMetadata)future.get();
					logger.debug("+++++++++++ after recordMetadata key={}", key );
				} catch( Exception e ) {
					logger.error("get() {}", e.getMessage());
					throw e;
				}catch( Throwable t ) {
					logger.error("get() {}", t.getMessage());
					throw t;
				}
				logger.debug("sent to kafka, key={}", key );
	
			} catch( Exception e ) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		} catch(Throwable t ) {
			logger.error(t.getMessage(), t);
			throw t;
		}
	} 
}
