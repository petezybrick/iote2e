package com.pzybrick.iote2e.ws.bdbb;

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

@ClientEndpoint
@ServerEndpoint(value = "/bdbb/")
public class ServerSideSocketBdbb {
	private static final Logger logger = LogManager.getLogger(ServerSideSocketBdbb.class);
	private Session session;
	private boolean authenticated;
	private String loginUuid;
	private LoginVo loginVo;
	private String keyCommon;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ServerSideSocketBdbb() {

	}

	@OnOpen
	public void onWebSocketConnect(Session session) {
		this.session = session;
		session.setMaxBinaryMessageBufferSize(1024 * 256); // 256K
		logger.info("Socket Connected: " + session.getId());
	}

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
					ThreadEntryPointBdbb.serverSideSocketByteBuffer.put(keyCommon, this);

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

	@OnMessage
	public void onWebSocketByte(byte[] bytes) {
		logger.debug("onWebSocketByte len=" + bytes.length);
		if (authenticated) {
			try {
				while (true) {
					logger.debug("bytes len: " + bytes.length );
					ThreadEntryPointBdbb.fromClientByteArrays.add(ByteBuffer.wrap(bytes));
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

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		boolean isRemove = ThreadEntryPointBdbb.serverSideSocketByteBuffer.remove(keyCommon, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = ThreadEntryPointBdbb.serverSideSocketByteBuffer.remove(keyCommon, this);
		logger.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
	}

	private void createkey() {
		StringBuilder sb = new StringBuilder(loginVo.getLoginName()).append("|").append(loginVo.getSourceName()).append("|");
		if( null != loginVo.getOptionalFilterSensorName() && loginVo.getOptionalFilterSensorName().length() > 0 )
			 sb.append(loginVo.getOptionalFilterSensorName()).append("|");
		keyCommon = sb.toString();
		logger.debug("keyCommon {}", keyCommon );
	}
	
	public boolean isAuthenticated() {
		return authenticated;
	}

	public String getLoginUuid() {
		return loginUuid;
	}


	public LoginVo getLoginVo() {
		return loginVo;
	}
}