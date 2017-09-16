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
package com.pzybrick.iote2e.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The Class ArgMap.
 */
public class ArgMap implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6184707477942375505L;
	
	/** The map args. */
	private Map<String,String> mapArgs = new HashMap<String,String>();

	/**
	 * Instantiates a new arg map.
	 *
	 * @param args the args
	 * @throws Exception the exception
	 */
	public ArgMap(String[] args) throws Exception {
		if( args != null && args.length > 0 ) {
			for( String arg : args ) {
				int posEquals = arg.indexOf("=");
				if( posEquals == -1 ) throw new Exception("Missing equals sign on arg: " + arg );
				mapArgs.put(arg.substring(0,posEquals), arg.substring(posEquals+1));
			}
		}
	}
	
	/**
	 * Gets the.
	 *
	 * @param name the name
	 * @return the string
	 */
	public String get( String name ) {
		return mapArgs.get(name);
	}
	
	/**
	 * Gets the.
	 *
	 * @param name the name
	 * @param defaultValue the default value
	 * @return the string
	 */
	public String get( String name, String defaultValue ) {
		if( mapArgs.containsKey(name)) return mapArgs.get(name);
		else return defaultValue;		
	}
	
	/**
	 * Gets the integer.
	 *
	 * @param name the name
	 * @return the integer
	 */
	public Integer getInteger( String name ) {
		if( mapArgs.containsKey(name)) return new Integer(mapArgs.get(name));
		else return null;
	}
	
	/**
	 * Gets the integer.
	 *
	 * @param name the name
	 * @param defaultValue the default value
	 * @return the integer
	 */
	public Integer getInteger( String name, Integer defaultValue ) {
		if( mapArgs.containsKey(name)) return new Integer(mapArgs.get(name));
		else return defaultValue;	
	}
	
	/**
	 * Gets the long.
	 *
	 * @param name the name
	 * @return the long
	 */
	public Long getLong( String name ) {
		if( mapArgs.containsKey(name)) return new Long(mapArgs.get(name));
		else return null;
	}
	
	/**
	 * Gets the long.
	 *
	 * @param name the name
	 * @param defaultValue the default value
	 * @return the long
	 */
	public Long getLong( String name, Long defaultValue ) {
		if( mapArgs.containsKey(name)) return new Long(mapArgs.get(name));
		else return defaultValue;		
	}
	
	/**
	 * Gets the double.
	 *
	 * @param name the name
	 * @return the double
	 */
	public Double getDouble( String name ) {
		if( mapArgs.containsKey(name)) return new Double(mapArgs.get(name));
		else return null;
	}
	
	/**
	 * Gets the double.
	 *
	 * @param name the name
	 * @param defaultValue the default value
	 * @return the double
	 */
	public Double getDouble( String name, Double defaultValue ) {
		if( mapArgs.containsKey(name)) return new Double(mapArgs.get(name));
		else return defaultValue;	
	}

	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	public Map<String,String> getAll( ) {
		return mapArgs;
	}
	
	/**
	 * Dump.
	 *
	 * @return the string
	 */
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
