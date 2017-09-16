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
package com.pzybrick.iote2e.ws.ignite.web;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.server.ServerContainer;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.Iote2eConstants;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;


/**
 * The Class ThreadEntryPointWeb.
 */
public class ThreadEntryPointWeb extends Thread {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ThreadEntryPointWeb.class);
	
	/** The master config. */
	public static MasterConfig masterConfig;
	
	
	/**
	 * Instantiates a new thread entry point web.
	 */
	public ThreadEntryPointWeb( ) {
	}
	
	
	/**
	 * Instantiates a new thread entry point web.
	 *
	 * @param masterConfig the master config
	 */
	public ThreadEntryPointWeb( MasterConfig masterConfig ) {
		ThreadEntryPointWeb.masterConfig = masterConfig;
	}


	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run( ) {
		logger.info(masterConfig.toString());
		try {
			Server server = new Server(masterConfig.getWebServerPort());

			WebAppContext context = new WebAppContext();
			context.setDescriptor( masterConfig.getWebServerContentPath() + "/WEB-INF/web.xml" );
			context.setResourceBase( masterConfig.getWebServerContentPath() );
			context.setContextPath( masterConfig.getWebServerContextPath() );
			context.setParentLoaderPriority(true);
			server.setHandler(context);
			logger.info("Starting");
			server.start();
			logger.info("Started");
			server.join();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}

}