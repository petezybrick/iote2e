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
package com.pzybrick.iote2e.common.persist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.exceptions.NoHostAvailableException;


/**
 * The Class CassandraBaseDao.
 */
public class CassandraBaseDao {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(CassandraBaseDao.class);
	
	/** The Constant DEFAULT_KEYSPACE_NAME. */
	public static final String DEFAULT_KEYSPACE_NAME = "iote2e";
	
	/** The cluster. */
	private static Cluster cluster;
	
	/** The session. */
	private static Session session;
	
	/** The contact point. */
	private static String contactPoint;
	
	/** The keyspace name. */
	private static String keyspaceName;
	

	/**
	 * Execute.
	 *
	 * @param cql the cql
	 * @return the result set
	 * @throws Exception the exception
	 */
	/*
	 * execute with built in retry - cassandra will retry the connection automatically, we just need to keep checking if it succeeded
	 */
	protected static ResultSet execute( String cql ) throws Exception {
		logger.debug("cql: {}",  cql);
		Exception lastException = null;
		long sleepMs = 1000;
		long maxAttempts = 10;
		boolean isSuccess = false;
		ResultSet rs =  null;
		for( int i=0 ; i<maxAttempts ; i++ ) {
			try {
				rs = getSession().execute(cql);
				isSuccess = true;
				break;
			} catch( NoHostAvailableException nhae ) {
				lastException = nhae;
				logger.warn(nhae.getLocalizedMessage());
				try {
					Thread.sleep(sleepMs);
					sleepMs = 2*sleepMs;
				} catch(Exception e) {}
			} catch( Exception e ) {
				if( e.getCause() instanceof com.datastax.driver.core.exceptions.AlreadyExistsException ) break;
				lastException = e;
				logger.warn(e.getLocalizedMessage());
				try {
					Thread.sleep(sleepMs);
					sleepMs = 2*sleepMs;
				} catch(Exception e2) {}
			}

		}
		logger.debug("isSuccess: {}",  isSuccess);
		if( isSuccess ) return rs;
		else throw new Exception(lastException);
	}

	
	/**
	 * Count.
	 *
	 * @param tableName the table name
	 * @return the long
	 * @throws Exception the exception
	 */
	public static long count( String tableName ) throws Exception {
		long cnt = -1;
		try {
			String selectCount = String.format("SELECT COUNT(*) FROM %s; ", tableName);
			logger.debug("selectCount={}",selectCount);
			ResultSet rs = execute(selectCount);
			Row row = rs.one();
			if( row != null ) {
				cnt = row.getLong(0);
			}

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
		return cnt;
	}
	
	/**
	 * Truncate.
	 *
	 * @param tableName the table name
	 * @throws Exception the exception
	 */
	public static void truncate( String tableName ) throws Exception {
		try {
			String truncate = String.format("TRUNCATE %s; ", tableName );
			logger.debug("truncate={}",truncate);
			execute(truncate);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Checks if is table exists.
	 *
	 * @param keyspaceName the keyspace name
	 * @param tableName the table name
	 * @return true, if is table exists
	 * @throws Exception the exception
	 */
	public static boolean isTableExists( String keyspaceName, String tableName ) throws Exception {
		try {
			TableMetadata tableMetadata = findTableMetadata( keyspaceName, tableName );
			if( tableMetadata != null ) return true;
			else return false;
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Find table metadata.
	 *
	 * @param keyspaceName the keyspace name
	 * @param tableName the table name
	 * @return the table metadata
	 * @throws Exception the exception
	 */
	public static TableMetadata findTableMetadata( String keyspaceName, String tableName ) throws Exception {
		KeyspaceMetadata keyspaceMetadata = cluster.getMetadata().getKeyspace(keyspaceName);
		return keyspaceMetadata.getTable(tableName);
	}

	/**
	 * Creates the keyspace.
	 *
	 * @param keyspaceName the keyspace name
	 * @param replicationStrategy the replication strategy
	 * @param replicationFactor the replication factor
	 * @throws Exception the exception
	 */
	public static void createKeyspace( String keyspaceName, String replicationStrategy, int replicationFactor ) throws Exception {
		execute( String.format("CREATE KEYSPACE %s WITH replication = {'class':'%s','replication_factor':%d}; ", 
				keyspaceName, replicationStrategy, replicationFactor) );
	}	
	
	/**
	 * Drop keyspace.
	 *
	 * @param keyspaceName the keyspace name
	 * @throws Exception the exception
	 */
	public static void dropKeyspace( String keyspaceName ) throws Exception {
		execute( String.format("DROP KEYSPACE IF EXISTS %s; ", keyspaceName) );
	}
	
	/**
	 * Use keyspace.
	 *
	 * @param keyspaceName the keyspace name
	 * @throws Exception the exception
	 */
	public static void useKeyspace( String keyspaceName ) throws Exception {
		if( keyspaceName == null ) throw new Exception("Missing required keyspace name, must be specified when creating MasterConfig");
		execute( String.format("USE %s; ", keyspaceName) );
	}
	
	/**
	 * Gets the session.
	 *
	 * @return the session
	 * @throws Exception the exception
	 */
	protected static Session getSession( ) throws Exception {
		try {
			logger.debug("getSession");
			if( session == null ) throw new Exception("Must do a connect() before a getSession()");
			return session;
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;			
		}
	}
	
	
	/**
	 * Connect.
	 *
	 * @param contactPoint the contact point
	 * @param keyspaceName the keyspace name
	 * @throws Exception the exception
	 */
	public synchronized static void connect( String contactPoint, String keyspaceName ) throws Exception {
		try {
			CassandraBaseDao.contactPoint = contactPoint;
			CassandraBaseDao.keyspaceName = keyspaceName;
			logger.debug("contactPoint={}",contactPoint);
			cluster = Cluster.builder().addContactPoint(contactPoint).build();
			session = cluster.connect( keyspaceName );
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			if( contactPoint != null ) disconnect();
			throw e;			
		}
	}
	
	/**
	 * Disconnect.
	 */
	public synchronized static void disconnect() {
		logger.debug("closing session and cluster");
		if( session != null ) {
			try {
				session.close();
			} catch( Exception eSession ) {
				logger.error(eSession.getLocalizedMessage());
			}
		}			
		if( cluster != null ) {
			try {
				if( session != null ) {
					while( !session.isClosed() ) {
						Thread.sleep(500);
					}
					session = null;
				}
				cluster.close();
				cluster = null;
			} catch( Exception eCluster ) {
				logger.error(eCluster.getLocalizedMessage());
			}
		}
	}
	
}
