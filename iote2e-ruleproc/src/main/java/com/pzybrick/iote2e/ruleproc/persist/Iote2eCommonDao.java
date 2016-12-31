package com.pzybrick.iote2e.ruleproc.persist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Iote2eCommonDao extends CassandraBaseDao {
	private static final Logger logger = LogManager.getLogger(Iote2eCommonDao.class);

	public static void createKeyspace( String keyspaceName, String replicationStrategy, int replicationFactor ) throws Exception {
		execute( String.format("CREATE KEYSPACE %s WITH replication = {'class':'%s','replication_factor':%d}; ", 
				keyspaceName, replicationStrategy, replicationFactor) );
	}	
	
	public static void dropKeyspace( String keyspaceName ) throws Exception {
		execute( String.format("DROP KEYSPACE IF EXISTS %s; ", keyspaceName) );
	}
	
	public static void useKeyspace( String keyspaceName ) throws Exception {
		execute( String.format("USE %s; ", keyspaceName) );
	}

}
