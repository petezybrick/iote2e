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
package com.pzybrick.iote2e.tests.spark;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.gson.Gson;
import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.stream.request.Iote2eSvc;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.tests.common.Iote2eRequestHandlerIgniteTestThread;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;


/**
 * The Class TestSparkHandlerBase.
 */
public class TestSparkHandlerBase extends TestCommonHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestSparkHandlerBase.class);
	
	/** The queue iote 2 e requests. */
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests;
	
	/** The queue iote 2 e results. */
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults;
	
	/** The iote 2 e request handler. */
	protected Iote2eRequestHandlerIgniteTestThread iote2eRequestHandler;
	
	/** The iote 2 e svc. */
	protected Iote2eSvc iote2eSvc;
	
	/** The kafka producer. */
	protected KafkaProducer<String, byte[]> kafkaProducer;
	
	/** The iote 2 e request reuse item. */
	protected Iote2eRequestReuseItem iote2eRequestReuseItem;
	
	/** The iote 2 e result reuse item. */
	protected Iote2eResultReuseItem iote2eResultReuseItem;
	
	/** The kafka topic. */
	protected String kafkaTopic;
	
	/** The kafka group. */
	protected String kafkaGroup;
	
	/** The thread ignite subscribe. */
	protected ThreadIgniteSubscribe threadIgniteSubscribe;
	
	/** The gson. */
	protected Gson gson;

	/**
	 * Instantiates a new test spark handler base.
	 *
	 * @throws Exception the exception
	 */
	public TestSparkHandlerBase() throws Exception {
		super();
	}
	
	/**
	 * Before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		// cassandra
		ConfigDao.connect( System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME"));
		ActuatorStateDao.connect( System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME"));
	}
	
	/**
	 * After class.
	 */
	@AfterClass
	public static void afterClass() {
		try {
			ConfigDao.disconnect();
			ActuatorStateDao.disconnect();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Before.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void before() throws Exception {
		logger.info(
				"------------------------------------------------------------------------------------------------------");
		iote2eResultReuseItem = new Iote2eResultReuseItem();
		iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
		iote2eRequestHandler = new Iote2eRequestHandlerIgniteTestThread(masterConfig,queueIote2eRequests);
		iote2eSvc = iote2eRequestHandler.getIote2eSvc();
		iote2eRequestHandler.start();		
		queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
		logger.info("Cache name: " + masterConfig.getIgniteCacheName());
		kafkaTopic = masterConfig.getKafkaTopic();
		kafkaGroup = masterConfig.getKafkaGroup();
		Properties props = new Properties();
		props.put("bootstrap.servers", masterConfig.getKafkaBootstrapServers() );
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		kafkaProducer = new KafkaProducer<String, byte[]>(props);
	}

	/**
	 * After.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void after() throws Exception {
		while (!queueIote2eRequests.isEmpty()) {
			try {
				Thread.sleep(2000L);
			} catch (Exception e) {
			}
		}
		try {
			Thread.sleep(2000L);
		} catch (Exception e) {
		}		
		iote2eRequestHandler.shutdown();
		iote2eRequestHandler.join();
		kafkaProducer.close();
		threadIgniteSubscribe.shutdown();
		threadIgniteSubscribe.join();
	}

	/**
	 * Common run.
	 *
	 * @param loginName the login name
	 * @param sourceName the source name
	 * @param sourceType the source type
	 * @param sensorName the sensor name
	 * @param sensorValue the sensor value
	 * @param igniteFilterKey the ignite filter key
	 * @throws Exception the exception
	 */
	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue, String igniteFilterKey) throws Exception {
		logger.info(String.format("loginName=%s, sourceName=%s, sourceType=%s, sensorName=%s, sensorValue=%s", loginName,
				sourceName, sourceType, sensorName, sensorValue));
		try {
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( masterConfig, 
					igniteFilterKey, queueIote2eResults, (Thread)null);

			Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
			pairs.put(sensorName, sensorValue);
			Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder().setLoginName(loginName).setSourceName(sourceName)
					.setSourceType(sourceType).setRequestUuid(UUID.randomUUID().toString())
					.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601()).setOperation(OPERATION.SENSORS_VALUES)
					.setPairs(pairs).build();

			String key = String.valueOf(System.currentTimeMillis());
			ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>(kafkaTopic, key, iote2eRequestReuseItem.toByteArray(iote2eRequest));
			logger.info("Sending to kafka: {}", iote2eRequest.toString());
			// send is an async call
			Future future = kafkaProducer.send(data);
			// for this simple testing, treat the send like a synchronous call, wait for it to complete
			try {
				RecordMetadata recordMetadata = (RecordMetadata)future.get();
			} catch( Exception e ) {
				logger.error(">>> get() {}", e.getMessage(), e);
				throw e;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

}
