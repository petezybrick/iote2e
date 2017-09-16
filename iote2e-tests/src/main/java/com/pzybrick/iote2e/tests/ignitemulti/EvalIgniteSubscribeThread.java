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
package com.pzybrick.iote2e.tests.ignitemulti;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.ignite.Iote2eIgniteCacheEntryEventSingleFilter;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;


/**
 * The Class EvalIgniteSubscribeThread.
 */
public class EvalIgniteSubscribeThread extends Thread {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(EvalIgniteSubscribeThread.class);
	
	/** The ignite filter key. */
	private String igniteFilterKey;
	
	/** The shutdown. */
	private boolean shutdown;
	
	/** The subscribe up. */
	private boolean subscribeUp;
	
	/** The queue iote 2 e results. */
	private ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults;
	
	/** The poller thread. */
	private Thread pollerThread;
	
	/** The cache. */
	private IgniteCache<String, byte[]> cache = null;
	
	/** The ignite. */
	private Ignite ignite = null;

	/**
	 * Instantiates a new eval ignite subscribe thread.
	 */
	public EvalIgniteSubscribeThread() {
	}

	/**
	 * Start thread subscribe.
	 *
	 * @param igniteFilterKey the ignite filter key
	 * @param queueIote2eResults the queue iote 2 e results
	 * @param threadPoller the thread poller
	 * @return the eval ignite subscribe thread
	 * @throws Exception the exception
	 */
	public static EvalIgniteSubscribeThread startThreadSubscribe(String igniteFilterKey,
			ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults, Thread threadPoller) throws Exception {
		EvalIgniteSubscribeThread igniteSubscribeThread = new EvalIgniteSubscribeThread()
				.setIgniteFilterKey(igniteFilterKey).setQueueIote2eResults(queueIote2eResults)
				.setPollerThread(threadPoller);
		igniteSubscribeThread.start();
		long timeoutAt = System.currentTimeMillis() + 10000L;
		while (System.currentTimeMillis() < timeoutAt && !igniteSubscribeThread.isSubscribeUp()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		if (!igniteSubscribeThread.isSubscribeUp())
			throw new Exception("Timeout starting ThreadSubscribe");
		return igniteSubscribeThread;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			String cacheName = "iote2e-evalmulticlient-cache";
			String igniteConfigPath = "/home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/config_ignite/";
			if (igniteConfigPath == null)
				throw new Exception(
						"Required MasterConfig value igniteConfigPath is not set, try setting to location of ignite-iote2e.xml");
			if (!igniteConfigPath.endsWith("/"))
				igniteConfigPath = igniteConfigPath + "/";
			String igniteConfigPathNameExt = igniteConfigPath + "ignite-iote2e-local-peer-false.xml";
			String configName = "ignite.cfg";
			logger.info("Initializing Ignite, config file=" + igniteConfigPathNameExt + ", config name=" + configName);
			IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(igniteConfigPathNameExt, configName);
			Ignition.setClientMode(true);
			ignite = Ignition.getOrStart(igniteConfiguration);
			cache = ignite.getOrCreateCache(cacheName);

			logger.debug("***************** create cache, igniteFilterKey={}, cacheName={}, cache={}", igniteFilterKey,
					cacheName, cache);
			// Create new continuous query.
			ContinuousQuery<String, byte[]> qry = new ContinuousQuery<String, byte[]>();
			qry.setTimeInterval(100);
			// Callback that is called locally when update notifications are
			// received.
			qry.setLocalListener(new CacheEntryUpdatedListener<String, byte[]>() {
				Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();

				@Override
				public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends byte[]>> evts) {
					for (CacheEntryEvent<? extends String, ? extends byte[]> e : evts) {
						Iote2eResult iote2eResult = null;
						try {
							iote2eResult = iote2eResultReuseItem.fromByteArray(e.getValue());
							logger.trace(
									"******************** ignite - adding to queueIote2eResults, eventType={}, key={}, value={}",
									e.getEventType().toString(), e.getKey(), iote2eResult.toString());
							queueIote2eResults.add(iote2eResult);
							if (pollerThread != null)
								pollerThread.interrupt(); // if subscriber is waiting then wake up
						} catch (Exception e2) {
							logger.error(e2.getMessage(), e);
						}
					}
				}
			});

			Iote2eIgniteCacheEntryEventSingleFilter<String, byte[]> sourceSensorCacheEntryEventFilter = new Iote2eIgniteCacheEntryEventSingleFilter<String, byte[]>(
					igniteFilterKey);
			qry.setRemoteFilterFactory(new Factory<Iote2eIgniteCacheEntryEventSingleFilter<String, byte[]>>() {
				@Override
				public Iote2eIgniteCacheEntryEventSingleFilter<String, byte[]> create() {
					return sourceSensorCacheEntryEventFilter;
					// return new SourceSensorCacheEntryEventFilter<String,
					// String>(remoteFilterKey);
				}
			});

			qry.setInitialQuery(new ScanQuery<>(new IgniteBiPredicate<String, byte[]>() {
				@Override
				public boolean apply(String key, byte[] val) {
					// TODO: recover forward from checkpoint
					return true;
				}
			}));

			subscribeUp = true;
			while (true) {
				try (QueryCursor<Cache.Entry<String, byte[]>> cur = cache.query(qry)) {
					try {
						Thread.sleep(500000);
					} catch (java.lang.InterruptedException e) {}
					if (shutdown) {
						logger.info("Shutdown subcribe start ");
						try {
							if (cache != null)
								cache.close();
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
						logger.info("Shutdown subcribe complete ");
						return;
					}
				}
			}

		} catch (javax.cache.CacheException e) {
			logger.error(e.getMessage(), e);
			if (e.getCause() instanceof org.apache.ignite.IgniteInterruptedException)
				logger.info("Stopping thread due to Ignite shutdown");
			else
				logger.error(e.getMessage(), e);
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
		//cache.close();
		//cache = null;
		// ignite.close();
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
	 * @return the eval ignite subscribe thread
	 */
	public EvalIgniteSubscribeThread setIgniteFilterKey(String igniteFilterKey) {
		this.igniteFilterKey = igniteFilterKey;
		return this;
	}

	/**
	 * Sets the shutdown.
	 *
	 * @param shutdown the shutdown
	 * @return the eval ignite subscribe thread
	 */
	public EvalIgniteSubscribeThread setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
		return this;
	}

	/**
	 * Sets the subscribe up.
	 *
	 * @param subscribeUp the subscribe up
	 * @return the eval ignite subscribe thread
	 */
	public EvalIgniteSubscribeThread setSubscribeUp(boolean subscribeUp) {
		this.subscribeUp = subscribeUp;
		return this;
	}

	/**
	 * Sets the queue iote 2 e results.
	 *
	 * @param iote2eResults the iote 2 e results
	 * @return the eval ignite subscribe thread
	 */
	public EvalIgniteSubscribeThread setQueueIote2eResults(ConcurrentLinkedQueue<Iote2eResult> iote2eResults) {
		this.queueIote2eResults = iote2eResults;
		return this;
	}

	/**
	 * Gets the poller thread.
	 *
	 * @return the poller thread
	 */
	public Thread getPollerThread() {
		return pollerThread;
	}

	/**
	 * Sets the poller thread.
	 *
	 * @param pollerThread the poller thread
	 * @return the eval ignite subscribe thread
	 */
	public EvalIgniteSubscribeThread setPollerThread(Thread pollerThread) {
		this.pollerThread = pollerThread;
		return this;
	}
}
