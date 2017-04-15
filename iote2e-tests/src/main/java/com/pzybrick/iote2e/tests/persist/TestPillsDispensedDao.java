package com.pzybrick.iote2e.tests.persist;

import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.PillsDispensedDao;
import com.pzybrick.iote2e.stream.persist.PillsDispensedVo;

public class TestPillsDispensedDao {
	private static MasterConfig masterConfig;
	private static String pillsDispensedUuid;

	
	@Test
	public void testInsertPending() throws Exception {
		String loginName = "pzybrick1";
		String actuatorName = "pilldisp1";
		int numToDispense = 2;
		PillsDispensedDao.insertPending( masterConfig, pillsDispensedUuid, loginName, actuatorName, numToDispense );
		PillsDispensedVo pillsDispensedVo = PillsDispensedDao.findByPillsDispensedUuid( masterConfig, pillsDispensedUuid );
		Assert.assertNotNull("pillsDispensedVo is null", pillsDispensedVo );
		Assert.assertEquals("pillsDispensedUuid", pillsDispensedUuid, pillsDispensedVo.getPillsDispensedUuid() );
		Assert.assertEquals("loginName", loginName, pillsDispensedVo.getLoginName() );
		Assert.assertEquals("actuatorName", actuatorName, pillsDispensedVo.getActuatorName() );
		Assert.assertEquals("numToDispense", numToDispense, pillsDispensedVo.getNumToDispense().intValue() );

	}
	
	//@Test
	public void testUpdatePendingToInProgress() {
		fail("Not yet implemented");
	}
	
	//@Test
	public void testUpdateDispensingToComplete() {
		fail("Not yet implemented");
	}
	

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestPillsDispensedDao.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestPillsDispensedDao.pillsDispensedUuid = UUID.randomUUID().toString();
		System.out.println(">>> Common pillsDispensedUuid=" + pillsDispensedUuid);
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
}
