package com.pzybrick.iote2e.tests.omh;

import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.component.LifeCycle;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ws.security.LoginVo;

public class ClientSocketOmhHandler {
	private static final Logger logger = LogManager.getLogger(ClientSocketOmhHandler.class);
	private String url;
	private URI uri;
	private WebSocketContainer container;
	protected Session session;
	protected LoginVo loginVo;
	private static final Utf8 TEST_SOURCE_LOGIN = new Utf8("pzybrick1");


	
	public static void main(String[] args) {
		// "ws://localhost:8090/iote2e/"
		try {
			ClientSocketOmhHandler clientSocketOmhHandler = new ClientSocketOmhHandler();
			clientSocketOmhHandler.setUrl(args[0]);
			clientSocketOmhHandler.connect();
			clientSocketOmhHandler.wait();
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
	}

	public void connect() throws Exception {
		if( url == null || url.length() == 0 ) 
			throw new Exception("URL must be set before calling connect()");
		loginVo = new LoginVo().setLoginName(TEST_SOURCE_LOGIN.toString());
		try {
			uri = URI.create(url);
			container = ContainerProvider.getWebSocketContainer();

			try {
				ClientSocketOmh iotClientSocketAvro = new ClientSocketOmh( );
				session = container.connectToServer(iotClientSocketAvro, uri);
				session.getBasicRemote().sendText( Iote2eUtils.getGsonInstance().toJson(loginVo));
				logger.info("loginVo sent for {}", loginVo.getLoginName());
			} finally {
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
	
	public void shutdown() throws Exception {
		if (session != null && session.isOpen()) {
			try {
				session.close();
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
		// Force lifecycle stop when done with container.
		// This is to free up threads and resources that the
		// JSR-356 container allocates. But unfortunately
		// the JSR-356 spec does not handle lifecycles (yet)
		if (container instanceof LifeCycle) {
			((LifeCycle) container).stop();
		}
		try {

		} catch( Exception e ) {
			logger.warn(e.getMessage());
		}
		// only applies if called from main()
		try {
			notify();
		} catch(Exception e) {}
	}
	

	public LoginVo getLoginVo() {
		return loginVo;
	}

	public ClientSocketOmhHandler setLoginVo(LoginVo loginVo) {
		this.loginVo = loginVo;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public ClientSocketOmhHandler setUrl(String url) {
		this.url = url;
		return this;
	}

}