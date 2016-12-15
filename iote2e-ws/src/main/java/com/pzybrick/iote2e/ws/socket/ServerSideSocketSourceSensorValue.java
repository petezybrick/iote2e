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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pzybrick.iote2e.common.utils.Iote2eConstants;
import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;
import com.pzybrick.iote2e.schema.avro.SourceSensorValue;
import com.pzybrick.iotete.ws.security.IotE2eAuthentication;
import com.pzybrick.iotete.ws.security.IotE2eAuthentication.IotAuthenticationException;

@ClientEndpoint
@ServerEndpoint(value = "/iote2e/")
public class ServerSideSocketSourceSensorValue {
	private static final Logger logger = LogManager.getLogger(ServerSideSocketSourceSensorValue.class);
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
				String login = message.substring(Iote2eConstants.LOGIN_HDR_LEN);
				login_uuid = IotE2eAuthentication.authenticate(login);
				authenticated = true;
				EntryPointServerSourceSensorValue.serverSideSocketSourceSensorValues.put(login_uuid, this);
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
	public void onWebSocketByte(byte[] messageByte) {
		logger.debug("onWebSocketByte len=" + messageByte.length);
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
					logger.debug("sourceSensorValue: " + sourceSensorValue.toString());
					LoginSourceSensorValue loginSourceSensorValue = LoginSourceSensorValue.newBuilder()
							.setLoginUuid(login_uuid).setSourceUuid(sourceSensorValue.getSourceUuid())
							.setSensorName(sourceSensorValue.getSensorName()).setSensorValue(sourceSensorValue.getSensorValue())
							.build();
					EntryPointServerSourceSensorValue.fromClientLoginSourceSensorValues.add(loginSourceSensorValue);
				}
			} catch (Exception e) {
				logger.info("Exception decoding SourceSensorValue, " + e);
			}

		} else {
			logger.info("Invalid byte message, not logged in - need to force close the socket");
			// TODO: force close on socket
		}
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		boolean isRemove = EntryPointServerSourceSensorValue.serverSideSocketSourceSensorValues.remove(login_uuid, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = EntryPointServerSourceSensorValue.serverSideSocketSourceSensorValues.remove(login_uuid, this);
		logger.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
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