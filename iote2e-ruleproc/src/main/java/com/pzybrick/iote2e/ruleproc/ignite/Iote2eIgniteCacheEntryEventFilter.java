package com.pzybrick.iote2e.ruleproc.ignite;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;

public class Iote2eIgniteCacheEntryEventFilter<K,V> implements CacheEntryEventFilter<String, byte[]> {
	private String remoteKey;
	
	public Iote2eIgniteCacheEntryEventFilter( String remoteKey ) {
		this.remoteKey = remoteKey;
	}

	@Override
	public boolean evaluate(CacheEntryEvent<? extends String, ? extends byte[]> event) throws CacheEntryListenerException {
		if( event.getKey().startsWith(remoteKey)) return true; 
		else return false;
	}
}

