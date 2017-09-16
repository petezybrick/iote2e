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
package com.pzybrick.iote2e.tests.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.kafka.Iote2eSvcKafkaImpl;
import com.pzybrick.iote2e.stream.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.tests.common.Iote2eRequestHandlerIgniteTestThread;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;


/**
 * The Class TestKafkaHandlerBase.
 */
public class TestKafkaHandlerBase extends TestCommonHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestKafkaHandlerBase.class);
	
	/** The queue iote 2 e requests. */
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests;
	
	/** The queue iote 2 e results. */
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults;
	
	/** The iote 2 e request reuse item. */
	protected Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
	
	/** The iote 2 e request handler. */
	protected Iote2eRequestHandlerIgniteTestThread iote2eRequestHandler;
	
	/** The iote 2 e svc. */
	protected Iote2eSvcKafkaImpl iote2eSvc;
	
	/** The kafka producer. */
	protected KafkaProducer<String, byte[]> kafkaProducer;
	
	/** The kafka topic. */
	protected String kafkaTopic;
	
	/** The kafka group. */
	protected String kafkaGroup;
	
	/** The kafka consumer connector. */
	protected ConsumerConnector kafkaConsumerConnector;
	
	/** The executor. */
	protected ExecutorService executor;

	/**
	 * Instantiates a new test kafka handler base.
	 *
	 * @throws Exception the exception
	 */
	public TestKafkaHandlerBase() throws Exception {
		super();
	}
	
	
	/**
	 * Before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		TestCommonHandler.beforeClass();
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
		queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
		queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
		iote2eRequestHandler = new Iote2eRequestHandlerIgniteTestThread(masterConfig, queueIote2eRequests);
		iote2eSvc = (Iote2eSvcKafkaImpl) iote2eRequestHandler.getIote2eSvc();
		iote2eSvc.setRuleEvalResults(null);
		iote2eRequestHandler.start();
		kafkaTopic = masterConfig.getKafkaTopic();
		kafkaGroup = masterConfig.getKafkaGroup();
		Properties props = new Properties();
		props.put("bootstrap.servers", masterConfig.getKafkaBootstrapServers() );
		//props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		kafkaProducer = new KafkaProducer<String, byte[]>(props);
		kafkaConsumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig( masterConfig.createKafkaZookeeperHostPortPairs(),kafkaGroup));
		startStreamConsumers(masterConfig.getKafkaConsumerNumThreads());
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
		iote2eRequestHandler.shutdown();
		iote2eRequestHandler.join();
		kafkaConsumerConnector.shutdown();
		kafkaProducer.close();
	}

	/**
	 * Common run.
	 *
	 * @param loginName the login name
	 * @param sourceName the source name
	 * @param sourceType the source type
	 * @param sensorName the sensor name
	 * @param sensorValue the sensor value
	 * @throws Exception the exception
	 */
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
			// send is an async call
			Future future = kafkaProducer.send(data);
			// for this simple testing, treat the send like a synchronous call, wait for it to complete
			try {
				RecordMetadata recordMetadata = (RecordMetadata)future.get();
			} catch( Exception e ) {
				logger.error("get() {}", e.getMessage());
				throw e;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}


	/**
	 * Common get rule eval results.
	 *
	 * @param maxWaitMsecs the max wait msecs
	 * @return the list
	 */
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
	
	/**
	 * Creates the consumer config.
	 *
	 * @param zookeeper the zookeeper
	 * @param groupId the group id
	 * @return the consumer config
	 */
	private static ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        //props.put("autocommit.enable", "false");
 
        return new ConsumerConfig(props);
    }
	
	 
    /**
     * Start stream consumers.
     *
     * @param numThreads the num threads
     */
    public void startStreamConsumers(int numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(kafkaTopic, new Integer(numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = kafkaConsumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(kafkaTopic);
        executor = Executors.newFixedThreadPool(numThreads);
        int threadNumber = 0;
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            executor.submit(new KafkaConsumerThread(stream, threadNumber));
            threadNumber++;
        }
    }
    
	
	/**
	 * The Class KafkaConsumerThread.
	 */
	public class KafkaConsumerThread implements Runnable {
	    
    	/** The kafka stream. */
    	private KafkaStream<byte[], byte[]> kafkaStream;
	    
    	/** The thread number. */
    	private int threadNumber;
	 
	    /**
    	 * Instantiates a new kafka consumer thread.
    	 *
    	 * @param kafkaStream the kafka stream
    	 * @param threadNumber the thread number
    	 */
    	public KafkaConsumerThread(KafkaStream<byte[], byte[]> kafkaStream, int threadNumber ) {
	        this.threadNumber = threadNumber;
	        this.kafkaStream = kafkaStream;
	    }
	 
	    /* (non-Javadoc)
    	 * @see java.lang.Runnable#run()
    	 */
    	public void run() {
	    	Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
	        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
	        while (it.hasNext()) {
				MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
				try {
					Iote2eRequest iote2eRequest = iote2eRequestReuseItem.fromByteArray(messageAndMetadata.message());
					logger.info(">>> Consumed: " + iote2eRequest.toString() );
					iote2eRequestHandler.addIote2eRequest(iote2eRequest);
				} catch( Exception e ) {
					logger.error(e.getMessage(), e);
				}
	        }
	        logger.info(">>> Shutting down Thread: " + threadNumber);
	    }
	}
}
