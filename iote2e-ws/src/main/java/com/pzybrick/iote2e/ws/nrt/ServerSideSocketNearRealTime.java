package com.pzybrick.iote2e.ws.nrt;

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

@ClientEndpoint
@ServerEndpoint(value = "/nrt/")
public class ServerSideSocketNearRealTime {
	private static final Logger logger = LogManager.getLogger(ServerSideSocketNearRealTime.class);
	public static final String NRT_IGNITE_KEY = "temperatureMonitor|";
	private Session session;
	private ThreadIgniteSubscribe threadIgniteSubscribe;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ServerSideSocketNearRealTime() {

	}

	@OnOpen
	public void onWebSocketConnect(Session session) throws Exception {
		this.session = session;
		EntryPointNearRealTime.serverSideSocketNearRealTimes.put(NRT_IGNITE_KEY, this);
		threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( EntryPointNearRealTime.masterConfig, NRT_IGNITE_KEY,
				EntryPointNearRealTime.toClientIote2eResults, (Thread)null );
		logger.info("Socket Connected: " + session.getId());
	}

	@OnMessage
	public void onWebSocketText(String message) {
		logger.debug("onWebSocketText " + message);
	}

	@OnMessage
	public void onWebSocketByte(byte[] bytes) {
		logger.debug("onWebSocketByte len=" + bytes.length);
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		boolean isRemove = EntryPointNearRealTime.serverSideSocketNearRealTimes.remove(NRT_IGNITE_KEY, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = EntryPointNearRealTime.serverSideSocketNearRealTimes.remove(NRT_IGNITE_KEY, this);
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

}