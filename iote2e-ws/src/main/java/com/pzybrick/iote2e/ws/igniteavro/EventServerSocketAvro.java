package com.pzybrick.iote2e.ws.igniteavro;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value = "/events/")
public class EventServerSocketAvro {
	private Session session;
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public EventServerSocketAvro() {
		System.out.println("EventSocket ctor");
	}

	@OnOpen
	public void onWebSocketConnect(Session session) {
		this.session = session;
		System.out.println("Socket Connected: " + session.getId());
		EventServerAvro.eventServerSocketAvros.put(session.getId(), this);
	}

	@OnMessage
	public void onWebSocketText(String message) {
		System.out.println("Received Serve TEXT message: " + message);
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		System.out.println("Socket Closed: " + reason);
		EventServerAvro.eventServerSocketAvros.remove(session.getId(), this);
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		cause.printStackTrace(System.err);
		EventServerAvro.eventServerSocketAvros.remove(session.getId(), this);
	}
}