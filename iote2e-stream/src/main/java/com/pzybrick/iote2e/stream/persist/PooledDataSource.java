package com.pzybrick.iote2e.stream.persist;

import java.sql.Connection;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;

public class PooledDataSource {
	private static final Logger logger = LogManager.getLogger(PooledDataSource.class);
	private static PooledDataSource pooledDataSource;
	private BasicDataSource bds;
	
	
	private PooledDataSource( MasterConfig masterConfig ) throws Exception {
		logger.debug("JDBC: login {}, class {}, url {}", masterConfig.getJdbcLogin(), masterConfig.getJdbcDriverClassName(), masterConfig.getJdbcUrl());
		bds = new BasicDataSource();
		bds.setDriverClassName(masterConfig.getJdbcDriverClassName());
		bds.setUrl(masterConfig.getJdbcUrl());
		bds.setUsername(masterConfig.getJdbcLogin());
		bds.setPassword(masterConfig.getJdbcPassword());
		// Optimize for bulk inserts
		bds.setDefaultAutoCommit(false);
	}
		
		
	public static PooledDataSource getInstance( MasterConfig masterConfig ) throws Exception {
		if(pooledDataSource != null ) return pooledDataSource;
		else {
			pooledDataSource = new PooledDataSource(masterConfig);
			return pooledDataSource;
		}
	}
	
	
	public Connection getConnection() throws Exception {
		try {
			return bds.getConnection();
		} catch( Exception e ) {
			throw new RuntimeException(e);
		}
	}
	
	
	public void overrideDefaultAutoCommit( boolean newAutoCommit ) {
		bds.setDefaultAutoCommit(newAutoCommit);
	}
	
}
