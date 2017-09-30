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


/**
 * The Class RouteValidicByteBufferToKafkaImpl.
 */
public class RouteValidicByteBufferToKafkaImpl implements RouteValidicByteBuffer {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RouteValidicByteBufferToKafkaImpl.class);
	
	/** The kafka producer. */
	protected KafkaProducer<String, byte[]> kafkaProducer;
	
	/** The kafka topic validic. */
	protected String kafkaTopicValidic;
	
	/** The kafka group validic. */
	protected String kafkaGroupValidic;
	
	
	/**
	 * Instantiates a new route validic byte buffer to kafka impl.
	 */
	public RouteValidicByteBufferToKafkaImpl(){
		
	}

	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.ws.route.RouteValidicByteBuffer#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	public void init(MasterConfig masterConfig) throws Exception {
		logger.debug("constructing, connecting to Kafka producer");
		kafkaTopicValidic = masterConfig.getKafkaTopicValidic();
		kafkaGroupValidic = masterConfig.getKafkaGroupValidic();
		Properties props = new Properties();
		props.put("bootstrap.servers", masterConfig.getKafkaBootstrapServers() );
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		kafkaProducer = new KafkaProducer<String, byte[]>(props);
		logger.debug("kafkaProducer: {}", kafkaProducer.toString());
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.ws.route.RouteValidicByteBuffer#routeToTarget(java.nio.ByteBuffer)
	 */
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
				data = new ProducerRecord<String, byte[]>(kafkaTopicValidic, key, byteBuffer.array() );
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
			logger.debug("+++++++++++ NOT SUCCESS attempt={}, key={}", i, key);
			try {
				Thread.sleep(retrySleepMs);
			} catch( Exception e ) {}
			retrySleepMs = retrySleepMs * 2;
		}
		if( !isSuccess ) throw new Exception("Failure on send/get");
		logger.debug("Validic sent to kafka, kafka key={}", key );

	} 
}
