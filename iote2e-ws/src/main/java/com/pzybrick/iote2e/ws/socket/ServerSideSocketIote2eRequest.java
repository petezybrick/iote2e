package com.pzybrick.iote2e.ws.socket;

import java.io.EOFException;

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

import com.pzybrick.iote2e.common.utils.Iote2eConstants;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iotete.ws.security.IotE2eAuthentication;
import com.pzybrick.iotete.ws.security.IotE2eAuthentication.IotAuthenticationException;

@ClientEndpoint
@ServerEndpoint(value = "/iote2e/")
public class ServerSideSocketIote2eRequest {
	private static final Logger logger = LogManager.getLogger(ServerSideSocketIote2eRequest.class);
	private Session session;
	private boolean authenticated;
	private String loginUuid;
	private String loginName;
	private Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ServerSideSocketIote2eRequest() {

	}

	@OnOpen
	public void onWebSocketConnect(Session session) {
		this.session = session;
		logger.info("Socket Connected: " + session.getId());
	}

	@OnMessage
	public void onWebSocketText(String message) {
		logger.debug("onWebSocketText " + message);
		if (authenticated) {
			logger.error("Invalid message attempt - text instead of byte array - need to force close the socket");
			// TODO: force close on socket

		} else if (message.startsWith(Iote2eConstants.LOGIN_HDR)) {
			try {
				loginName = message.substring(Iote2eConstants.LOGIN_HDR_LEN);
				loginUuid = IotE2eAuthentication.authenticate(loginName);
				authenticated = true;
				EntryPointIote2eRequest.serverSideSocketSourceSensorValues.put(loginName, this);
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
					EntryPointIote2eRequest.fromClientIote2eRequests.add(iote2eRequest);
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
		boolean isRemove = EntryPointIote2eRequest.serverSideSocketSourceSensorValues.remove(loginUuid, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = EntryPointIote2eRequest.serverSideSocketSourceSensorValues.remove(loginUuid, this);
		logger.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getLoginUuid() {
		return loginUuid;
	}

	public void setLoginUuid(String uuid) {
		this.loginUuid = uuid;
	}
}