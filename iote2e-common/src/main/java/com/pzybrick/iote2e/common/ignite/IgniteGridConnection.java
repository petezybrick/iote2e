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
 * The Class IgniteGridConnection.
 */
public class IgniteGridConnection {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ThreadIgniteSubscribe.class);
	
	/** The cache. */
	private IgniteCache<String, byte[]> cache = null;
	
	/** The ignite. */
	private Ignite ignite = null;
	
	/**
	 * Connect.
	 *
	 * @param masterConfig the master config
	 * @return the ignite grid connection
	 * @throws Exception the exception
	 */
	public IgniteGridConnection connect( MasterConfig masterConfig) throws Exception {
		try {
			String igniteConfigPath = masterConfig.getIgniteConfigPath();
			if( igniteConfigPath == null ) throw new Exception("Required MasterConfig value igniteConfigPath is not set, try setting to location of ignite-iote2e.xml");
			if( !igniteConfigPath.endsWith("/") ) igniteConfigPath = igniteConfigPath + "/";
			String igniteConfigPathNameExt = igniteConfigPath + masterConfig.getIgniteConfigFile();
			logger.info("Initializing Ignite, config file=" + igniteConfigPathNameExt + ", config name=" +  masterConfig.getIgniteConfigName());
			IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(
					igniteConfigPathNameExt, masterConfig.getIgniteConfigName());
			Ignition.setClientMode(masterConfig.isIgniteClientMode());
			ignite = Ignition.getOrStart(igniteConfiguration);
			cache = ignite.getOrCreateCache(masterConfig.getIgniteCacheName());	
			logger.debug("***************** create cache: cacheName={}, cache={}", masterConfig.getIgniteCacheName(), cache);
			return this;
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Gets the cache.
	 *
	 * @return the cache
	 */
	public IgniteCache<String, byte[]> getCache() {
		return cache;
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
	 * Sets the cache.
	 *
	 * @param cache the cache
	 * @return the ignite grid connection
	 */
	public IgniteGridConnection setCache(IgniteCache<String, byte[]> cache) {
		this.cache = cache;
		return this;
	}
	
	/**
	 * Sets the ignite.
	 *
	 * @param ignite the ignite
	 * @return the ignite grid connection
	 */
	public IgniteGridConnection setIgnite(Ignite ignite) {
		this.ignite = ignite;
		return this;
	}	
}
