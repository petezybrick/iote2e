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
package com.pzybrick.iote2e.tests.persist;

import java.util.List;
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


/**
 * The Class TestPillsDispensedDao.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPillsDispensedDao {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestPillsDispensedDao.class);
	
	/** The Constant loginName. */
	private static final String loginName = "pzybrick1";
	
	/** The Constant actuatorName. */
	private static final String actuatorName = "pilldisp1";
	
	/** The Constant sourceName. */
	private static final String sourceName = "rpi-001";
	
	/** The Constant numToDispense. */
	private static final int numToDispense = 2;
	
	/** The master config. */
	private static MasterConfig masterConfig;
	
	/** The pills dispensed uuid. */
	private static String pillsDispensedUuid;

	
	/**
	 * Testa insert pending.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testaInsertPending() throws Exception {
		PillsDispensedDao.insertPending( masterConfig, pillsDispensedUuid, loginName, sourceName, actuatorName, numToDispense );
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
		Assert.assertNull("stateDispensedTs is not null", pillsDispensedVo.getStateDispensedTs() );
		Assert.assertNull("stateConfirmedTs is not null", pillsDispensedVo.getStateConfirmedTs() );
	}
	
	/**
	 * Testb update pending to dispensing.
	 *
	 * @throws Exception the exception
	 */
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
		Assert.assertNull("stateDispensedTs is not null", pillsDispensedVo.getStateDispensedTs() );
		Assert.assertNull("stateConfirmedTs is not null", pillsDispensedVo.getStateConfirmedTs() );
	}
	
	/**
	 * Testc update dispensing to dispensed delta zero.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testcUpdateDispensingToDispensedDeltaZero() throws Exception {
		int targetDelta = 0;
		int targetNumDispensed = numToDispense;
		byte[] imagePngBefore = TestCommonHandler.fileToByteArray(System.getenv("PATH_TEST_PNG"));
		PillsDispensedDao.updateDispensingToDispensed(masterConfig, pillsDispensedUuid, targetNumDispensed, targetDelta, imagePngBefore);
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.DISPENSED.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertEquals("numDispensed", targetNumDispensed, pillsDispensedVo.getNumDispensed().intValue() );
		Assert.assertEquals("delta", targetDelta, pillsDispensedVo.getDelta().intValue() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNotNull("stateDispensingTs is null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNotNull("stateDispensedTs is not null", pillsDispensedVo.getStateDispensedTs() );
		Assert.assertNull("stateConfirmedTs is not null", pillsDispensedVo.getStateConfirmedTs() );
		
		byte[] imagePngAfter = PillsDispensedDao.findImageBytesByPillsDispensedUuid(masterConfig, pillsDispensedUuid);
		Assert.assertNotNull("imagePngAfter is null", imagePngAfter );
		Assert.assertEquals("imagePng length", imagePngBefore.length, imagePngAfter.length );		
	}

	
	/**
	 * Testd update dispensing to dispensed delta lt zero.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testdUpdateDispensingToDispensedDeltaLtZero() throws Exception {
		PillsDispensedDao.deleteImageBytesByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		int targetDelta = -1;
		int targetNumDispensed = numToDispense-1;
		byte[] imagePngBefore = TestCommonHandler.fileToByteArray(System.getenv("PATH_TEST_PNG"));
		PillsDispensedDao.updateDispensingToDispensed(masterConfig, pillsDispensedUuid, targetNumDispensed, targetDelta, imagePngBefore);
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.DISPENSED.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertEquals("numDispensed", targetNumDispensed, pillsDispensedVo.getNumDispensed().intValue() );
		Assert.assertEquals("delta", targetDelta, pillsDispensedVo.getDelta().intValue() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNotNull("stateDispensingTs is null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNotNull("stateDispensedTs is not null", pillsDispensedVo.getStateDispensedTs() );
		Assert.assertNull("stateConfirmedTs is not null", pillsDispensedVo.getStateConfirmedTs() );
		byte[] imagePngAfter = PillsDispensedDao.findImageBytesByPillsDispensedUuid(masterConfig, pillsDispensedUuid);
		Assert.assertNotNull("imagePngAfter is null", imagePngAfter );
		Assert.assertEquals("imagePng length", imagePngBefore.length, imagePngAfter.length );		
	}
	

	/**
	 * Teste update dispensing to dispensed delta gt zero.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testeUpdateDispensingToDispensedDeltaGtZero() throws Exception {
		PillsDispensedDao.deleteImageBytesByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		int targetDelta = 1;
		int targetNumDispensed = numToDispense+1;
		byte[] imagePngBefore = TestCommonHandler.fileToByteArray(System.getenv("PATH_TEST_PNG"));
		PillsDispensedDao.updateDispensingToDispensed(masterConfig, pillsDispensedUuid, targetNumDispensed, targetDelta, imagePngBefore);
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.DISPENSED.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertEquals("numDispensed", targetNumDispensed, pillsDispensedVo.getNumDispensed().intValue() );
		Assert.assertEquals("delta", targetDelta, pillsDispensedVo.getDelta().intValue() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNotNull("stateDispensingTs is null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNotNull("stateDispensedTs is null", pillsDispensedVo.getStateDispensedTs() );
		Assert.assertNull("stateConfirmedTs is not null", pillsDispensedVo.getStateConfirmedTs() );
		byte[] imagePngAfter = PillsDispensedDao.findImageBytesByPillsDispensedUuid(masterConfig, pillsDispensedUuid);
		Assert.assertNotNull("imagePngAfter is null", imagePngAfter );
		Assert.assertEquals("imagePng length", imagePngBefore.length, imagePngAfter.length );		
	}
	
	
	/**
	 * Testf update dispensed to confirmed.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testfUpdateDispensedToConfirmed() throws Exception {
		PillsDispensedDao.deleteImageBytesByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		int targetDelta = 0;
		int targetNumDispensed = numToDispense;
		byte[] imagePngBefore = TestCommonHandler.fileToByteArray(System.getenv("PATH_TEST_PNG"));
		PillsDispensedDao.updateDispensingToDispensed(masterConfig, pillsDispensedUuid, targetNumDispensed, targetDelta, imagePngBefore);
		PillsDispensedDao.updateDispensedToConfirmed(masterConfig, pillsDispensedUuid);
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );		
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );
		Assert.assertEquals("dispenseState", DispenseState.CONFIRMED.toString(), pillsDispensedVo.getDispenseState() );
		Assert.assertEquals("numDispensed", targetNumDispensed, pillsDispensedVo.getNumDispensed().intValue() );
		Assert.assertEquals("delta", targetDelta, pillsDispensedVo.getDelta().intValue() );
		Assert.assertNotNull("statePendingTs is null", pillsDispensedVo.getStatePendingTs() );
		Assert.assertNotNull("stateDispensingTs is null", pillsDispensedVo.getStateDispensingTs() );
		Assert.assertNotNull("stateDispensedTs is null", pillsDispensedVo.getStateDispensedTs() );
		Assert.assertNotNull("stateConfirmedTs is null", pillsDispensedVo.getStateConfirmedTs() );
		
		byte[] imagePngAfter = PillsDispensedDao.findImageBytesByPillsDispensedUuid(masterConfig, pillsDispensedUuid);
		Assert.assertNotNull("imagePngAfter is null", imagePngAfter );
		Assert.assertEquals("imagePng length", imagePngBefore.length, imagePngAfter.length );		
	}

	
	/**
	 * Testg find pending.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testgFindPending() throws Exception {
		PillsDispensedDao.deleteImageBytesByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		PillsDispensedDao.deletePillsDispensedByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		PillsDispensedDao.insertPending( masterConfig, pillsDispensedUuid, loginName, sourceName, actuatorName, numToDispense );
		List<PillsDispensedVo> pillsDispensedVos = PillsDispensedDao.sqlFindByDispenseState(masterConfig, DispenseState.PENDING );
		Assert.assertEquals("pillsDispensedVos.size() == 1", 1, pillsDispensedVos.size() );
		PillsDispensedVo pillsDispensedVo = pillsDispensedVos.get(0);
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
		Assert.assertNull("stateDispensedTs is not null", pillsDispensedVo.getStateDispensedTs() );
		Assert.assertNull("stateConfirmedTs is not null", pillsDispensedVo.getStateConfirmedTs() );
	}
	
	/**
	 * Before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		TestPillsDispensedDao.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestPillsDispensedDao.pillsDispensedUuid = UUID.randomUUID().toString();
		logger.info(">>> Common pillsDispensedUuid=" + pillsDispensedUuid);
	}
	

	/**
	 * After class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void afterClass() throws Exception {
		logger.info(">>> Cleaning up");
		PillsDispensedDao.deleteImageBytesByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		PillsDispensedDao.deletePillsDispensedByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
	}
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
}
