package com.pzybrick.iote2e.ws.security;

import java.util.HashMap;
import java.util.Map;

public class IotE2eAuthentication {
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
	
	public static String authenticate( String login ) throws Exception {
		if( mapLoginToUuid.containsKey(login) ) return mapLoginToUuid.get(login);
		else throw new IotAuthenticationException("Login and/or password invalid");
	}
	
	public static class IotAuthenticationException extends Exception {
		private static final long serialVersionUID = 1L;

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
