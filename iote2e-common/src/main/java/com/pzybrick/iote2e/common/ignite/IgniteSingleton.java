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

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;


/**
 * The Class IgniteSingleton.
 */
public class IgniteSingleton {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(IgniteSingleton.class);
	
	/** The ignite singleton. */
	private static IgniteSingleton igniteSingleton;
	
	/** The ignite. */
	private Ignite ignite;
	
	/** The cache. */
	private IgniteCache<String, byte[]> cache;
	
	/**
	 * Instantiates a new ignite singleton.
	 *
	 * @param ignite the ignite
	 * @param cache the cache
	 */
	private IgniteSingleton(Ignite ignite, IgniteCache<String, byte[]> cache) {
		this.ignite = ignite;
		this.cache = cache;
	}
	
	/**
	 * Reset.
	 *
	 * @throws Exception the exception
	 */
	public static synchronized void reset( ) throws Exception {
		logger.info("reset called");
		if( igniteSingleton != null ) {
			try {
				igniteSingleton.getCache().close();
				long expiresAt = System.currentTimeMillis() + (10*1000);
				while( expiresAt > System.currentTimeMillis()) {
					if( igniteSingleton.getCache().isClosed() ) break;
					Thread.sleep(250);
				}
				if( !igniteSingleton.getCache().isClosed() ) throw new Exception("Failed to close Ignite cache");
			} catch( Exception e ) {
				logger.warn(e.getMessage(),e);
			}			
			try {
				igniteSingleton.getIgnite().close();
				logger.info("reset: before Ignition.kill");
				//Ignition.kill(false);
				logger.info("reset: before Ignition.kill");
			} catch( Exception e ) {
				logger.warn(e.getMessage(),e);
			}
			igniteSingleton = null;
		}
	}
		
	/**
	 * Gets the single instance of IgniteSingleton.
	 *
	 * @param masterConfig the master config
	 * @return single instance of IgniteSingleton
	 * @throws Exception the exception
	 */
	public static synchronized IgniteSingleton getInstance( MasterConfig masterConfig ) throws Exception {
		if( System.currentTimeMillis() > 0 ) throw new Exception("DEPRECATED: IgniteSingleton.getInstance()");
		if( igniteSingleton == null ) {
			Throwable lastThrowable = null;
			long retryMs = 1000;
			for( int i=0 ; i<10 ; i++ ) {
				try {
					logger.info("attempting getInstance, attempt number: {}", i);
					String igniteConfigPath = masterConfig.getIgniteConfigPath();
					if( igniteConfigPath == null ) throw new Exception("Required MasterConfig value igniteConfigPath is not set, try setting to location of ignite-iote2e.xml");
					if( !igniteConfigPath.endsWith("/") ) igniteConfigPath = igniteConfigPath + "/";
					String igniteConfigPathNameExt = igniteConfigPath + masterConfig.getIgniteConfigFile();
					logger.info("Initializing Ignite, config file=" + igniteConfigPathNameExt + ", config name=" +  masterConfig.getIgniteConfigName());
					IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(
							igniteConfigPathNameExt, masterConfig.getIgniteConfigName());
					Ignition.setClientMode(masterConfig.isIgniteClientMode());
					Ignite ignite = Ignition.start(igniteConfiguration);
					if (logger.isDebugEnabled()) logger.debug(ignite.toString());
					IgniteCache<String, byte[]> cache = ignite.getOrCreateCache(masterConfig.getIgniteCacheName());
					igniteSingleton = new IgniteSingleton( ignite, cache);
					logger.info("successful getInstance, attempt number: {}", i);
					break;
				} catch (Throwable t ) {
					logger.warn("Ignite initialization failure", t);
					lastThrowable = t;
					logger.info("getInstance: before Ignition.kill");
					//Ignition.kill(false);
					logger.info("getInstance: after Ignition.kill");
				}
				try { Thread.sleep(retryMs); } catch(Exception e ) {}
				retryMs = retryMs*2;
			}
			if( lastThrowable != null ) throw new Exception(lastThrowable);
		}
		return igniteSingleton;
	}

	/**
	 * Gets the ignite.
	 *
	 * @return the ignite
	 */
	public Ignite getIgnite() {
		return ignite;
	}

	/**
	 * Gets the cache.
	 *
	 * @return the cache
	 */
	public IgniteCache<String, byte[]> getCache() {
		return cache;
	}
	
}
