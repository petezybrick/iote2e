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

import java.io.Serializable;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;


/**
 * The Class Iote2eIgniteCacheEntryEventSingleFilter.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class Iote2eIgniteCacheEntryEventSingleFilter<K,V> implements CacheEntryEventFilter<String, byte[]>, Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 882712943322969160L;
	
	/** The remote key. */
	private String remoteKey;
	
	/**
	 * Instantiates a new iote 2 e ignite cache entry event single filter.
	 *
	 * @param remoteKey the remote key
	 */
	public Iote2eIgniteCacheEntryEventSingleFilter( String remoteKey ) {
		this.remoteKey = remoteKey;
	}

	/* (non-Javadoc)
	 * @see javax.cache.event.CacheEntryEventFilter#evaluate(javax.cache.event.CacheEntryEvent)
	 */
	@Override
	public boolean evaluate(CacheEntryEvent<? extends String, ? extends byte[]> event) throws CacheEntryListenerException {
		if( event.getKey().startsWith(remoteKey)) return true; 
		else return false;
	}
}

