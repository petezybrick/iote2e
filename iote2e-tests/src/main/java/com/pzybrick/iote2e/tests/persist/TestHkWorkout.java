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
import com.pzybrick.iote2e.stream.persist.HkWorkoutDao;
import com.pzybrick.iote2e.stream.persist.HkWorkoutVo;

import org.junit.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHkWorkout {
	private static MasterConfig masterConfig;
	private static String uuid;


	@Test
	public void test001Insert() throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		HkWorkoutVo hkWorkoutVo = new HkWorkoutVo()
			 .setHkWorkoutUuid(uuid)
			 .setHdrSourceName("aaa")
			 .setHdrSourceCreationDateTime(now)
			 .setHdrUserId("bbb")
			 .setHdrModality("ccc")
			 .setHdrSchemaNamespace("ddd")
			 .setHdrSchemaVersion("eee")
			 .setEffectiveTimeFrame(now)
			 .setUserNotes("ggg")
			 .setActivityName("hhh")
			 .setDistanceUnit("iii")
			 .setDistanceValue(123)
			 .setKcalBurnedUnit("jjj")
			 .setKcalBurnedValue(456);
		HkWorkoutDao.insert(masterConfig, hkWorkoutVo);
		HkWorkoutVo foundHkWorkoutVo = HkWorkoutDao.findByPk(masterConfig, hkWorkoutVo);
		Assert.assertTrue( "insert HkWorkoutVo", hkWorkoutVo.equals(foundHkWorkoutVo));
	}

	@Test
	public void test002Delete() throws Exception {
		HkWorkoutVo hkWorkoutVo = new HkWorkoutVo()
				 .setHkWorkoutUuid(uuid);
		HkWorkoutDao.deleteByPk(masterConfig, hkWorkoutVo);
		HkWorkoutVo foundHkWorkoutVo = HkWorkoutDao.findByPk(masterConfig, hkWorkoutVo);
		Assert.assertNull( "delete HkWorkoutVo", foundHkWorkoutVo );
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestHkWorkout.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestHkWorkout.uuid = UUID.randomUUID().toString();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
}
