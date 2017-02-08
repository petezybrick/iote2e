package com.pzybrick.iote2e.ruleproc.kafka;

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

public class InjectIote2eRequestsToKafka {
	private static final Logger logger = LogManager.getLogger(InjectIote2eRequestsToKafka.class);
	private KafkaProducer<String, byte[]> kafkaProducer;
	private Iote2eRequestReuseItem iote2eRequestReuseItem;
	private String kafkaTopic;
	private String kafkaGroup;
	private String bootstrapServers;
	private ArgMap argMap;
	
    public static void main(String[] args) throws Exception {
    	logger.info(">>> Starting injector <<<");
    	InjectIote2eRequestsToKafka injectIote2eRequestsToKafka = new InjectIote2eRequestsToKafka();
    	injectIote2eRequestsToKafka.process(args);
    }

	public InjectIote2eRequestsToKafka() {
		super();
	}

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
