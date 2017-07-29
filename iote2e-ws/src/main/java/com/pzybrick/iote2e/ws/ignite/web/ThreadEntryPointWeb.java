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

public class ThreadEntryPointWeb extends Thread {
	private static final Logger logger = LogManager.getLogger(ThreadEntryPointWeb.class);
	public static MasterConfig masterConfig;
	
	
	public ThreadEntryPointWeb( ) {
	}
	
	
	public ThreadEntryPointWeb( MasterConfig masterConfig ) {
		ThreadEntryPointWeb.masterConfig = masterConfig;
	}


	public void run( ) {
		logger.info(masterConfig.toString());
		try {
			Server server = new Server(masterConfig.getWebServerPort());

			WebAppContext context = new WebAppContext();
			context.setDescriptor("../iote2e-ws/WebContent/WEB-INF/web.xml");
			context.setResourceBase("../iote2e-ws/WebContent");
			context.setContextPath( masterConfig.getWebServerContextPath() );
			context.setParentLoaderPriority(true);
			server.setHandler(context);
			server.start();
			server.join();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}

}