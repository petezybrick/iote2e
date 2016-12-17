package com.pzybrick.test.iote2e.ruleproc.ksi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.gson.Gson;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.kafka.Iote2eSvcKafkaImpl;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestHandler;
import com.pzybrick.iote2e.ruleproc.spark.Iote2eRequestSparkConsumer;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.test.iote2e.ruleproc.common.TestRuleProcCommon;
import com.pzybrick.test.iote2e.ruleproc.common.ThreadIgniteSubscribe;
import com.pzybrick.test.iote2e.ruleproc.common.ThreadSparkRun;

public class TestKsiHandlerBase implements TestRuleProcCommon {
	private static final Logger logger = LogManager.getLogger(TestKsiHandlerBase.class);
	protected ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests;
	protected Iote2eRequestHandler iote2eRequestHandler;
	protected Iote2eSvcKafkaImpl iote2eSvc;
	protected KafkaProducer<String, byte[]> kafkaProducer;
	protected Iote2eRequestReuseItem iote2eRequestReuseItem;
	protected String kafkaTopic;
	protected String kafkaGroup;
	protected static Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	protected static ThreadSparkRun threadSparkRun;
	protected ThreadIgniteSubscribe threadIgniteSubscribe;
	protected IgniteSingleton igniteSingleton = null;
	protected ConcurrentLinkedQueue<byte[]> subscribeResults;
	protected Gson gson;

	public TestKsiHandlerBase() {
		super();
	}
	
	@BeforeClass
	public static void beforeClass() {
		// spark
    	iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
    	String[] sparkArgs = System.getenv("SPARK_ARGS_UNIT_TEST").split(" ");
    	threadSparkRun = new ThreadSparkRun( iote2eRequestSparkConsumer, sparkArgs);
    	threadSparkRun.start();
	}
	
	@AfterClass
	public static void afterClass() {
		try {
	    	iote2eRequestSparkConsumer.stop();
			threadSparkRun.join();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
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
		
		subscribeResults = new ConcurrentLinkedQueue<byte[]>();
		igniteSingleton = IgniteSingleton.getInstance(iote2eRequestHandler.getRuleConfig());
		logger.info(">>> Cache name: " + iote2eRequestHandler.getRuleConfig().getSourceResponseIgniteCacheName());

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
		kafkaProducer.close();
		threadIgniteSubscribe.shutdown();
		threadIgniteSubscribe.join();
		IgniteSingleton.reset();
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

}
