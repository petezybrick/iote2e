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
import com.pzybrick.iote2e.stream.persist.BloodPressureDao;
import com.pzybrick.iote2e.stream.persist.BloodPressureVo;

import org.junit.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBloodPressure {
	private static MasterConfig masterConfig;
	private static String uuid;


	@Test
	public void test001Insert() throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		BloodPressureVo bloodPressureVo = new BloodPressureVo()
			 .setBloodPressureUuid(uuid)
			 .setHdrSourceName("aaa")
			 .setHdrSourceCreationDateTime(now)
			 .setHdrUserId("bbb")
			 .setHdrModality("ccc")
			 .setHdrSchemaNamespace("ddd")
			 .setHdrSchemaVersion("eee")
			 .setEffectiveTimeFrame(now)
			 .setDescriptiveStatistic("fff")
			 .setUserNotes("ggg")
			 .setPositionDuringMeasurement("hhh")
			 .setSystolicBloodPressureUnit("jjj")
			 .setSystolicBloodPressureValue(123)
			 .setDiastolicBloodPressureUnit("kkk")
			 .setDiastolicBloodPressureValue(456);
		BloodPressureDao.insert(masterConfig, bloodPressureVo);
		BloodPressureVo foundBloodPressureVo = BloodPressureDao.findByPk(masterConfig, bloodPressureVo);
		Assert.assertTrue( "insert BloodPressureVo", bloodPressureVo.equals(foundBloodPressureVo));
	}

	@Test
	public void test002Delete() throws Exception {
		BloodPressureVo bloodPressureVo = new BloodPressureVo()
				 .setBloodPressureUuid(uuid);
		BloodPressureDao.deleteByPk(masterConfig, bloodPressureVo);
		BloodPressureVo foundBloodPressureVo = BloodPressureDao.findByPk(masterConfig, bloodPressureVo);
		Assert.assertNull( "delete BloodPressureVo", foundBloodPressureVo );
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestBloodPressure.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestBloodPressure.uuid = UUID.randomUUID().toString();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
}
