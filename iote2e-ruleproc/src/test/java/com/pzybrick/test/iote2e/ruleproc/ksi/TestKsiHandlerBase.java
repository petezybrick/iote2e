package com.pzybrick.test.iote2e.ruleproc.ksi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.kafka.Iote2eSvcKafkaImpl;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestHandler;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.test.iote2e.ruleproc.common.TestRuleProcCommon;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class TestKsiHandlerBase implements TestRuleProcCommon {
	private static final Logger logger = LogManager.getLogger(TestKsiHandlerBase.class);
	protected ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests;
	protected Iote2eRequestHandler iote2eRequestHandler;
	protected Iote2eSvcKafkaImpl iote2eSvc;
	protected KafkaProducer<String, byte[]> kafkaProducer;
	protected Iote2eRequestReuseItem iote2eRequestReuseItem;
	protected String kafkaTopic;
	protected String kafkaGroup;
	protected Iote2eRequestPoller iote2eRequestPoller;

	public TestKsiHandlerBase() {
		super();
	}

	@Before
	public void before() throws Exception {
		logger.info(
				"------------------------------------------------------------------------------------------------------");
		iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		iote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
		iote2eRequestHandler = new Iote2eRequestHandler(System.getenv("REQUEST_CONFIG_JSON_FILE_KAFKA"), iote2eRequests);
		iote2eSvc = (Iote2eSvcKafkaImpl) iote2eRequestHandler.getIote2eSvc();
		iote2eSvc.setRuleEvalResults(null);
		iote2eRequestHandler.start();
		
		kafkaTopic = System.getenv("KAFKA_TOPIC_UNIT_TEST");
		kafkaGroup = System.getenv("KAFKA_GROUP_UNIT_TEST");
		Properties props = new Properties();
		props.put("bootstrap.servers", System.getenv("KAFKA_BOOTSTRAP_SERVERS_UNIT_TEST") );
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("partition.assignment.strategy", "RoundRobin");
		props.put("request.required.acks", "1");
		props.put("group.id", kafkaGroup);
		kafkaProducer = new KafkaProducer<String, byte[]>(props);
		iote2eRequestPoller = new Iote2eRequestPoller("testfolder");
	}

	@After
	public void after() throws Exception {
		while (!iote2eRequests.isEmpty()) {
			try {
				Thread.sleep(2000L);
			} catch (Exception e) {
			}
		}
		iote2eRequestHandler.shutdown();
		iote2eRequestHandler.join();
		iote2eRequestPoller.shutdown();
		iote2eRequestPoller.join();
		kafkaProducer.close();
	}

	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue) throws Exception {
		logger.info(String.format("loginName=%s, sourceName=%s, sourceType=%s, sensorName=%s, sensorValue=%s", loginName,
				sourceName, sourceType, sensorName, sensorValue));
		try {
			Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
			pairs.put(sensorName, sensorValue);
			Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder().setLoginName(loginName).setSourceName(sourceName)
					.setSourceType(sourceType).setRequestUuid(UUID.randomUUID().toString())
					.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601()).setOperation(OPERATION.SENSORS_VALUES)
					.setPairs(pairs).build();

			String key = String.valueOf(System.currentTimeMillis());
			ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>(kafkaTopic, key, iote2eRequestReuseItem.toByteArray(iote2eRequest));
			kafkaProducer.send(data);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}


	protected List<RuleEvalResult> commonGetRuleEvalResults(long maxWaitMsecs) {
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			if (iote2eSvc.getRuleEvalResults() != null)
				return iote2eSvc.getRuleEvalResults();
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		return null;
	}
    
	
	public class Iote2eRequestPoller extends Thread {
	    private String folderTestOutput;
	    private boolean shutdown;
	 
	    public Iote2eRequestPoller( String folderTestOutput ) {
	        this.folderTestOutput = folderTestOutput;
	    }
	 
	    public void run() {
	    	try {
		    	long timeoutAt = System.currentTimeMillis() + 5000l;
		    	while( System.currentTimeMillis() < timeoutAt ) {
		    		// TODO: Poll folder for test data
		    		try { 
		    			sleep(500);
		    		} catch(Exception e ) {}
		    		if( shutdown ) break;
		    	}
	    		
	    	} catch( Exception e ) {
	    		logger.error(e.getMessage(), e);
	    	}
	        logger.info(">>> Shutting down");
	    }
	    
	    public void shutdown() throws Exception {
	    	shutdown = true;
	    	this.interrupt();
	    }
	}
}
