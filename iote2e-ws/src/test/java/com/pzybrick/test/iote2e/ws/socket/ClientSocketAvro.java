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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ws.socket.EntryPointIote2eRequest;

@ClientEndpoint
@ServerEndpoint(value = "/iote2e/")
public class ClientSocketAvro {
	private static final Logger logger = LogManager.getLogger(EntryPointIote2eRequest.class);
	private Thread iotClientSocketThread;
	private ConcurrentLinkedQueue<byte[]> iote2eResultBytes;
	
	public ClientSocketAvro( ) {
		logger.debug("IotClientSocketAvro ctor empty");
	}
	
	public ClientSocketAvro( Thread iotClientSocketThread, ConcurrentLinkedQueue<byte[]> iote2eResultBytes ) {
		logger.debug("IotClientSocketAvro ctor thread, queue");
		this.iotClientSocketThread = iotClientSocketThread;
		this.iote2eResultBytes = iote2eResultBytes;
	}

	@OnOpen
	public void onWebSocketConnect(Session session) {
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
		iote2eResultBytes.add( messageByte );
		iotClientSocketThread.interrupt();
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		logger.info("Socket Closed: " + reason);
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		logger.error(cause);
	}

	public ConcurrentLinkedQueue<byte[]> getRcvdAvroByteArrays() {
		return iote2eResultBytes;
	}

	public void setIote2eResultBytes(ConcurrentLinkedQueue<byte[]> iote2eResultBytes) {
		this.iote2eResultBytes = iote2eResultBytes;
	}
}