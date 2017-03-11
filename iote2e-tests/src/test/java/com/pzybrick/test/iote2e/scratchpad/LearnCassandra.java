package com.pzybrick.test.iote2e.scratchpad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.pzybrick.iote2e.stream.svc.ActuatorState;
import com.pzybrick.iote2e.stream.svc.LoginSourceSensorActuator;

public class LearnCassandra {
	private static final Logger logger = LogManager.getLogger(LearnCassandra.class);
	private static final String TEST_KEYSPACE_NAME = "iote2e";
	private static final String TEST_TABLE_NAME = "actuator_state";
	private static final String TEST_CONTACT_POINT = "127.0.0.1";
	private Cluster cluster;
	private Session session;

	public static void main(String[] args) {
		LearnCassandra learnCassandra = new LearnCassandra();
		learnCassandra.process();
	}
	
	public void process()  {

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
			String pk = "lo2|lo2_so2|lo2_so2_se2";
			Map<String,Object> mapRow = findRow( pk );
			logger.info("Before Update: actuator_value={}, actuator_value_updated_at={}", mapRow.get("actuator_value"), mapRow.get("actuator_value_updated_at"));
			updateRow(pk, "on");
			mapRow = findRow( pk );
			logger.info("After Update: actuator_value={}, actuator_value_updated_at={}", mapRow.get("actuator_value"), mapRow.get("actuator_value_updated_at"));
			deleteRow( pk );
			mapRow = findRow( pk );
			logger.info("After Delete: mapRow={}", mapRow);
			
			long cnt = count(TEST_TABLE_NAME);
			logger.info("Row Count before truncate: {}",  cnt );
			truncate(TEST_TABLE_NAME);
			cnt = count(TEST_TABLE_NAME);
			logger.info("Row Count after truncate: {}",  cnt );

			
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage());			
		} finally {
			disconnect();
		}
	}
	
	protected ResultSet execute( String cql ) throws Exception {
		Exception lastException = null;
		long sleepMs = 1000;
		long maxAttempts = 10;
		boolean isSuccess = false;
		ResultSet rs =  null;
		for( int i=0 ; i<maxAttempts ; i++ ) {
			try {
				rs = session.execute(cql);
				System.out.println(">>>> success <<<< "+ cql);
				isSuccess = true;
				break;
			} catch( NoHostAvailableException nhae ) {
				System.out.println(">>>> failure <<<< "+ cql);
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
		if( isSuccess ) return rs;
		else throw new Exception(lastException);
	}
	
	public long count( String tableName ) throws Exception {
		long cnt = -1;
		try {
			String selectCount = String.format("SELECT COUNT(*) FROM %s", tableName);
			logger.debug("selectCount={}",selectCount);
			long before = System.currentTimeMillis();
			ResultSet rs = execute(selectCount);
			Row row = rs.one();
			if( row != null ) {
				cnt = row.getLong(0);
			}
			logger.info(">>> elapsed {}",(System.currentTimeMillis()-before));

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
		return cnt;
	}
	
	public void truncate( String tableName ) throws Exception {
		try {
			String truncate = String.format("TRUNCATE %s", tableName );
			logger.debug("truncate={}",truncate);
			long before = System.currentTimeMillis();
			execute(truncate);
			logger.info(">>> elapsed {}",(System.currentTimeMillis()-before));

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	public void deleteRow( String pk ) throws Exception {
		try {
			String delete = String.format("DELETE FROM actuator_state where login_source_sensor='%s'", pk);
			logger.debug("delete={}",delete);
			long before = System.currentTimeMillis();
			execute(delete);
			logger.info(">>> elapsed {}",(System.currentTimeMillis()-before));

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public void updateRow( String pk, String newValue ) throws Exception {
		try {
			String update = String.format("UPDATE actuator_state SET actuator_value='%s',actuator_value_updated_at=toTimestamp(now()) where login_source_sensor='%s'", newValue, pk);
			logger.debug("update={}",update);
			long before = System.currentTimeMillis();
			execute(update);
			logger.info(">>> elapsed {}",(System.currentTimeMillis()-before));

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public Map<String,Object> findRow( String pk ) throws Exception {
		try {				
			Map<String,Object> map = null;
			String select = String.format("SELECT * FROM actuator_state where login_source_sensor='%s'", pk);
			logger.debug("select={}",select);
			long before = System.currentTimeMillis();
			ResultSet rs = execute(select);
			Iterator<Row> it = rs.iterator();
			Row row = rs.one();
			if( row != null ) {
				map = new HashMap<String,Object>();
				logger.info(">>> elapsed {}",(System.currentTimeMillis()-before));
				ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
				for( ColumnDefinitions.Definition columnDefinition : columnDefinitions) {
					String name = columnDefinition.getName();
					Object value = row.getObject(name);
					map.put( name, value );
				}
			}
			return map;

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	
	public void insertActuatorState( ActuatorState actuatorState) throws Exception {
		try {
			logger.debug("loginSourceSensorActuator={}",actuatorState.toString());
			String insert = createInsertActuatorState( actuatorState );
			logger.debug("insert={}",insert);
			session.execute(insert);		

		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}
	
	
	public void insertActuatorStateBatch( List<ActuatorState> actuatorStates ) throws Exception {
		try {
			logger.debug( "inserting {} batch rows", actuatorStates.size());
			StringBuilder sb = new StringBuilder("BEGIN BATCH\n");
			for( ActuatorState actuatorState : actuatorStates ) {
				sb.append( createInsertActuatorState( actuatorState )).append("\n");
			}
			sb.append("APPLY BATCH;");
			logger.debug("insert batch={}", sb.toString());
			session.execute(sb.toString());
			
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}

	
	public static String createInsertActuatorState( ActuatorState actuatorState ) {
		String key = actuatorState.getLoginName() + "|" +
				actuatorState.getSourceName() + "|" +
				actuatorState.getSensorName();
		String insert = String.format("INSERT INTO actuator_state " + 
			"(login_source_sensor,actuator_name,actuator_value,actuator_desc,actuator_value_updated_at) " + 
			"values('%s','%s','%s','%s',toTimestamp(now()));",
			key, actuatorState.getActuatorName(), actuatorState.getActuatorValue(),
			actuatorState.getActuatorDesc() );
		return insert;
	}

	
	public void useKeyspace( String keyspaceName ) throws Exception {
		try {
			logger.debug("keyspaceName={}",keyspaceName);
			execute("USE " + keyspaceName );		
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			throw e;
		}
	}	
	
	public void createTable( String createTable ) throws Exception {
		try {
			logger.debug("createTable={}",createTable);
			execute(createTable);		
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
			execute(dropKeyspace);
			execute(createKeyspace);			
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
	
	private static ActuatorState createActuatorStateSingle() {
		return new ActuatorState().setLoginName("lo1").setSourceName("lo1_so1").setSensorName("lo1_so1_se1")
				.setActuatorName("fan1").setActuatorValue("off").setActuatorDesc("fan in greenhouse");
	}
	
	private static List<ActuatorState> createActuatorStateBatch() {
		List<ActuatorState> actuatorStates = new ArrayList<ActuatorState>();
		actuatorStates.add( new ActuatorState().setLoginName("lo2").setSourceName("lo2_so2").setSensorName("lo2_so2_se2")
				.setActuatorName("ledGreen").setActuatorValue("off").setActuatorDesc("Green LED") );
		actuatorStates.add( new ActuatorState().setLoginName("lo3").setSourceName("lo3_so3").setSensorName("lo3_so3_se3")
				.setActuatorName("ledYellow").setActuatorValue("off").setActuatorDesc("Yellow LED") );
		actuatorStates.add( new ActuatorState().setLoginName("lo4").setSourceName("lo4_so4").setSensorName("lo4_so4_se4")
				.setActuatorName("ledRed").setActuatorValue("off").setActuatorDesc("Red LED") );
		return actuatorStates;
	}

}
