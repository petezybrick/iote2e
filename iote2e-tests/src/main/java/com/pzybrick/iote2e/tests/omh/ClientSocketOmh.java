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


/**
 * The Class ClientSocketOmh.
 */
@ClientEndpoint
@ServerEndpoint(value = "/omh/")
public class ClientSocketOmh {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ClientSocketOmh.class);
	
	/**
	 * Instantiates a new client socket omh.
	 */
	public ClientSocketOmh( ) {
		logger.debug("ClientSocketOmh ctor empty");
	}
	

	/**
	 * On web socket connect.
	 *
	 * @param session the session
	 */
	@OnOpen
	public void onWebSocketConnect(Session session) {
		session.setMaxBinaryMessageBufferSize(1024 * 256); // 256K
		logger.debug("Socket Connected: " + session.getId());
	}

	/**
	 * On web socket text.
	 *
	 * @param message the message
	 */
	@OnMessage
	public void onWebSocketText(String message) {
		//iote2eResultBytes.add(message);
		//iotClientSocketThread.interrupt();
	}

	/**
	 * On web socket text.
	 *
	 * @param messageByte the message byte
	 */
	@OnMessage
	public void onWebSocketText(byte[] messageByte) {
		logger.debug("rcvd byte message");
	}

	/**
	 * On web socket close.
	 *
	 * @param reason the reason
	 */
	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		logger.info("Socket Closed: " + reason);
	}

	/**
	 * On web socket error.
	 *
	 * @param cause the cause
	 */
	@OnError
	public void onWebSocketError(Throwable cause) {
		logger.error(cause);
	}

}