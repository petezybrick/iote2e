package com.pzybrick.iote2e.tests.omh;

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

@ClientEndpoint
@ServerEndpoint(value = "/omh/")
public class ClientSocketOmh {
	private static final Logger logger = LogManager.getLogger(ClientSocketOmh.class);
	
	public ClientSocketOmh( ) {
		logger.debug("ClientSocketOmh ctor empty");
	}
	

	@OnOpen
	public void onWebSocketConnect(Session session) {
		session.setMaxBinaryMessageBufferSize(1024 * 256); // 256K
		logger.debug("Socket Connected: " + session.getId());
	}

	@OnMessage
	public void onWebSocketText(String message) {
		//iote2eResultBytes.add(message);
		//iotClientSocketThread.interrupt();
	}

	@OnMessage
	public void onWebSocketText(byte[] messageByte) {
		logger.debug("rcvd byte message");
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		logger.info("Socket Closed: " + reason);
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		logger.error(cause);
	}

}