package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.junit.After;
import org.junit.Before;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.common.utils.IotE2eUtils;
import com.pzybrick.iote2e.ruleproc.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.ignite.Iote2eIgniteCacheEntryEventFilter;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestHandler;
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.AvroSchemaUtils;
import com.pzybrick.iote2e.schema.util.LoginActuatorResponseFromByteArrayReuseItem;
import com.pzybrick.iote2e.schema.util.LoginSourceRequestToByteArrayReuseItem;

public class TestIgniteHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteHandlerBase.class);
	protected ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests;
	protected ConcurrentLinkedQueue<byte[]> subscribeResults;
	protected Iote2eRequestHandler iote2eRequestHandler;
	protected Iote2eSvc iote2eSvc;
	protected ThreadSubscribe threadSubscribe;
	protected boolean subscribeUp;
	protected IgniteSingleton igniteSingleton = null;
	protected Gson gson;
	protected LoginActuatorResponseFromByteArrayReuseItem loginActuatorResponseFromByteArrayReuseItem;
//	protected BinaryDecoder binaryDecoder = null;
//	private DatumReader<ActuatorResponse> datumReaderActuatorResponse = null;

	@Before
	public void before() throws Exception {
		try {
			gson = new GsonBuilder().create();
			loginActuatorResponseFromByteArrayReuseItem = new LoginActuatorResponseFromByteArrayReuseItem();
			subscribeResults = new ConcurrentLinkedQueue<byte[]>();
			iote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
			iote2eRequestHandler = new Iote2eRequestHandler(System.getenv("REQUEST_CONFIG_JSON_FILE_IGNITE"),
					iote2eRequests);
			iote2eSvc = iote2eRequestHandler.getIote2eRequestSvc();
			igniteSingleton = IgniteSingleton.getInstance(iote2eRequestHandler.getRuleConfig());
			log.info(
					"------------------------------------------------------------------------------------------------------");
			log.info(">>> Cache name: " + iote2eRequestHandler.getRuleConfig().getSourceResponseIgniteCacheName());
			iote2eRequestHandler.start();
		} catch (Exception e) {
			log.error("Exception in before, " + e.getMessage(), e);
		}
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
		threadSubscribe.shutdown();
		threadSubscribe.join();
		IgniteSingleton.reset();
	}

	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue, String igniteFilterKey) throws Exception {
		log.info(String.format("loginName=%s, sourceName=%s, sourceType=%s, sensorName=%s, sensorValue=%s", loginName,
				sourceName, sourceType, sensorName, sensorValue));
		try {
			startThreadSubscribe(iote2eRequestHandler.getRuleConfig(), igniteFilterKey);
			Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
			pairs.put(sensorName, sensorValue);
			Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder().setLoginName(loginName).setSourceName(sourceName)
					.setSourceType(sourceType).setRequestUuid(UUID.randomUUID().toString())
					.setTimestamp(IotE2eUtils.getDateNowUtc8601()).setOperation(OPERATION.SENSORS_VALUES)
					.setPairs(pairs).build();
			iote2eRequestHandler.addIote2eRequest(iote2eRequest);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	protected List<LoginActuatorResponse> commonThreadSubscribeGetLoginActuatorResponses(long maxWaitMsecs) throws Exception {
		List<LoginActuatorResponse> loginActuatorResponses = new ArrayList<LoginActuatorResponse>();
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			if (subscribeResults.size() > 0) {
				try {
					Thread.sleep(250);
				} catch (Exception e) {
				}
				for( byte[] bytes : subscribeResults ) {
					try {
						AvroSchemaUtils.loginActuatorResponseFromByteArray(loginActuatorResponseFromByteArrayReuseItem, bytes);
						loginActuatorResponses.add( loginActuatorResponseFromByteArrayReuseItem.getLoginActuatorResponse() );
					} catch( IOException e ) {
						log.error(e.getMessage(),e);
						throw e;
					}
				}
				break;
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		return loginActuatorResponses;
	}

	private void startThreadSubscribe(RuleConfig ruleConfig, String igniteFilterKey) throws Exception {
		threadSubscribe = new ThreadSubscribe(ruleConfig, igniteFilterKey);
		threadSubscribe.start();
		long timeoutAt = System.currentTimeMillis() + 10000L;
		while (System.currentTimeMillis() < timeoutAt && !subscribeUp) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		if (!subscribeUp)
			throw new Exception("Timeout starting ThreadSubscribe");
	}

	private class ThreadSubscribe extends Thread {
		private RuleConfig ruleConfig;
		private String remoteFilterKey;
		private boolean shutdown;

		public ThreadSubscribe(RuleConfig ruleConfig, String remoteFilterKey) {
			this.ruleConfig = ruleConfig;
			this.remoteFilterKey = remoteFilterKey;
		}

		public void shutdown() {
			this.shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			try {
				// Create new continuous query.
				ContinuousQuery<String,  byte[]> qry = new ContinuousQuery<>();

				// Callback that is called locally when update notifications are
				// received.
				qry.setLocalListener(new CacheEntryUpdatedListener<String,  byte[]>() {
					@Override
					public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends  byte[]>> evts) {
						for (CacheEntryEvent<? extends String, ? extends  byte[]> e : evts) {
							log.info("Updated entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
							subscribeResults.add(e.getValue());
						}
					}
				});

				Iote2eIgniteCacheEntryEventFilter<String,byte[]> sourceSensorCacheEntryEventFilter = 
						new Iote2eIgniteCacheEntryEventFilter<String, byte[]>(remoteFilterKey);
				qry.setRemoteFilterFactory(new Factory<Iote2eIgniteCacheEntryEventFilter<String, byte[]>>() {
					@Override
					public Iote2eIgniteCacheEntryEventFilter<String, byte[]> create() {
						return sourceSensorCacheEntryEventFilter;
						//return new SourceSensorCacheEntryEventFilter<String, String>(remoteFilterKey);
					}
				});

//				qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<String, String>>() {
//					@Override
//					public CacheEntryEventFilter<String, String> create() {
//						return new CacheEntryEventFilter<String, String>() {
//							@Override
//							public boolean evaluate(CacheEntryEvent<? extends String, ? extends String> e) {
//								final String remoteFilter = "lo1so1|lo1so1se1|";
//								if (e.getKey().startsWith(remoteFilter))
//									return true;
//								else
//									return false;
//								// return false;
//							}
//						};
//					}
//				});
				
				QueryCursor<Cache.Entry<String, byte[]>> cur = igniteSingleton.getCache().query(qry);

			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return;

			}
			subscribeUp = true;
			while (true) {
				if (shutdown)
					break;
				try {
					sleep(60 * 60 * 5);
				} catch (InterruptedException e) {
				}
			}
		}
	}

}
