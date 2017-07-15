package com.pzybrick.iote2e.tests.persist;

import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.HeartRateDao;
import com.pzybrick.iote2e.stream.persist.HeartRateVo;

import org.junit.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHeartRate {
	private static MasterConfig masterConfig;
	private static String uuid;


	@Test
	public void test001Insert() throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		HeartRateVo heartRateVo = new HeartRateVo()
			 .setHeartRateUuid(uuid)
			 .setHdrSourceName("aaa")
			 .setHdrSourceCreationDateTime(now)
			 .setHdrUserId("bbb")
			 .setHdrModality("ccc")
			 .setHdrSchemaNamespace("ddd")
			 .setHdrSchemaVersion("eee")
			 .setEffectiveTimeFrame(now)
			 .setUserNotes("ggg")
			 .setTemporalRelationshipToPhysicalActivity("hhh")
			 .setHeartRateUnit("iii")
			 .setHeartRateValue(123);
		HeartRateDao.insert(masterConfig, heartRateVo);
		HeartRateVo foundHeartRateVo = HeartRateDao.findByPk(masterConfig, heartRateVo);
		Assert.assertTrue( "insert HeartRateVo", heartRateVo.equals(foundHeartRateVo));
	}

	@Test
	public void test002Delete() throws Exception {
		HeartRateVo heartRateVo = new HeartRateVo()
				 .setHeartRateUuid(uuid);
		HeartRateDao.deleteByPk(masterConfig, heartRateVo);
		HeartRateVo foundHeartRateVo = HeartRateDao.findByPk(masterConfig, heartRateVo);
		Assert.assertNull( "delete HeartRateVo", foundHeartRateVo );
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestHeartRate.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestHeartRate.uuid = UUID.randomUUID().toString();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
}
