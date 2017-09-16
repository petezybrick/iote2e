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
package com.pzybrick.iote2e.tests.bdbb;

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


/**
 * The Class ClientSocketBdbbHandler.
 */
public class ClientSocketBdbbHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ClientSocketBdbbHandler.class);
	
	/** The url. */
	private String url;
	
	/** The uri. */
	private URI uri;
	
	/** The container. */
	private WebSocketContainer container;
	
	/** The session. */
	protected Session session;
	
	/** The login vo. */
	protected LoginVo loginVo;
	
	/** The Constant TEST_SOURCE_LOGIN. */
	private static final Utf8 TEST_SOURCE_LOGIN = new Utf8("pzybrick1");


	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// "ws://localhost:8093/bdbb/"
		try {
			ClientSocketBdbbHandler clientSocketBdbbHandler = new ClientSocketBdbbHandler();
			clientSocketBdbbHandler.setUrl(args[0]);
			clientSocketBdbbHandler.connect();
			clientSocketBdbbHandler.wait();
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
	}

	/**
	 * Connect.
	 *
	 * @throws Exception the exception
	 */
	public void connect() throws Exception {
		if( url == null || url.length() == 0 ) 
			throw new Exception("URL must be set before calling connect()");
		loginVo = new LoginVo().setLoginName(TEST_SOURCE_LOGIN.toString());
		try {
			uri = URI.create(url);
			container = ContainerProvider.getWebSocketContainer();

			try {
				ClientSocketBdbb clientSocketBdbb = new ClientSocketBdbb( );
				session = container.connectToServer(clientSocketBdbb, uri);
				session.getBasicRemote().sendText( Iote2eUtils.getGsonInstance().toJson(loginVo));
				logger.info("loginVo sent for {}", loginVo.getLoginName());
			} finally {
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
	
	/**
	 * Shutdown.
	 *
	 * @throws Exception the exception
	 */
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
	

	/**
	 * Gets the login vo.
	 *
	 * @return the login vo
	 */
	public LoginVo getLoginVo() {
		return loginVo;
	}

	/**
	 * Sets the login vo.
	 *
	 * @param loginVo the login vo
	 * @return the client socket bdbb handler
	 */
	public ClientSocketBdbbHandler setLoginVo(LoginVo loginVo) {
		this.loginVo = loginVo;
		return this;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the url
	 * @return the client socket bdbb handler
	 */
	public ClientSocketBdbbHandler setUrl(String url) {
		this.url = url;
		return this;
	}

}