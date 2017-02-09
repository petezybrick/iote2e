package com.pzybrick.iote2e.common.persist;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class ConfigDao extends CassandraBaseDao {
	private static final Logger logger = LogManager.getLogger(ConfigDao.class);
	private static final String TABLE_NAME = "config";
	private static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "( " + 
			"	config_name text PRIMARY KEY, " + 
			"	config_json text " + 
			");";

	
	public static void createTable( ) throws Exception {
		try {
			logger.debug("createTable={}",CREATE_TABLE);
			execute(CREATE_TABLE);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	public static void dropTable( ) throws Exception {
		try {
			logger.debug("dropTable={}",TABLE_NAME);
			execute("DROP TABLE IF EXISTS " + TABLE_NAME + "; ");

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public static void updateConfigJson( String pk, String configJson ) throws Exception {
		try {
			String update = createConfigJsonCql( pk, configJson );
			logger.debug("update={}",update);
			execute(update);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	public static String findConfigJson( String pk ) throws Exception {
		try {
			String select = String.format("SELECT config_json FROM %s where config_name='%s'; ", TABLE_NAME, pk);
			logger.debug("select={}",select);
			ResultSet rs = execute(select);
			Row row = rs.one();
			return row != null ? row.getString("config_json") : null;

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	public static long count( ) throws Exception {
		return count(TABLE_NAME);
	}
	
	public static void truncate( ) throws Exception {
		truncate(TABLE_NAME);
	}
	
	public static boolean isTableExists( String keyspaceName ) throws Exception {
		return isTableExists(keyspaceName, TABLE_NAME);
	}

	public static void deleteRow( String pk ) throws Exception {
		try {
			String delete = String.format("DELETE FROM %s where config_name='%s'; ", TABLE_NAME, pk);
			logger.debug("delete={}",delete);
			execute(delete);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	public static void insertConfig( ConfigVo configVo) throws Exception {
		try {
			logger.debug("ConfigVo={}",configVo.toString());
			String insert = createInsertConfig( configVo );
			logger.debug("insert={}",insert);
			execute(insert);		

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public static void insertConfigBatch( List<ConfigVo> configVos ) throws Exception {
		try {
			logger.debug( "inserting configVos {} batch rows", configVos.size());
			StringBuilder sb = new StringBuilder("BEGIN BATCH\n");
			for( ConfigVo configVo : configVos ) {
				sb.append( createInsertConfig( configVo )).append("\n");
			}
			sb.append("APPLY BATCH;");
			logger.debug("insert batch={}", sb.toString());
			execute(sb.toString());
			
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	public static String createInsertConfig( ConfigVo configVo ) {
		String insert = String.format("INSERT INTO %s " + 
			"(config_name,config_json) values('%s','%s'); ",
			TABLE_NAME, configVo.getConfigName(), configVo.getConfigJson() );
		return insert;
	}
	
	private static String createConfigJsonCql( String pk, String configJson  ) {
		if( configJson != null ) configJson = "'" + configJson + "'";
		return String.format("UPDATE %s SET config_json=%s where config_name='%s'; ", 
				TABLE_NAME, configJson, pk);
	}
	
}
