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

import com.pzybrick.iote2e.common.ignite.IgniteSingleton;
import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.ws.security.LoginVo;
import com.pzybrick.iotete.ws.security.IotE2eAuthentication;
import com.pzybrick.iotete.ws.security.IotE2eAuthentication.IotAuthenticationException;

@ClientEndpoint
@ServerEndpoint(value = "/iote2e/")
public class ServerSideSocketIote2eRequest {
	private static final Logger logger = LogManager.getLogger(ServerSideSocketIote2eRequest.class);
	private Session session;
	private boolean authenticated;
	private String loginUuid;
	private LoginVo loginVo;
	private Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
	private String keyCommon;
	private ThreadIgniteSubscribe threadIgniteSubscribe;

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

		} else {
			loginVo = null;
			try {
				loginVo = Iote2eUtils.getGsonInstance().fromJson(message,  LoginVo.class );
			} catch( Exception e ) {
				logger.error( "Invalid JSON on login attempt" );
			}
			if( loginVo != null ) {
				try {
					loginUuid = IotE2eAuthentication.authenticate(loginVo.getLogin());
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
					//	2. login|soruceName|sensorName
					//		2.1 This implies that the code running on the sourceName device is a single program managing a single sensor,
					//			the code on the sensor will look at the returned Iote2eResult's and decide how to route the response.
					//		2.2 for example, if the filter is pzybrick1|rpi_001|temp1, then all Iote2eResult's that are keyed with 
					//			pzybrick1|rpi_001|temp1 will be returned to the source system (i.e. RPi #1) over the same socket, 
					// 			and the program listening on that socket (i.e. a Python program running on an RPi) will 
					//			not need to look at the sensor name in each Iote2eResult because it will always be temp1
					//		2.3 This key structure is used when the attribute LoginVo.optionalFilterSensorName is not null and not an empty string
					createkey();
					EntryPointIote2eRequest.serverSideSocketSourceSensorValues.put(keyCommon, this);
					threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe(keyCommon,
							EntryPointIote2eRequest.igniteSingleton, EntryPointIote2eRequest.toClientIote2eResults, (Thread)null );

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
		boolean isRemove = EntryPointIote2eRequest.serverSideSocketSourceSensorValues.remove(keyCommon, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = EntryPointIote2eRequest.serverSideSocketSourceSensorValues.remove(keyCommon, this);
		logger.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}
	
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

	private void createkey() {
		StringBuilder sb = new StringBuilder(loginVo.getLogin()).append("|").append(loginVo.getSourceName());
		if( null != loginVo.getOptionalFilterSensorName() && loginVo.getOptionalFilterSensorName().length() > 0 )
			 sb.append("|").append(loginVo.getOptionalFilterSensorName());
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