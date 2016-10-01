package com.pzybrick.test.iote2e.ws.socket;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.ws.socket.EntryPointServerSourceSensorValue;

@ClientEndpoint
@ServerEndpoint(value = "/e2e/")
public class ClientSocketAvro {
	private static final Log log = LogFactory.getLog(EntryPointServerSourceSensorValue.class);
	private Thread iotClientSocketThread;
	private ConcurrentLinkedQueue<String> rcvdMessages;
	
	public ClientSocketAvro( ) {
		log.debug("IotClientSocketAvro ctor empty");
	}
	
	public ClientSocketAvro( Thread iotClientSocketThread, ConcurrentLinkedQueue<String> rcvdMessages ) {
		log.debug("IotClientSocketAvro ctor thread, queue");
		this.iotClientSocketThread = iotClientSocketThread;
		this.rcvdMessages = rcvdMessages;
	}

	@OnOpen
	public void onWebSocketConnect(Session session) {
		log.debug("Socket Connected: " + session.getId());
	}

	@OnMessage
	public void onWebSocketText(String message) {
		rcvdMessages.add(message);
		iotClientSocketThread.interrupt();
	}

	@OnMessage
	public void onWebSocketText(byte[] messageByte) {
		log.debug("rcvd byte message");
		rcvdMessages.add( new String(messageByte) );
		iotClientSocketThread.interrupt();
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		log.info("Socket Closed: " + reason);
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		log.error(cause);
	}

	public ConcurrentLinkedQueue<String> getRcvdMessages() {
		return rcvdMessages;
	}

	public void setRcvdMessages(ConcurrentLinkedQueue<String> rcvdMessages) {
		this.rcvdMessages = rcvdMessages;
	}
}