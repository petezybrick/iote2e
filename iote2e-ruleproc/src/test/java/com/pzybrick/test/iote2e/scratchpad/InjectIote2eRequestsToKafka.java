package com.pzybrick.test.iote2e.scratchpad;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.util.Utf8;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;

public class InjectIote2eRequestsToKafka {
	private static final Log log = LogFactory.getLog(InjectIote2eRequestsToKafka.class);
	protected KafkaProducer<String, byte[]> kafkaProducer;
	protected Iote2eRequestReuseItem iote2eRequestReuseItem;
	protected String kafkaTopic;
	protected String kafkaGroup;
	
    public static void main(String[] args) throws Exception {
    	InjectIote2eRequestsToKafka injectIote2eRequestsToKafka = new InjectIote2eRequestsToKafka();
    	injectIote2eRequestsToKafka.process();
    }

	public InjectIote2eRequestsToKafka() {
		super();
	}

	public void process() throws Exception {
		iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		
		kafkaTopic = "com.pzybrick.iote2e.schema.avro.Iote2eRequest-sandbox";
		kafkaGroup = "iote2e-group-sandbox";
		Properties props = new Properties();
		props.put("bootstrap.servers", "hp-lt-ubuntu-1:9092,hp-lt-ubuntu-1:9093,hp-lt-ubuntu-1:9094" );
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		props.put("partition.assignment.strategy", "RoundRobin");
		props.put("request.required.acks", "1");
		props.put("group.id", kafkaGroup);

		kafkaProducer = new KafkaProducer<String, byte[]>(props);
		
		for( int i=1 ; i<1000 ; i++ ) {
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
