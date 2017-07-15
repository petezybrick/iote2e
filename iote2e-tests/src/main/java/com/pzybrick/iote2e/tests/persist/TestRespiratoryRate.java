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
import com.pzybrick.iote2e.stream.persist.RespiratoryRateDao;
import com.pzybrick.iote2e.stream.persist.RespiratoryRateVo;

import org.junit.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRespiratoryRate {
	private static MasterConfig masterConfig;
	private static String uuid;


	@Test
	public void test001Insert() throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		RespiratoryRateVo respiratoryRateVo = new RespiratoryRateVo()
			 .setRespiratoryRateUuid(uuid)
			 .setHdrSourceName("aaa")
			 .setHdrSourceCreationDateTime(now)
			 .setHdrUserId("bbb")
			 .setHdrModality("ccc")
			 .setHdrSchemaNamespace("ddd")
			 .setHdrSchemaVersion("eee")
			 .setEffectiveTimeFrame(now)
			 .setUserNotes("ggg")
			 .setDescriptiveStatistic("hhh")
			 .setTemporalRelationshipToPhysicalActivity("iii")
			 .setRespiratoryRateUnit("jjj")
			 .setRespiratoryRateValue(123);
		RespiratoryRateDao.insert(masterConfig, respiratoryRateVo);
		RespiratoryRateVo foundRespiratoryRateVo = RespiratoryRateDao.findByPk(masterConfig, respiratoryRateVo);
		Assert.assertTrue( "insert RespiratoryRateVo", respiratoryRateVo.equals(foundRespiratoryRateVo));
	}

	@Test
	public void test002Delete() throws Exception {
		RespiratoryRateVo respiratoryRateVo = new RespiratoryRateVo()
				 .setRespiratoryRateUuid(uuid);
		RespiratoryRateDao.deleteByPk(masterConfig, respiratoryRateVo);
		RespiratoryRateVo foundRespiratoryRateVo = RespiratoryRateDao.findByPk(masterConfig, respiratoryRateVo);
		Assert.assertNull( "delete RespiratoryRateVo", foundRespiratoryRateVo );
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestRespiratoryRate.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestRespiratoryRate.uuid = UUID.randomUUID().toString();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
}
