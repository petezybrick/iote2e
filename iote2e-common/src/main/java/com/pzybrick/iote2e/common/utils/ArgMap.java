package com.pzybrick.iote2e.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgMap implements Serializable {
	private static final long serialVersionUID = 6184707477942375505L;
	private Map<String,String> mapArgs = new HashMap<String,String>();

	public ArgMap(String[] args) throws Exception {
		if( args != null && args.length > 0 ) {
			for( String arg : args ) {
				int posEquals = arg.indexOf("=");
				if( posEquals == -1 ) throw new Exception("Missing equals sign on arg: " + arg );
				mapArgs.put(arg.substring(0,posEquals), arg.substring(posEquals+1));
			}
		}
	}
	
	public String get( String name ) {
		return mapArgs.get(name);
	}
	
	public String get( String name, String defaultValue ) {
		if( mapArgs.containsKey(name)) return mapArgs.get(name);
		else return defaultValue;		
	}
	
	public Integer getInteger( String name ) {
		if( mapArgs.containsKey(name)) return new Integer(mapArgs.get(name));
		else return null;
	}
	
	public Integer getInteger( String name, Integer defaultValue ) {
		if( mapArgs.containsKey(name)) return new Integer(mapArgs.get(name));
		else return defaultValue;	
	}
	
	public Long getLong( String name ) {
		if( mapArgs.containsKey(name)) return new Long(mapArgs.get(name));
		else return null;
	}
	
	public Long getLong( String name, Long defaultValue ) {
		if( mapArgs.containsKey(name)) return new Long(mapArgs.get(name));
		else return defaultValue;		
	}
	
	public Double getDouble( String name ) {
		if( mapArgs.containsKey(name)) return new Double(mapArgs.get(name));
		else return null;
	}
	
	public Double getDouble( String name, Double defaultValue ) {
		if( mapArgs.containsKey(name)) return new Double(mapArgs.get(name));
		else return defaultValue;	
	}

	public Map<String,String> getAll( ) {
		return mapArgs;
	}
	
	public String dump( ) {
		List<String> keys = new ArrayList<String>(mapArgs.keySet());
		Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		for( String key : keys ) {
			if( sb.length() > 0 ) sb.append("\n");
			sb.append(key).append(" = ").append(mapArgs.get(key));
		}
		return sb.toString();
	}
}
