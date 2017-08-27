package com.pzybrick.iote2e.ws.route;

import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;

public class RouteBdbbByteBufferToKafkaImpl implements RouteBdbbByteBuffer {
	private static final Logger logger = LogManager.getLogger(RouteBdbbByteBufferToKafkaImpl.class);
	protected KafkaProducer<String, byte[]> kafkaProducer;
	protected String kafkaTopicBdbb;
	protected String kafkaGroupBdbb;
	
	
	public RouteBdbbByteBufferToKafkaImpl(){
		
	}

	
	public void init(MasterConfig masterConfig) throws Exception {
		logger.debug("constructing, connecting to Kafka producer");
		kafkaTopicBdbb = masterConfig.getKafkaTopicBdbb();
		kafkaGroupBdbb = masterConfig.getKafkaGroupBdbb();
		Properties props = new Properties();
		props.put("bootstrap.servers", masterConfig.getKafkaBootstrapServers() );
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		kafkaProducer = new KafkaProducer<String, byte[]>(props);
		logger.debug("kafkaProducer: {}", kafkaProducer.toString());
	}

	public void routeToTarget( ByteBuffer byteBuffer ) throws Exception {
		String key = String.valueOf(System.currentTimeMillis());
		logger.debug("sending to kafka, key={}", key );
		ProducerRecord<String, byte[]> data = null;
		long retrySleepMs = 100;
		final int MAX_RETRIES = 10;
		boolean isSuccess = false;
		for( int i=0 ; i<MAX_RETRIES ; i++ ) {
			isSuccess = false;
			try {
				data = new ProducerRecord<String, byte[]>(kafkaTopicBdbb, key, byteBuffer.array() );
				// send is an async call
				// for this simple testing, treat the send like a synchronous call, wait for it to complete
				Future<RecordMetadata> future = kafkaProducer.send(data);
				future.get();
				isSuccess = true;
				break;
			} catch( Exception e ) {
				logger.error("send/get {}", e.getMessage(), e);
			}catch( Throwable t ) {
				logger.error("send/get {}", t.getMessage(), t);
			} 
			logger.debug("+++++++++++ NOT SUCCESS attempt={}, key={}", i, key);
			try {
				Thread.sleep(retrySleepMs);
			} catch( Exception e ) {}
			retrySleepMs = retrySleepMs * 2;
		}
		if( !isSuccess ) throw new Exception("Failure on send/get");
		logger.debug("BDBB JSON sent to kafka, kafka key={}, length={}", key, data.value().length );

	} 
}
