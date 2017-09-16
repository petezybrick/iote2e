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
package com.pzybrick.iote2e.stream.persist;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.google.gson.reflect.TypeToken;
import com.pzybrick.iote2e.common.persist.CassandraBaseDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.svc.ActuatorState;


/**
 * The Class ActuatorStateDao.
 */
public class ActuatorStateDao extends CassandraBaseDao {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ActuatorStateDao.class);
	
	/** The Constant TABLE_NAME. */
	private static final String TABLE_NAME = "actuator_state";
	
	/** The Constant CREATE_TABLE. */
	private static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "( " + 
			"	login_source_sensor text PRIMARY KEY, " + 
			"	actuator_name text, " + 
			"	actuator_value text, " + 
			"	actuator_desc text, " + 
			"	actuator_value_updated_at timestamp " + 
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
	 * Update actuator value.
	 *
	 * @param pk the pk
	 * @param newValue the new value
	 * @throws Exception the exception
	 */
	public static void updateActuatorValue( String pk, String newValue ) throws Exception {
		try {
			String update = createUpdateValueCql( pk, newValue );
			logger.debug("update={}",update);
			execute(update);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Find actuator value.
	 *
	 * @param pk the pk
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String findActuatorValue( String pk ) throws Exception {
		try {
			String select = String.format("SELECT actuator_value FROM %s where login_source_sensor='%s'; ", TABLE_NAME, pk);
			logger.debug("select={}",select);
			ResultSet rs = execute(select);
			Row row = rs.one();
			return row.getString("actuator_value");

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	/**
	 * Find actuator state.
	 *
	 * @param pk the pk
	 * @return the actuator state
	 * @throws Exception the exception
	 */
	public static ActuatorState findActuatorState( String pk ) throws Exception {
		try {
			String select = String.format("SELECT * FROM %s where login_source_sensor='%s'; ", TABLE_NAME, pk);
			logger.debug("select={}",select);
			ResultSet rs = execute(select);
			Row row = rs.one();
			return actuatorStateFromRow( row );

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
			String delete = String.format("DELETE FROM %s where login_source_sensor='%s'; ", TABLE_NAME, pk);
			logger.debug("delete={}",delete);
			execute(delete);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Actuator state from row.
	 *
	 * @param row the row
	 * @return the actuator state
	 */
	public static ActuatorState actuatorStateFromRow( Row row ) {
		if( row == null ) return null;
		String loginSourceSensor = row.getString("login_source_sensor");
		if( loginSourceSensor == null ) return null;	// not found
		String[] losose = loginSourceSensor.split("[|]");
		ActuatorState actuatorState = new ActuatorState().setLoginName(losose[0]).setSourceName(losose[1])
				.setSensorName(losose[2])
				.setActuatorName(row.getString("actuator_name"))
				.setActuatorValue(row.getString("actuator_value"))
				.setActuatorDesc(row.getString("actuator_desc"))
				.setActuatorValueUpdatedAt( new DateTime(row.getTimestamp("actuator_value_updated_at")).withZone(DateTimeZone.UTC).toString() );
		return actuatorState;
	}

	
	/**
	 * Insert actuator state.
	 *
	 * @param actuatorState the actuator state
	 * @throws Exception the exception
	 */
	public static void insertActuatorState( ActuatorState actuatorState) throws Exception {
		try {
			logger.debug("actuatorState={}",actuatorState.toString());
			String insert = createInsertActuatorState( actuatorState );
			logger.debug("insert={}",insert);
			execute(insert);		

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	/**
	 * Insert actuator state batch.
	 *
	 * @param actuatorStates the actuator states
	 * @throws Exception the exception
	 */
	public static void insertActuatorStateBatch( List<ActuatorState> actuatorStates ) throws Exception {
		try {
			logger.debug( "inserting actuatorState {} batch rows", actuatorStates.size());
			StringBuilder sb = new StringBuilder("BEGIN BATCH\n");
			for( ActuatorState actuatorState : actuatorStates ) {
				sb.append( createInsertActuatorState( actuatorState )).append("\n");
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
	 * Reset actuator state batch.
	 *
	 * @param actuatorStates the actuator states
	 * @throws Exception the exception
	 */
	public static void resetActuatorStateBatch( List<ActuatorState> actuatorStates ) throws Exception {
		try {
			logger.debug( "Resetting actuatorState {} batch rows", actuatorStates.size());
			StringBuilder sb = new StringBuilder("BEGIN BATCH\n");
			for( ActuatorState actuatorState : actuatorStates ) {
				sb.append( createUpdateValueCql( actuatorState.getPk(), null )).append("\n");
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
	 * Creates the insert actuator state.
	 *
	 * @param actuatorState the actuator state
	 * @return the string
	 */
	public static String createInsertActuatorState( ActuatorState actuatorState ) {
		String key = actuatorState.getLoginName() + "|" +
				actuatorState.getSourceName() + "|" +
				actuatorState.getSensorName() + "|";
		String insActuatorValue = actuatorState.getActuatorValue() != null ? "'"+actuatorState.getActuatorValue()+"'" : "null";
		String insert = String.format("INSERT INTO %s " + 
			"(login_source_sensor,actuator_name,actuator_value,actuator_desc,actuator_value_updated_at) " + 
			"values('%s','%s',%s,'%s',toTimestamp(now())); ",
			TABLE_NAME, key, actuatorState.getActuatorName(), insActuatorValue,
			actuatorState.getActuatorDesc() );
		return insert;
	}
	
	/**
	 * Creates the actuator states from json.
	 *
	 * @param rawJson the raw json
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<ActuatorState> createActuatorStatesFromJson( String rawJson ) throws Exception {
		List<ActuatorState> actuatorStates = Iote2eUtils.getGsonInstance().fromJson(rawJson,
				new TypeToken<List<ActuatorState>>() {
				}.getType());
		expandLoginsSourceNames(actuatorStates);
		return actuatorStates;
	}
	
	/**
	 * Expand logins source names.
	 *
	 * @param actuatorStates the actuator states
	 * @throws Exception the exception
	 */
	public static void expandLoginsSourceNames(List<ActuatorState> actuatorStates) throws Exception {
		ListIterator<ActuatorState> lit = actuatorStates.listIterator();
		while( lit.hasNext() ) {
			ActuatorState actuatorState = lit.next();
			if(actuatorState.getLoginName().indexOf("|") > -1 || actuatorState.getSourceName().indexOf("|") > -1 ) {
				lit.remove();
				List<String> loginNames = Arrays.asList( actuatorState.getLoginName().split("[|]"));
				List<String> sourceNames = Arrays.asList( actuatorState.getSourceName().split("[|]"));
				for( String loginName : loginNames ) {
					for( String sourceName : sourceNames ) {
						ActuatorState clone = actuatorState.clone();
						clone.setLoginName(loginName);
						clone.setSourceName(sourceName);
						lit.add(clone);
					}
				}
			}
		}
	}

	
	/**
	 * Creates the update value cql.
	 *
	 * @param pk the pk
	 * @param newValue the new value
	 * @return the string
	 */
	private static String createUpdateValueCql( String pk, String newValue  ) {
		if( newValue != null ) newValue = "'" + newValue + "'";
		return String.format("UPDATE %s SET actuator_value=%s,actuator_value_updated_at=toTimestamp(now()) where login_source_sensor='%s'; ", 
				TABLE_NAME, newValue, pk);
	}
	
}
