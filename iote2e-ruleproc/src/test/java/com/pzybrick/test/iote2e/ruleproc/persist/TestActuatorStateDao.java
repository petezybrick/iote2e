package com.pzybrick.test.iote2e.ruleproc.persist;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.ruleproc.persist.Iote2eCommonDao;
import com.pzybrick.iote2e.ruleproc.svc.ActuatorState;
import com.pzybrick.iote2e.ruleproc.svc.LoginSourceSensorActuator;



public class TestActuatorStateDao {
	private static final Logger logger = LogManager.getLogger(TestActuatorStateDao.class);
	private static final String TEST_KEYSPACE_NAME = "iote2e";
	private static final String TEST_KEYSPACE_REPLICATION_STRATEGY = "SimpleStrategy";
	private static final int TEST_KEYSPACE_REPLICATION_FACTOR = 3;


	@Test
	public void testSequence() {
		try {
			Iote2eCommonDao.dropKeyspace(TEST_KEYSPACE_NAME);
			Iote2eCommonDao.createKeyspace(TEST_KEYSPACE_NAME, TEST_KEYSPACE_REPLICATION_STRATEGY, TEST_KEYSPACE_REPLICATION_FACTOR);
			Iote2eCommonDao.useKeyspace(TEST_KEYSPACE_NAME);
			
			
			ActuatorStateDao.dropTable();
			ActuatorStateDao.isTableExists(TEST_KEYSPACE_NAME);
			
			
			ActuatorStateDao.createTable();

			ActuatorStateDao.insertActuatorState( createActuatorStateSingle());
			ActuatorStateDao.insertActuatorStateBatch( createActuatorStateBatch());
			
			String pk = "lo2|lo2_so2|lo2_so2_se2";
			ActuatorState actuatorState = ActuatorStateDao.findActuatorState(pk);
			logger.info(actuatorState.toString());
			Assert.assertEquals("actuatorState login", "lo2", actuatorState.getLoginName());
			Assert.assertEquals("actuatorState source", "lo2_so2", actuatorState.getSourceName());
			Assert.assertEquals("actuatorState sensor", "lo2_so2_se2", actuatorState.getSensorName());
			Assert.assertEquals("actuatorState name", "ledGreen", actuatorState.getActuatorName());
			Assert.assertEquals("actuatorState actuatorValue", "off", actuatorState.getActuatorValue());
			Assert.assertEquals("actuatorState desc", "Green LED", actuatorState.getActuatorDesc());
			
			String value = ActuatorStateDao.findActuatorValue(pk);
			logger.info("value - before update {}", value );
			ActuatorStateDao.updateActuatorValue(pk,"on");
			value = ActuatorStateDao.findActuatorValue(pk);
			logger.info("value - after update {}", value );
			Assert.assertEquals("value after update", "on", value);
			
			ActuatorStateDao.deleteRow(pk);
			actuatorState = ActuatorStateDao.findActuatorState(pk);
			logger.info("actuatorState after delete: {}",actuatorState);
			Assert.assertNull("actuatorState after delete", actuatorState);
			
			long cntBefore = ActuatorStateDao.count();
			logger.info("cntBefore {}", cntBefore);
			Assert.assertEquals("cnt before truncate", 3, cntBefore );
			ActuatorStateDao.truncate();
			long cntAfter = ActuatorStateDao.count();
			logger.info("cntAfter {}", cntAfter);
			Assert.assertEquals("cnt after truncate", 0, cntAfter );
			
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage());			
		} finally {
			Iote2eCommonDao.disconnect();
			ActuatorStateDao.disconnect();
		}
	}
	
	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
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
