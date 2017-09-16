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
package com.pzybrick.iote2e.common.ignite;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;



/**
 * The Class ThreadIgniteSubscribe.
 */
public class ThreadIgniteSubscribe extends Thread {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ThreadIgniteSubscribe.class);
	
	/** The ignite filter key. */
	private String igniteFilterKey;
	
	/** The shutdown. */
	private boolean shutdown;
	
	/** The subscribe up. */
	private boolean subscribeUp;
	
	/** The queue iote 2 e results. */
	private ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults;
	
	/** The thread poller. */
	private Thread threadPoller;
	
	/** The ignite grid connection. */
	private IgniteGridConnection igniteGridConnection;
	
	/** The master config. */
	private MasterConfig masterConfig;
	

	/**
	 * Instantiates a new thread ignite subscribe.
	 */
	public ThreadIgniteSubscribe() {
	}

	/**
	 * Start thread subscribe.
	 *
	 * @param masterConfig the master config
	 * @param igniteFilterKey the ignite filter key
	 * @param queueIote2eResults the queue iote 2 e results
	 * @param threadPoller the thread poller
	 * @return the thread ignite subscribe
	 * @throws Exception the exception
	 */
	public static ThreadIgniteSubscribe startThreadSubscribe( MasterConfig masterConfig, String igniteFilterKey,
			ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults, Thread threadPoller ) throws Exception {
		ThreadIgniteSubscribe threadIgniteSubscribe = new ThreadIgniteSubscribe()
				.setIgniteFilterKey(igniteFilterKey)
				.setQueueIote2eResults(queueIote2eResults)
				.setThreadPoller(threadPoller)
				.setMasterConfig(masterConfig);
		threadIgniteSubscribe.start();
		long timeoutAt = System.currentTimeMillis() + 10000L;
		while (System.currentTimeMillis() < timeoutAt && !threadIgniteSubscribe.isSubscribeUp() ) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		if (!threadIgniteSubscribe.isSubscribeUp())
			throw new Exception("Timeout starting ThreadSubscribe");
		return threadIgniteSubscribe;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			igniteGridConnection = new IgniteGridConnection().connect(masterConfig);
//			MasterConfig masterConfig = MasterConfig.getInstance();
//			String igniteConfigPath = masterConfig.getIgniteConfigPath();
//			if( igniteConfigPath == null ) throw new Exception("Required MasterConfig value igniteConfigPath is not set, try setting to location of ignite-iote2e.xml");
//			if( !igniteConfigPath.endsWith("/") ) igniteConfigPath = igniteConfigPath + "/";
//			String igniteConfigPathNameExt = igniteConfigPath + masterConfig.getIgniteConfigFile();
//			logger.info("Initializing Ignite, config file=" + igniteConfigPathNameExt + ", config name=" +  masterConfig.getIgniteConfigName());
//			IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(
//					igniteConfigPathNameExt, masterConfig.getIgniteConfigName());
//			Ignition.setClientMode(masterConfig.isIgniteClientMode());
//			ignite = Ignition.start(igniteConfiguration);
//			cache = ignite.getOrCreateCache(masterConfig.getIgniteCacheName());	
//			logger.debug("***************** create cache, igniteFilterKey={}, cacheName={}, cache={}", igniteFilterKey,
//					masterConfig.getIgniteCacheName(), cache);
			// Create new continuous query.
			ContinuousQuery<String,  byte[]> qry = new ContinuousQuery<>();
			qry.setTimeInterval(100);
			// Callback that is called locally when update notifications are
			// received.
			qry.setLocalListener(new CacheEntryUpdatedListener<String,  byte[]>() {
				Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();
				@Override
				public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends byte[]>> evts) {
					for (CacheEntryEvent<? extends String, ? extends  byte[]> e : evts) {
						Iote2eResult iote2eResult = null;
						try {
							iote2eResult = iote2eResultReuseItem.fromByteArray(e.getValue());
							logger.debug("******************** ignite - adding to queueIote2eResults, eventType={}, key={}, value={}", e.getEventType().toString(), e.getKey(), iote2eResult.toString());
							queueIote2eResults.add(iote2eResult);
							if( threadPoller != null ) threadPoller.interrupt(); // if subscriber is waiting then wake up
						} catch( Exception e2 ) {
							logger.error(e2.getMessage(),e );
						}
					}
				}
			});
			
			Iote2eIgniteCacheEntryEventSingleFilter<String,byte[]> sourceSensorCacheEntryEventFilter = 
					new Iote2eIgniteCacheEntryEventSingleFilter<String, byte[]>(igniteFilterKey);
			qry.setRemoteFilterFactory(new Factory<Iote2eIgniteCacheEntryEventSingleFilter<String, byte[]>>() {
				@Override
				public Iote2eIgniteCacheEntryEventSingleFilter<String, byte[]> create() {
					return sourceSensorCacheEntryEventFilter;
					//return new SourceSensorCacheEntryEventFilter<String, String>(remoteFilterKey);
				}
			});
			
            qry.setInitialQuery(new ScanQuery<>(new IgniteBiPredicate<String, byte[]>() {
                @Override public boolean apply(String key, byte[] val) {
                	// TODO: recover forward from checkpoint
                    return true;
                }
            }));
			
			subscribeUp = true;
			while (true) {
				try (QueryCursor<Cache.Entry<String, byte[]>> cur = igniteGridConnection.getCache().query(qry)) {
					try {
						Thread.sleep(500000);
					} catch (java.lang.InterruptedException e) {}
					if (shutdown) {
						logger.info("Shutdown subcribe start ");
						try {
//							if (igniteGridConnection.getCache() != null)
//								igniteGridConnection.getCache().close();
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
						logger.info("Shutdown subcribe complete ");
						return;
					}
				}
			}

		} catch (javax.cache.CacheException e) {
			if( e.getCause() instanceof org.apache.ignite.IgniteInterruptedException )
				logger.info("Stopping thread due to Ignite shutdown");
			else logger.error(e.getMessage(), e);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return;
		} finally {
			
		}
	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		this.shutdown = true;
		interrupt();
	}

	/**
	 * Gets the ignite filter key.
	 *
	 * @return the ignite filter key
	 */
	public String getIgniteFilterKey() {
		return igniteFilterKey;
	}

	/**
	 * Checks if is shutdown.
	 *
	 * @return true, if is shutdown
	 */
	public boolean isShutdown() {
		return shutdown;
	}

	/**
	 * Checks if is subscribe up.
	 *
	 * @return true, if is subscribe up
	 */
	public boolean isSubscribeUp() {
		return subscribeUp;
	}

	/**
	 * Gets the queue iote 2 e results.
	 *
	 * @return the queue iote 2 e results
	 */
	public ConcurrentLinkedQueue<Iote2eResult> getQueueIote2eResults() {
		return queueIote2eResults;
	}

	/**
	 * Sets the ignite filter key.
	 *
	 * @param igniteFilterKey the ignite filter key
	 * @return the thread ignite subscribe
	 */
	public ThreadIgniteSubscribe setIgniteFilterKey(String igniteFilterKey) {
		this.igniteFilterKey = igniteFilterKey;
		return this;
	}

	/**
	 * Sets the shutdown.
	 *
	 * @param shutdown the shutdown
	 * @return the thread ignite subscribe
	 */
	public ThreadIgniteSubscribe setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
		return this;
	}

	/**
	 * Sets the subscribe up.
	 *
	 * @param subscribeUp the subscribe up
	 * @return the thread ignite subscribe
	 */
	public ThreadIgniteSubscribe setSubscribeUp(boolean subscribeUp) {
		this.subscribeUp = subscribeUp;
		return this;
	}

	/**
	 * Sets the queue iote 2 e results.
	 *
	 * @param iote2eResults the iote 2 e results
	 * @return the thread ignite subscribe
	 */
	public ThreadIgniteSubscribe setQueueIote2eResults(ConcurrentLinkedQueue<Iote2eResult> iote2eResults) {
		this.queueIote2eResults = iote2eResults;
		return this;
	}

	/**
	 * Gets the thread poller.
	 *
	 * @return the thread poller
	 */
	public Thread getThreadPoller() {
		return threadPoller;
	}

	/**
	 * Sets the thread poller.
	 *
	 * @param threadPoller the thread poller
	 * @return the thread ignite subscribe
	 */
	public ThreadIgniteSubscribe setThreadPoller(Thread threadPoller) {
		this.threadPoller = threadPoller;
		return this;
	}

	/**
	 * Gets the ignite grid connection.
	 *
	 * @return the ignite grid connection
	 */
	public IgniteGridConnection getIgniteGridConnection() {
		return igniteGridConnection;
	}

	/**
	 * Gets the master config.
	 *
	 * @return the master config
	 */
	public MasterConfig getMasterConfig() {
		return masterConfig;
	}

	/**
	 * Sets the master config.
	 *
	 * @param masterConfig the master config
	 * @return the thread ignite subscribe
	 */
	public ThreadIgniteSubscribe setMasterConfig(MasterConfig masterConfig) {
		this.masterConfig = masterConfig;
		return this;
	}
}
