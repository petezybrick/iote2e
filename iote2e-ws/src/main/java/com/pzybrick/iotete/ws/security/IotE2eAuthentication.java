package com.pzybrick.iotete.ws.security;

import java.util.HashMap;
import java.util.Map;

public class IotE2eAuthentication {
	private static final Map<String,String> mapLoginToUuid;
	
	static {
		mapLoginToUuid = new HashMap<String,String>();
		mapLoginToUuid.put("test0001", "a1-a1-a1-a1");
		mapLoginToUuid.put("test0002", "b1-b1-b1-b1");
		mapLoginToUuid.put("test0003", "c1-c1-c1-c1");
		mapLoginToUuid.put("test0004", "d1-d1-d1-d1");
		mapLoginToUuid.put("test0005", "e1-e1-e1-e1");
		mapLoginToUuid.put("test0006", "f1-f1-f1-f1");
		mapLoginToUuid.put("test0007", "g1-g1-g1-g1");
		mapLoginToUuid.put("test0008", "h1-h1-h1-h1");
	}
	
	public static String authenticate( String login ) throws Exception {
		if( mapLoginToUuid.containsKey(login) ) return mapLoginToUuid.get(login);
		else throw new IotAuthenticationException("Login and/or password invalid");
	}
	
	public static class IotAuthenticationException extends Exception {

		public IotAuthenticationException() {
			super();
		}

		public IotAuthenticationException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		public IotAuthenticationException(String message, Throwable cause) {
			super(message, cause);
		}

		public IotAuthenticationException(String message) {
			super(message);
		}

		public IotAuthenticationException(Throwable cause) {
			super(cause);
		}
		
	}
}
