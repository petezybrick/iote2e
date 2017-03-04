package com.pzybrick.iote2e.ws.route;

import java.util.Properties;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;

public class RouteIote2eRequestToKafkaImpl implements RouteIote2eRequest {
	private static final Logger logger = LogManager.getLogger(RouteIote2eRequestToKafkaImpl.class);
	protected KafkaProducer<String, byte[]> kafkaProducer;
	protected Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
	protected String kafkaTopic;
	protected String kafkaGroup;
	
	public RouteIote2eRequestToKafkaImpl(MasterConfig masterConfig) throws Exception {
		logger.debug("constructing, connecting to Kafka producer");
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
		logger.debug(iote2eRequest.toString());
		String key = String.valueOf(System.currentTimeMillis());
		logger.debug("sending to kafka, key={}", key );
		ProducerRecord<String, byte[]> data = null;
		long retrySleepMs = 100;
		final int MAX_RETRIES = 10;
		boolean isSuccess = false;
		for( int i=0 ; i<MAX_RETRIES ; i++ ) {
			isSuccess = false;
			try {
				data = new ProducerRecord<String, byte[]>(kafkaTopic, key, iote2eRequestReuseItem.toByteArray(iote2eRequest));
				// send is an async call
				// for this simple testing, treat the send like a synchronous call, wait for it to complete
				Future<RecordMetadata> future = kafkaProducer.send(data);;
				future.get();
				isSuccess = true;
				break;
			} catch( Exception e ) {
				logger.error("send/get {}", e.getMessage(), e);
			}catch( Throwable t ) {
				logger.error("send/get {}", t.getMessage(), t);
			} 
			logger.debug("+++++++++++ NOT SUCCESS attempt={},request uuid={}, key={}", i, key, iote2eRequest.getRequestUuid() );
			try {
				Thread.sleep(retrySleepMs);
			} catch( Exception e ) {}
			retrySleepMs = retrySleepMs * 2;
		}
		if( !isSuccess ) throw new Exception("Failure on send/get");
		logger.debug("Iote2eRequest sent to kafka, kafka key={}, request uuid={}", key, iote2eRequest.getRequestUuid() );

	} 
}
