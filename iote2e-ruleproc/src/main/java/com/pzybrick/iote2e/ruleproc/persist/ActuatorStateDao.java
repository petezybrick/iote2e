package com.pzybrick.iote2e.ruleproc.persist;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.pzybrick.iote2e.ruleproc.svc.ActuatorState;
import com.pzybrick.iote2e.ruleproc.svc.LoginSourceSensorActuator;

public class ActuatorStateDao extends CassandraBaseDao {
	private static final Logger logger = LogManager.getLogger(ActuatorStateDao.class);
	private static final String TABLE_NAME = "actuator_state";
	private static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "( " + 
			"	login_source_sensor text PRIMARY KEY, " + 
			"	actuator_name text, " + 
			"	actuator_value text, " + 
			"	actuator_desc text, " + 
			"	actuator_value_updated_at timestamp " + 
			");";

	
	public static void createTableActuatorState( ) throws Exception {
		try {
			logger.debug("createTable={}",CREATE_TABLE);
			execute(CREATE_TABLE);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public static void updateValue( String pk, String newValue ) throws Exception {
		try {
			String update = String.format("UPDATE actuator_state SET actuator_value='%s',actuator_value_updated_at=toTimestamp(now()) where login_source_sensor='%s'", 
					newValue, pk);
			logger.debug("update={}",update);
			execute(update);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public static String findValue( String pk ) throws Exception {
		try {
			String select = String.format("SELECT actuator_value FROM actuator_state where login_source_sensor='%s'", pk);
			logger.debug("select={}",select);
			ResultSet rs = execute(select);
			Row row = rs.one();
			return row.getString("actuator_value");

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public static ActuatorState findActuatorState( String pk ) throws Exception {
		try {
			String select = String.format("SELECT * FROM actuator_state where login_source_sensor='%s'", pk);
			logger.debug("select={}",select);
			ResultSet rs = execute(select);
			Row row = rs.one();
			return actuatorStateFromRow( row );

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
	
	public static void deleteRow( String pk ) throws Exception {
		try {
			String delete = String.format("DELETE FROM actuator_state where login_source_sensor='%s'", pk);
			logger.debug("delete={}",delete);
			execute(delete);

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	public static ActuatorState actuatorStateFromRow( Row row ) {
		if( row == null ) return null;
		String loginSourceSensor = row.getString("login_source_sensor");
		if( loginSourceSensor == null ) return null;	// not found
		String[] losose = loginSourceSensor.split("[|]");
		ActuatorState actuatorState = new ActuatorState().setLoginName(losose[0]).setSourceName(losose[1])
				.setSensorName(losose[2])
				.setActuatorName(row.getString("actuator_name"))
				.setActuatorValue(row.getString("actuator_value"))
				.setDesc(row.getString("actuator_desc"))
				.setActuatorValueUpdatedAt( new DateTime(row.getTimestamp("actuator_value_updated_at")).withZone(DateTimeZone.UTC).toString() );
		return actuatorState;
	}

	
	public static void insertActuatorState( LoginSourceSensorActuator loginSourceSensorActuator) throws Exception {
		try {
			logger.debug("loginSourceSensorActuator={}",loginSourceSensorActuator.toString());
			String insert = convertLoginSourceSensorActuatorToInsert( loginSourceSensorActuator );
			logger.debug("insert={}",insert);
			execute(insert);		

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public static void insertActuatorStateBatch( List<LoginSourceSensorActuator> loginSourceSensorActuators ) throws Exception {
		try {
			logger.debug( "inserting {} batch rows", loginSourceSensorActuators.size());
			StringBuilder sb = new StringBuilder("BEGIN BATCH\n");
			for( LoginSourceSensorActuator loginSourceSensorActuator : loginSourceSensorActuators ) {
				sb.append( convertLoginSourceSensorActuatorToInsert( loginSourceSensorActuator )).append("\n");
			}
			sb.append("APPLY BATCH;");
			logger.debug("insert batch={}", sb.toString());
			execute(sb.toString());
			
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public static String convertLoginSourceSensorActuatorToInsert( LoginSourceSensorActuator loginSourceSensorActuator ) {
		String key = loginSourceSensorActuator.getLoginName() + "|" +
				loginSourceSensorActuator.getSourceName() + "|" +
				loginSourceSensorActuator.getSensorName();
		String insert = String.format("INSERT INTO actuator_state " + 
			"(login_source_sensor,actuator_name,actuator_value,actuator_desc,actuator_value_updated_at) " + 
			"values('%s','%s','%s','%s',toTimestamp(now()));",
			key, loginSourceSensorActuator.getActuatorName(), loginSourceSensorActuator.getActuatorValue(),
			loginSourceSensorActuator.getDesc() );
		return insert;
	}
}
