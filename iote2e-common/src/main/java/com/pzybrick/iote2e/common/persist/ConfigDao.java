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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;


/**
 * The Class ConfigDao.
 */
public class ConfigDao extends CassandraBaseDao {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ConfigDao.class);
	
	/** The Constant TABLE_NAME. */
	private static final String TABLE_NAME = "config";
	
	/** The Constant CREATE_TABLE. */
	private static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "( " + 
			"	config_name text PRIMARY KEY, " + 
			"	config_json text " + 
			");";

	
	/**
	 * Creates the table.
	 *
	 * @throws Exception the exception
	 */
	public static void createTable( ) throws Exception {
		try {
			logger.debug("createTable={}",CREATE_TABLE);
			execute(CREATE_TABLE);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Drop table.
	 *
	 * @throws Exception the exception
	 */
	public static void dropTable( ) throws Exception {
		try {
			logger.debug("dropTable={}",TABLE_NAME);
			execute("DROP TABLE IF EXISTS " + TABLE_NAME + "; ");

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	/**
	 * Update config json.
	 *
	 * @param pk the pk
	 * @param configJson the config json
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Find config json.
	 *
	 * @param pk the pk
	 * @return the string
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Count.
	 *
	 * @return the long
	 * @throws Exception the exception
	 */
	public static long count( ) throws Exception {
		return count(TABLE_NAME);
	}
	
	/**
	 * Truncate.
	 *
	 * @throws Exception the exception
	 */
	public static void truncate( ) throws Exception {
		truncate(TABLE_NAME);
	}
	
	/**
	 * Checks if is table exists.
	 *
	 * @param keyspaceName the keyspace name
	 * @return true, if is table exists
	 * @throws Exception the exception
	 */
	public static boolean isTableExists( String keyspaceName ) throws Exception {
		return isTableExists(keyspaceName, TABLE_NAME);
	}

	/**
	 * Delete row.
	 *
	 * @param pk the pk
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Insert config.
	 *
	 * @param configVo the config vo
	 * @throws Exception the exception
	 */
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
	
	
	/**
	 * Insert config batch.
	 *
	 * @param configVos the config vos
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Creates the insert config.
	 *
	 * @param configVo the config vo
	 * @return the string
	 */
	public static String createInsertConfig( ConfigVo configVo ) {
		String insert = String.format("INSERT INTO %s " + 
			"(config_name,config_json) values('%s','%s'); ",
			TABLE_NAME, configVo.getConfigName(), configVo.getConfigJson() );
		return insert;
	}
	
	/**
	 * Creates the config json cql.
	 *
	 * @param pk the pk
	 * @param configJson the config json
	 * @return the string
	 */
	private static String createConfigJsonCql( String pk, String configJson  ) {
		if( configJson != null ) configJson = "'" + configJson + "'";
		return String.format("UPDATE %s SET config_json=%s where config_name='%s'; ", 
				TABLE_NAME, configJson, pk);
	}
	
}
