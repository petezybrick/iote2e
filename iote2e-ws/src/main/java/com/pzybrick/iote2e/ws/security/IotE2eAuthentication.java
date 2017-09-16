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
package com.pzybrick.iote2e.ws.security;

import java.util.HashMap;
import java.util.Map;


/**
 * The Class IotE2eAuthentication.
 */
public class IotE2eAuthentication {
	
	/** The Constant mapLoginToUuid. */
	private static final Map<String,String> mapLoginToUuid;
	
	static {
		mapLoginToUuid = new HashMap<String,String>();
		mapLoginToUuid.put("pzybrick1", "pz1-pz1-pz1-pz1");
		mapLoginToUuid.put("jdoe2", "jd1-jd1-jd1-jd1");
		mapLoginToUuid.put("sjones3", "sj1-sj1-sj1-sj1");
		mapLoginToUuid.put("test0001", "t1-t1-t1-t1");
		mapLoginToUuid.put("test0002", "t2-t2-t2-t2");
		mapLoginToUuid.put("test0003", "t3-t3-t3-t3");
		mapLoginToUuid.put("test0004", "t4-t4-t4-t4");
		mapLoginToUuid.put("test0005", "t5-t5-t5-t5");
		mapLoginToUuid.put("test0006", "t6-t6-t6-t6");
		mapLoginToUuid.put("test0007", "t7-t7-t7-t7");
		mapLoginToUuid.put("test0008", "t8-t8-t8-t8");
	}
	
	/**
	 * Authenticate.
	 *
	 * @param login the login
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String authenticate( String login ) throws Exception {
		if( mapLoginToUuid.containsKey(login) ) return mapLoginToUuid.get(login);
		else throw new IotAuthenticationException("Login and/or password invalid");
	}
	
	/**
	 * The Class IotAuthenticationException.
	 */
	public static class IotAuthenticationException extends Exception {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new iot authentication exception.
		 */
		public IotAuthenticationException() {
			super();
		}

		/**
		 * Instantiates a new iot authentication exception.
		 *
		 * @param message the message
		 * @param cause the cause
		 * @param enableSuppression the enable suppression
		 * @param writableStackTrace the writable stack trace
		 */
		public IotAuthenticationException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		/**
		 * Instantiates a new iot authentication exception.
		 *
		 * @param message the message
		 * @param cause the cause
		 */
		public IotAuthenticationException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * Instantiates a new iot authentication exception.
		 *
		 * @param message the message
		 */
		public IotAuthenticationException(String message) {
			super(message);
		}

		/**
		 * Instantiates a new iot authentication exception.
		 *
		 * @param cause the cause
		 */
		public IotAuthenticationException(Throwable cause) {
			super(cause);
		}
		
	}
}
