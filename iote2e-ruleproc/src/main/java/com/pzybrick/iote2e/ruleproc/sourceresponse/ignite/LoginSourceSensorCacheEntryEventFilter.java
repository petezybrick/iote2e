package com.pzybrick.iote2e.ruleproc.sourceresponse.ignite;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;

public class LoginSourceSensorCacheEntryEventFilter<K,V> implements CacheEntryEventFilter<String, byte[]> {
	private String remoteKey;
	
	public LoginSourceSensorCacheEntryEventFilter( String remoteKey ) {
		this.remoteKey = remoteKey;
	}

	@Override
	public boolean evaluate(CacheEntryEvent<? extends String, ? extends byte[]> event) throws CacheEntryListenerException {
		if( event.getKey().startsWith(remoteKey)) return true; 
		else return false;
	}
}

