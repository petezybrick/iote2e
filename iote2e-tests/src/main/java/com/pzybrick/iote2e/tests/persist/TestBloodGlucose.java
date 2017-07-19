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
import com.pzybrick.iote2e.stream.persist.BloodGlucoseDao;
import com.pzybrick.iote2e.stream.persist.BloodGlucoseVo;

import org.junit.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBloodGlucose {
	private static MasterConfig masterConfig;
	private static String uuid;


	@Test
	public void test001Insert() throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		BloodGlucoseVo xxx = new BloodGlucoseVo();
		BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo()
			 .setBloodGlucoseUuid(uuid)
			 .setHdrSourceName("aaa")
			 .setHdrSourceCreationDateTime(now)
			 .setHdrUserId("bbb")
			 .setHdrModality("ccc")
			 .setHdrSchemaNamespace("ddd")
			 .setHdrSchemaVersion("eee")
			 .setEffectiveTimeFrame(now)
			 .setDescriptiveStatistic("fff")
			 .setUserNotes("ggg")
			 .setBloodSpecimenType("hhh")
			 .setTemporalRelationshipToMeal("iii")
			 .setTemporalRelationshipToSleep("jjj")
			 .setBloodGlucoseUnit("kkk")
			 .setBloodGlucoseValue(123);
		BloodGlucoseDao.insert(masterConfig, bloodGlucoseVo);
		BloodGlucoseVo foundBloodGlucoseVo = BloodGlucoseDao.findByPk(masterConfig, bloodGlucoseVo);
		Assert.assertTrue( "insert BloodGlucoseVo", bloodGlucoseVo.equals(foundBloodGlucoseVo));
	}

	@Test
	public void test002Delete() throws Exception {
		BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo()
				 .setBloodGlucoseUuid(uuid);
		BloodGlucoseDao.deleteByPk(masterConfig, bloodGlucoseVo);
		BloodGlucoseVo foundBloodGlucoseVo = BloodGlucoseDao.findByPk(masterConfig, bloodGlucoseVo);
		Assert.assertNull( "delete BloodGlucoseVo", foundBloodGlucoseVo );
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestBloodGlucose.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestBloodGlucose.uuid = UUID.randomUUID().toString();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
}
