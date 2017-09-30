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
package com.pzybrick.iote2e.ws.validic;

import java.nio.ByteBuffer;

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

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ws.security.IotE2eAuthentication;
import com.pzybrick.iote2e.ws.security.IotE2eAuthentication.IotAuthenticationException;
import com.pzybrick.iote2e.ws.security.LoginVo;


/**
 * The Class ServerSideSocketOmh.
 */
@ClientEndpoint
@ServerEndpoint(value = "/validic/")
public class ServerSideSocketValidic {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ServerSideSocketValidic.class);
	
	/** The session. */
	private Session session;
	
	/** The authenticated. */
	private boolean authenticated;
	
	/** The login uuid. */
	private String loginUuid;
	
	/** The login vo. */
	private LoginVo loginVo;
	
	/** The key common. */
	private String keyCommon;

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
	 * Instantiates a new server side socket omh.
	 */
	public ServerSideSocketValidic() {

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
					createkey();
					ThreadEntryPointValidic.serverSideSocketByteBuffer.put(keyCommon, this);

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
					logger.debug("bytes len: " + bytes.length );
					ThreadEntryPointValidic.fromClientByteArrays.add(ByteBuffer.wrap(bytes));
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
		boolean isRemove = ThreadEntryPointValidic.serverSideSocketByteBuffer.remove(keyCommon, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
	}

	/**
	 * On web socket error.
	 *
	 * @param cause the cause
	 */
	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = ThreadEntryPointValidic.serverSideSocketByteBuffer.remove(keyCommon, this);
		logger.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
	}

	/**
	 * Createkey.
	 */
	private void createkey() {
		StringBuilder sb = new StringBuilder(loginVo.getLoginName()).append("|");
		if( null != loginVo.getSourceName() && loginVo.getSourceName().length() > 0 )
			sb.append(loginVo.getSourceName()).append("|");
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