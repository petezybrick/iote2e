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
package com.pzybrick.iote2e.stream.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.utils.ArgMap;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;


/**
 * The Class InjectIote2eRequestsToKafka.
 */
public class InjectIote2eRequestsToKafka {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(InjectIote2eRequestsToKafka.class);
	
	/** The kafka producer. */
	private KafkaProducer<String, byte[]> kafkaProducer;
	
	/** The iote 2 e request reuse item. */
	private Iote2eRequestReuseItem iote2eRequestReuseItem;
	
	/** The kafka topic. */
	private String kafkaTopic;
	
	/** The kafka group. */
	private String kafkaGroup;
	
	/** The bootstrap servers. */
	private String bootstrapServers;
	
	/** The arg map. */
	private ArgMap argMap;
	
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
    	logger.info(">>> Starting injector <<<");
    	InjectIote2eRequestsToKafka injectIote2eRequestsToKafka = new InjectIote2eRequestsToKafka();
    	injectIote2eRequestsToKafka.process(args);
    }

	/**
	 * Instantiates a new inject iote 2 e requests to kafka.
	 */
	public InjectIote2eRequestsToKafka() {
		super();
	}

	/**
	 * Process.
	 *
	 * @param args the args
	 * @throws Exception the exception
	 */
	public void process(String[] args) throws Exception {
    	argMap = new ArgMap(args);
    	logger.info("Starting, argMap: " + argMap.dump() );

    	kafkaGroup = argMap.get("kafkaGroup"); 
    	kafkaTopic = argMap.get("kafkaTopic"); 
    	bootstrapServers = argMap.get("bootstrapServers");
    	Integer numInjects = argMap.getInteger("numInjects", 1000);
		iote2eRequestReuseItem = new Iote2eRequestReuseItem();

		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers );
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		kafkaProducer = new KafkaProducer<String, byte[]>(props);
		
		for( int i=1 ; i<numInjects ; i++ ) {
			String key = String.valueOf(System.currentTimeMillis());
			
			Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
			metadata.put( new Utf8("testMetadataNamea"+i), new Utf8("testMetadataValuea"+i));
			metadata.put( new Utf8("testMetadataNameb"+i), new Utf8("testMetadataValueb"+i));
			Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
			pairs.put( new Utf8("testSensorNamea"+i),  new Utf8("testSensorValuea"+i));
			pairs.put( new Utf8("testSensorNameb"+i),  new Utf8("testSensorValueb"+i));
			Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder()
					.setLoginName("testLoginName"+i)
					.setSourceName("testSourceName"+i)
					.setSourceType("testSourceType"+i)
					.setMetadata(metadata)
					.setRequestUuid("testRequestUuid"+i)
					.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601())
					.setOperation(OPERATION.SENSORS_VALUES)
					.setPairs(pairs)
					.build();
			
			ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>(kafkaTopic, key, iote2eRequestReuseItem.toByteArray(iote2eRequest));
			kafkaProducer.send(data);
			if( i % 25 == 0 ) Thread.sleep(250L);
		}
		
		kafkaProducer.close();
	}

}
