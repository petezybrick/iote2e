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
package com.pzybrick.iote2e.ws.socket;

import java.io.EOFException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.ws.security.IotE2eAuthentication;
import com.pzybrick.iote2e.ws.security.LoginVo;
import com.pzybrick.iote2e.ws.security.IotE2eAuthentication.IotAuthenticationException;


/**
 * The Class ServerSideSocketIote2eRequest.
 */
@ClientEndpoint
@ServerEndpoint(value = "/iote2e/")
public class ServerSideSocketIote2eRequest {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ServerSideSocketIote2eRequest.class);
	
	/** The session. */
	private Session session;
	
	/** The authenticated. */
	private boolean authenticated;
	
	/** The login uuid. */
	private String loginUuid;
	
	/** The login vo. */
	private LoginVo loginVo;
	
	/** The iote 2 e request reuse item. */
	private Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
	
	/** The key common. */
	private String keyCommon;
	
	/** The thread ignite subscribe. */
	private ThreadIgniteSubscribe threadIgniteSubscribe;

	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Sets the session.
	 *
	 * @param session the new session
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Instantiates a new server side socket iote 2 e request.
	 */
	public ServerSideSocketIote2eRequest() {

	}

	/**
	 * On web socket connect.
	 *
	 * @param session the session
	 */
	@OnOpen
	public void onWebSocketConnect(Session session) {
		this.session = session;
		session.setMaxBinaryMessageBufferSize(1024 * 256); // 256K
		logger.info("Socket Connected: " + session.getId());
	}

	/**
	 * On web socket text.
	 *
	 * @param message the message
	 */
	@OnMessage
	public void onWebSocketText(String message) {
		logger.debug("onWebSocketText " + message);
		if (authenticated) {
			logger.error("Invalid message attempt - text instead of byte array - need to force close the socket");
			// TODO: force close on socket

		} else {
			loginVo = null;
			try {
				loginVo = Iote2eUtils.getGsonInstance().fromJson(message,  LoginVo.class );
			} catch( Exception e ) {
				logger.error( "Invalid JSON on login attempt" );
			}
			if( loginVo != null ) {
				try {
					loginUuid = IotE2eAuthentication.authenticate(loginVo.getLoginName());
					authenticated = true;
					// The keyCommon attribute is used to identify this socket connection on the map of socket connections,
					// as well key used when creating the Ignite remote filter 
					// There are two possible key structures:
					//   1. login|sourceName
					//		1.1 This implies that the code running on the sourceName device is a single program managing multiple sensors,
					//			the code on the sensor will look at the returned Iote2eResult's and decide how to route the response.
					//		1.2 for example, if the filter is pzybrick1|rpi_001, and the device has two sensors, i.e. temp1 and humidity1, 
					//			then all Iote2eResult's that are keyed with pzybrick1|rpi_001|temp1 and pzybrick1|rpi_001|humidity1 will be
					//			returned to the source system (i.e. RPi #1) over the same socket, and the program listening on that socket (i.e. 
					//			a Python program running on an RPi) will need to look at the sensor name in each Iote2eResult and decide what to do with it
					//		1.3 This key structure is used when the attribute LoginVo.optionalFilterSensorName is null or an empty string
					//	2. login|sourceName|sensorName
					//		2.1 This implies that the code running on the sourceName device is a single program managing a single sensor,
					//			the code on the sensor will look at the returned Iote2eResult's and decide how to route the response.
					//		2.2 for example, if the filter is pzybrick1|rpi_001|temp1, then all Iote2eResult's that are keyed with 
					//			pzybrick1|rpi_001|temp1 will be returned to the source system (i.e. RPi #1) over the same socket, 
					// 			and the program listening on that socket (i.e. a Python program running on an RPi) will 
					//			not need to look at the sensor name in each Iote2eResult because it will always be temp1
					//		2.3 This key structure is used when the attribute LoginVo.optionalFilterSensorName is not null and not an empty string
					createkey();
					ThreadEntryPointIote2eRequest.serverSideSocketIote2eRequest.put(keyCommon, this);
					threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( ThreadEntryPointIote2eRequest.masterConfig, keyCommon,
							ThreadEntryPointIote2eRequest.toClientIote2eResults, (Thread)null );

				} catch (IotAuthenticationException e) {
					logger.error(e.getMessage());
					// TODO: force close on socket
				} catch (Exception e) {
					logger.error(e);
					// TODO: force close on socket
				}
			} else {
				logger.error("Invalid login attempt - need to force close the socket");
				// TODO: force close on socket
			}
		}
	}

	/**
	 * On web socket byte.
	 *
	 * @param bytes the bytes
	 */
	@OnMessage
	public void onWebSocketByte(byte[] bytes) {
		logger.debug("onWebSocketByte len=" + bytes.length);
		if (authenticated) {
			try {
				while (true) {
					Iote2eRequest iote2eRequest = null;
					try {
						iote2eRequest = iote2eRequestReuseItem.fromByteArray(bytes);
					} catch (EOFException e) {
						break; 
					}
					logger.debug("iote2eRequest: " + iote2eRequest.toString());
					ThreadEntryPointIote2eRequest.fromClientIote2eRequests.add(iote2eRequest);
					break;
				}
			} catch (Exception e) {
				logger.error("Exception decoding Iote2eRequest: {}", e.getMessage(), e);
			}

		} else {
			logger.info("Invalid byte message, not logged in - need to force close the socket");
			// TODO: force close on socket
		}
	}

	/**
	 * On web socket close.
	 *
	 * @param reason the reason
	 */
	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		boolean isRemove = ThreadEntryPointIote2eRequest.serverSideSocketIote2eRequest.remove(keyCommon, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}

	/**
	 * On web socket error.
	 *
	 * @param cause the cause
	 */
	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = ThreadEntryPointIote2eRequest.serverSideSocketIote2eRequest.remove(keyCommon, this);
		logger.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}
	
	/**
	 * Shutdown thread ignite subscribe.
	 */
	private void shutdownThreadIgniteSubscribe() {
		logger.debug("Shutting down threadIgniteSubscribe");
		try {
			threadIgniteSubscribe.shutdown();
			threadIgniteSubscribe.join(5000);
		} catch( InterruptedException e ) {
		} catch( Exception e ) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Createkey.
	 */
	private void createkey() {
		StringBuilder sb = new StringBuilder(loginVo.getLoginName()).append("|").append(loginVo.getSourceName()).append("|");
		if( null != loginVo.getOptionalFilterSensorName() && loginVo.getOptionalFilterSensorName().length() > 0 )
			 sb.append(loginVo.getOptionalFilterSensorName()).append("|");
		keyCommon = sb.toString();
		logger.debug("keyCommon {}", keyCommon );
	}
	
	/**
	 * Checks if is authenticated.
	 *
	 * @return true, if is authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * Gets the login uuid.
	 *
	 * @return the login uuid
	 */
	public String getLoginUuid() {
		return loginUuid;
	}


	/**
	 * Gets the login vo.
	 *
	 * @return the login vo
	 */
	public LoginVo getLoginVo() {
		return loginVo;
	}
}