package com.pzybrick.iote2e.ruleproc.persist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class CassandraBaseDao {
	private static final Logger logger = LogManager.getLogger(CassandraBaseDao.class);
	private static Cluster cluster;
	private static Session session;
	private static String contactPoint;
	

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
				lastException = e;
				logger.error(e.getLocalizedMessage());
				break;
			}

		}
		logger.debug("isSuccess: {}",  isSuccess);
		if( isSuccess ) return rs;
		else throw new Exception(lastException);
	}

	
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
	
	public static TableMetadata findTableMetadata( String keyspaceName, String tableName ) throws Exception {
		KeyspaceMetadata keyspaceMetadata = cluster.getMetadata().getKeyspace(keyspaceName);
		return keyspaceMetadata.getTable(tableName);
	}

	
	public static void useKeyspace( String keyspaceName ) throws Exception {
		logger.debug("keyspaceName={}",keyspaceName);
		execute( String.format("USE %s; ", keyspaceName) );
	}
	
	protected static Session getSession() throws Exception {
		try {
			logger.debug("getSession");
			if( session == null ) connect();
			return session;
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;			
		}
	}
	
	
	public synchronized static void connect() throws Exception {
		try {
			if( contactPoint == null ) contactPoint = System.getenv("CASSANDRA_CONTACT_POINT");
			logger.debug("contactPoint={}",contactPoint);
			cluster = Cluster.builder().addContactPoint(contactPoint).build();
			session = cluster.connect( );
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			disconnect();
			throw e;			
		}
	}
	
	public synchronized static void disconnect() {
		synchronized(session) {
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
					cluster.close();
				} catch( Exception eCluster ) {
					logger.error(eCluster.getLocalizedMessage());
				}
			}
			logger.debug("closed session and cluster");
		}
	}
	
}
