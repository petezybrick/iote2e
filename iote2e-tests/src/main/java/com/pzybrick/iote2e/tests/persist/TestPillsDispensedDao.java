package com.pzybrick.iote2e.tests.persist;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.PillsDispensedDao;
import com.pzybrick.iote2e.stream.persist.PillsDispensedVo;
import com.pzybrick.iote2e.stream.persist.PillsDispensedVo.DispenseState;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPillsDispensedDao {
	private static final Logger logger = LogManager.getLogger(TestPillsDispensedDao.class);
	private static final String loginName = "pzybrick1";
	private static final String actuatorName = "pilldisp1";
	private static final int numToDispense = 2;
	private static MasterConfig masterConfig;
	private static String pillsDispensedUuid;

	
	@Test
	public void testaInsertPending() throws Exception {

		PillsDispensedDao.insertPending( masterConfig, pillsDispensedUuid, loginName, actuatorName, numToDispense );
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.PENDING.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertNull("numDispensed is not null", pillsDispensedVo.getNumDispensed() );
		Assert.assertNull("delta is not null", pillsDispensedVo.getDelta() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNull("stateDispensingTs is not null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNull("stateCompleteTs is not null", pillsDispensedVo.getStateCompleteTs() );
	}
	
	@Test
	public void testbUpdatePendingToDispensing() throws Exception {
		PillsDispensedDao.updatePendingToDispensing( masterConfig, pillsDispensedUuid );
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.DISPENSING.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertNull("numDispensed is not null", pillsDispensedVo.getNumDispensed() );
		Assert.assertNull("delta is not null", pillsDispensedVo.getDelta() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNotNull("stateDispensingTs is null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNull("stateCompleteTs is not null", pillsDispensedVo.getStateCompleteTs() );
	}
	
	@Test
	public void testcUpdateDispensingToCompleteDeltaZero() throws Exception {
		int targetDelta = 0;
		int targetNumDispensed = numToDispense;
		byte[] imagePngBefore = TestCommonHandler.fileToByteArray(System.getenv("PATH_TEST_PNG"));
		PillsDispensedDao.updateDispensingToComplete(masterConfig, pillsDispensedUuid, targetNumDispensed, targetDelta, imagePngBefore);
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.COMPLETE.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertEquals("numDispensed", targetNumDispensed, pillsDispensedVo.getNumDispensed().intValue() );
		Assert.assertEquals("delta", targetDelta, pillsDispensedVo.getDelta().intValue() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNotNull("stateDispensingTs is null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNotNull("stateCompleteTs is null", pillsDispensedVo.getStateCompleteTs() );
		byte[] imagePngAfter = PillsDispensedDao.findImageBytesByPillsDispensedUuid(masterConfig, pillsDispensedUuid);
		Assert.assertNotNull("imagePngAfter is null", imagePngAfter );
		Assert.assertEquals("imagePng length", imagePngBefore.length, imagePngAfter.length );		
	}

	
	@Test
	public void testdUpdateDispensingToCompleteDeltaLtZero() throws Exception {
		PillsDispensedDao.deleteImageBytesByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		int targetDelta = -1;
		int targetNumDispensed = numToDispense-1;
		byte[] imagePngBefore = TestCommonHandler.fileToByteArray(System.getenv("PATH_TEST_PNG"));
		PillsDispensedDao.updateDispensingToComplete(masterConfig, pillsDispensedUuid, targetNumDispensed, targetDelta, imagePngBefore);
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.COMPLETE.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertEquals("numDispensed", targetNumDispensed, pillsDispensedVo.getNumDispensed().intValue() );
		Assert.assertEquals("delta", targetDelta, pillsDispensedVo.getDelta().intValue() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNotNull("stateDispensingTs is null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNotNull("stateCompleteTs is null", pillsDispensedVo.getStateCompleteTs() );
		byte[] imagePngAfter = PillsDispensedDao.findImageBytesByPillsDispensedUuid(masterConfig, pillsDispensedUuid);
		Assert.assertNotNull("imagePngAfter is null", imagePngAfter );
		Assert.assertEquals("imagePng length", imagePngBefore.length, imagePngAfter.length );		
	}
	

	@Test
	public void testeUpdateDispensingToCompleteDeltaGtZero() throws Exception {
		PillsDispensedDao.deleteImageBytesByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		int targetDelta = 1;
		int targetNumDispensed = numToDispense+1;
		byte[] imagePngBefore = TestCommonHandler.fileToByteArray(System.getenv("PATH_TEST_PNG"));
		PillsDispensedDao.updateDispensingToComplete(masterConfig, pillsDispensedUuid, targetNumDispensed, targetDelta, imagePngBefore);
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.COMPLETE.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertEquals("numDispensed", targetNumDispensed, pillsDispensedVo.getNumDispensed().intValue() );
		Assert.assertEquals("delta", targetDelta, pillsDispensedVo.getDelta().intValue() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNotNull("stateDispensingTs is null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNotNull("stateCompleteTs is null", pillsDispensedVo.getStateCompleteTs() );
		byte[] imagePngAfter = PillsDispensedDao.findImageBytesByPillsDispensedUuid(masterConfig, pillsDispensedUuid);
		Assert.assertNotNull("imagePngAfter is null", imagePngAfter );
		Assert.assertEquals("imagePng length", imagePngBefore.length, imagePngAfter.length );		
	}
	

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestPillsDispensedDao.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestPillsDispensedDao.pillsDispensedUuid = UUID.randomUUID().toString();
		logger.info(">>> Common pillsDispensedUuid=" + pillsDispensedUuid);
	}
	

	@AfterClass
	public static void afterClass() throws Exception {
		logger.info(">>> Cleaning up");
		PillsDispensedDao.deleteImageBytesByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		PillsDispensedDao.deletePillsDispensedByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
}
