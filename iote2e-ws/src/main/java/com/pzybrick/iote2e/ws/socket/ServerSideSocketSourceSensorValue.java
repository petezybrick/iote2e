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

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.common.utils.IotE2eConstants;
import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;
import com.pzybrick.iote2e.schema.avro.SourceSensorValue;
import com.pzybrick.iotete.ws.security.IotE2eAuthentication;
import com.pzybrick.iotete.ws.security.IotE2eAuthentication.IotAuthenticationException;

@ClientEndpoint
@ServerEndpoint(value = "/iote2e/")
public class ServerSideSocketSourceSensorValue {
	private static final Log log = LogFactory.getLog(ServerSideSocketSourceSensorValue.class);
	private Session session;
	private boolean authenticated;
	private String login_uuid;
	private BinaryDecoder binaryDecoder = null;
	// private SourceSensorValue sourceSensorValue = null;
	private DatumReader<SourceSensorValue> datumReaderSourceSensorValue = new SpecificDatumReader<SourceSensorValue>(SourceSensorValue.getClassSchema());

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ServerSideSocketSourceSensorValue() {

	}

	@OnOpen
	public void onWebSocketConnect(Session session) {
		this.session = session;
		log.info("Socket Connected: " + session.getId());
	}

	@OnMessage
	public void onWebSocketText(String message) {
		log.debug("onWebSocketText " + message);
		if (authenticated) {
			log.error("Invalid message attempt - text instead of byte array - need to force close the socket");
			// TODO: force close on socket

		} else if (message.startsWith(IotE2eConstants.LOGIN_HDR)) {
			try {
				String login = message.substring(IotE2eConstants.LOGIN_HDR_LEN);
				login_uuid = IotE2eAuthentication.authenticate(login);
				authenticated = true;
				EntryPointServerSourceSensorValue.serverSideSocketSourceSensorValues.put(login_uuid, this);
			} catch (IotAuthenticationException e) {
				log.error(e.getMessage());
				// TODO: force close on socket
			} catch (Exception e) {
				log.error(e);
				// TODO: force close on socket
			}
		} else {
			log.error("Invalid login attempt - need to force close the socket");
			// TODO: force close on socket
		}
	}

	@OnMessage
	public void onWebSocketByte(byte[] messageByte) {
		log.debug("onWebSocketByte len=" + messageByte.length);
		if (authenticated) {
			try {
				binaryDecoder = DecoderFactory.get().binaryDecoder(messageByte, binaryDecoder);
				while (true) {
					SourceSensorValue sourceSensorValue = null;
					try {
						sourceSensorValue = datumReaderSourceSensorValue.read(null, binaryDecoder);
					} catch (EOFException e) {
						break; 
					}
					log.debug("sourceSensorValue: " + sourceSensorValue.toString());
					LoginSourceSensorValue loginSourceSensorValue = LoginSourceSensorValue.newBuilder()
							.setLoginUuid(login_uuid).setSourceUuid(sourceSensorValue.getSourceUuid())
							.setSensorName(sourceSensorValue.getSensorName()).setSensorValue(sourceSensorValue.getSensorValue())
							.build();
					EntryPointServerSourceSensorValue.fromClientLoginSourceSensorValues.add(loginSourceSensorValue);
				}
			} catch (Exception e) {
				log.info("Exception decoding SourceSensorValue, " + e);
			}

		} else {
			log.info("Invalid byte message, not logged in - need to force close the socket");
			// TODO: force close on socket
		}
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		boolean isRemove = EntryPointServerSourceSensorValue.serverSideSocketSourceSensorValues.remove(login_uuid, this);
		log.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = EntryPointServerSourceSensorValue.serverSideSocketSourceSensorValues.remove(login_uuid, this);
		log.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getLogin_uuid() {
		return login_uuid;
	}

	public void setLogin_uuid(String uuid) {
		this.login_uuid = uuid;
	}
}