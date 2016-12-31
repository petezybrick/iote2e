package com.pzybrick.test.iote2e.scratchpad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.pzybrick.iote2e.ruleproc.svc.LoginSourceSensorActuator;

public class LearnCassandra {
	private static final Logger logger = LogManager.getLogger(LearnCassandra.class);
	private static final String TEST_KEYSPACE_NAME = "iote2e";
	private static final String TEST_TABLE_NAME = "actuator_state";
	private static final String TEST_CONTACT_POINT = "127.0.0.1";
	private Cluster cluster;
	private Session session;

	public static void main(String[] args) {
		try {
			LearnCassandra learnCassandra = new LearnCassandra();
			learnCassandra.process();
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(),e);
		}

	}
	
	public void process() throws Exception {

		try {
			connect();
			createKeyspace( TEST_KEYSPACE_NAME );
			useKeyspace( TEST_KEYSPACE_NAME );
			final String createTable = 
				"CREATE TABLE actuator_state( " + 
				"	login_source_sensor text PRIMARY KEY, " + 
				"	actuator_name text, " + 
				"	actuator_value text, " + 
				"	actuator_desc text, " + 
				"	actuator_value_updated_at timestamp " + 
				");";
			createTable( createTable );
			insertActuatorState( createActuatorStateSingle());
			insertActuatorStateBatch( createActuatorStateBatch());
			Map mapRow = findRow( "lo2|lo2_so2|lo2_so2_se2" );
			System.out.println(mapRow);
			
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;			
		} finally {
			disconnect();
		}
	}
	
	
	public Map<String,Object> findRow( String pk ) throws Exception {
		try {
			String select = String.format("SELECT * FROM actuator_state where login_source_sensor='%s'", pk);
			logger.debug("select={}",select);
			long before = System.currentTimeMillis();
			ResultSet rs = session.execute(select);
			Row row = rs.all().get(0);
			logger.info(">>> elapsed {}",(System.currentTimeMillis()-before));
			ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
			Map<String,Object> map = new HashMap<String,Object>();
			for( ColumnDefinitions.Definition columnDefinition : columnDefinitions) {
				String name = columnDefinition.getName();
				Object value = row.getObject(name);
				map.put( name, value );
			}
			return map;

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	
	public void insertActuatorState( LoginSourceSensorActuator loginSourceSensorActuator) throws Exception {
		try {
			logger.debug("loginSourceSensorActuator={}",loginSourceSensorActuator.toString());
			String insert = convertLoginSourceSensorActuatorToInsert( loginSourceSensorActuator );
			logger.debug("insert={}",insert);
			session.execute(insert);		

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public void insertActuatorStateBatch( List<LoginSourceSensorActuator> loginSourceSensorActuators ) throws Exception {
		try {
			logger.debug( "inserting {} batch rows", loginSourceSensorActuators.size());
			StringBuilder sb = new StringBuilder("BEGIN BATCH\n");
			for( LoginSourceSensorActuator loginSourceSensorActuator : loginSourceSensorActuators ) {
				sb.append( convertLoginSourceSensorActuatorToInsert( loginSourceSensorActuator )).append("\n");
			}
			sb.append("APPLY BATCH;");
			logger.debug("insert batch={}", sb.toString());
			session.execute(sb.toString());
			
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

	
	public void useKeyspace( String keyspaceName ) throws Exception {
		try {
			logger.debug("keyspaceName={}",keyspaceName);
			session.execute("USE " + keyspaceName );		
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}	
	
	public void createTable( String createTable ) throws Exception {
		try {
			logger.debug("createTable={}",createTable);
			session.execute(createTable);		
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}	
	public void createKeyspace( String keyspaceName ) throws Exception {
		try {
			logger.debug("keyspaceName={}",keyspaceName);
			String dropKeyspace = "DROP KEYSPACE IF EXISTS " + keyspaceName + "; ";
			String createKeyspace = "CREATE KEYSPACE " + keyspaceName + " WITH replication = {'class':'SimpleStrategy','replication_factor':1}; ";
			session.execute(dropKeyspace);
			session.execute(createKeyspace);			
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	public void connect() throws Exception {
		try {
			logger.debug("contactPoint={}",TEST_CONTACT_POINT);
			cluster = Cluster.builder().addContactPoint(TEST_CONTACT_POINT).build();
			session = cluster.connect( );
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			disconnect();
			throw e;			
		}
	}
	
	public void disconnect() {
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
	
	private static LoginSourceSensorActuator createActuatorStateSingle() {
		return new LoginSourceSensorActuator().setLoginName("lo1").setSourceName("lo1_so1").setSensorName("lo1_so1_se1")
				.setActuatorName("fan1").setActuatorValue("off").setDesc("fan in greenhouse");
	}
	
	private static List<LoginSourceSensorActuator> createActuatorStateBatch() {
		List<LoginSourceSensorActuator> loginSourceSensorActuators = new ArrayList<LoginSourceSensorActuator>();
		loginSourceSensorActuators.add( new LoginSourceSensorActuator().setLoginName("lo2").setSourceName("lo2_so2").setSensorName("lo2_so2_se2")
				.setActuatorName("ledGreen").setActuatorValue("off").setDesc("Green LED") );
		loginSourceSensorActuators.add( new LoginSourceSensorActuator().setLoginName("lo3").setSourceName("lo3_so3").setSensorName("lo3_so3_se3")
				.setActuatorName("ledYellow").setActuatorValue("off").setDesc("Yellow LED") );
		loginSourceSensorActuators.add( new LoginSourceSensorActuator().setLoginName("lo4").setSourceName("lo4_so4").setSensorName("lo4_so4_se4")
				.setActuatorName("ledRed").setActuatorValue("off").setDesc("Red LED") );
		return loginSourceSensorActuators;
	}

}
