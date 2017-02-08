package com.pzybrick.iote2e.tests.sim;

import java.util.HashMap;
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
import com.pzybrick.iote2e.ruleproc.config.MasterConfig;
import com.pzybrick.iote2e.ruleproc.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.ruleproc.persist.ConfigDao;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestHandler;
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.ruleproc.spark.Iote2eRequestSparkConsumer;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;
import com.pzybrick.iote2e.tests.common.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.tests.common.ThreadSparkRun;

public class SimBase {
	private static final Logger logger = LogManager.getLogger(SimBase.class);
	protected ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests;
	protected Iote2eRequestHandler iote2eRequestHandler;
	protected Iote2eSvc iote2eSvc;
	protected KafkaProducer<String, byte[]> kafkaProducer;
	protected Iote2eRequestReuseItem iote2eRequestReuseItem;
	protected Iote2eResultReuseItem iote2eResultReuseItem;
	protected String kafkaTopic;
	protected String kafkaGroup;
	protected ThreadIgniteSubscribe threadIgniteSubscribe;
	protected IgniteSingleton igniteSingleton = null;
	protected ConcurrentLinkedQueue<byte[]> iote2eResultsBytes;
	protected Gson gson;
	protected static Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	protected static ThreadSparkRun threadSparkRun;


	public SimBase() {
		super();
	}

	public void before() throws Exception {
		// cassandra
		ActuatorStateDao.useKeyspace( System.getenv("CASSANDRA_KEYSPACE_NAME"));
		MasterConfig masterConfig = MasterConfig.getInstance();
		iote2eResultReuseItem = new Iote2eResultReuseItem();
		iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		iote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
		iote2eRequestHandler = new Iote2eRequestHandler(iote2eRequests);
		iote2eSvc = iote2eRequestHandler.getIote2eSvc();
		iote2eRequestHandler.start();
		
		iote2eResultsBytes = new ConcurrentLinkedQueue<byte[]>();
		igniteSingleton = IgniteSingleton.getInstance(iote2eRequestHandler.getMasterConfig());
		logger.info("Cache name: " + iote2eRequestHandler.getMasterConfig().getIgniteCacheName());

		kafkaTopic = masterConfig.getKafkaTopic();
		kafkaGroup = masterConfig.getKafkaGroup();
		Properties props = new Properties();
		props.put("bootstrap.servers", masterConfig.getKafkaBootstrapServers() );
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		kafkaProducer = new KafkaProducer<String, byte[]>(props);
		// Start Spark locally if the simulation is running locally, otherwise assume SparkStreaming already up and running under Docker
		// spark
		if( masterConfig.getSparkMaster().startsWith("local")) {
	    	iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
	    	threadSparkRun = new ThreadSparkRun( iote2eRequestSparkConsumer);
	    	threadSparkRun.start();
		}
	}

	public void after() throws Exception {
		logger.info(">>>> Shutdown hook calling after");
		try {
			if( MasterConfig.getInstance().getSparkMaster().startsWith("local")) {
		    	iote2eRequestSparkConsumer.stop();
				threadSparkRun.join();
			}
			iote2eRequestHandler.shutdown();
			iote2eRequestHandler.join();
			kafkaProducer.close();
			threadIgniteSubscribe.shutdown();
			threadIgniteSubscribe.join();
			IgniteSingleton.reset();
			ConfigDao.disconnect();
			ActuatorStateDao.disconnect();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	

	protected void kafkaSend(String loginName, String sourceName, String sourceType, String sensorName,
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
			logger.info("Sending to kafka: {}", iote2eRequest.toString());
			kafkaProducer.send(data);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
}
