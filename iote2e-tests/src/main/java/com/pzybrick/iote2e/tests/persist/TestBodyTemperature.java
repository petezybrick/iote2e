package com.pzybrick.iote2e.tests.persist;

import java.sql.Timestamp;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.BodyTemperatureDao;
import com.pzybrick.iote2e.stream.persist.BodyTemperatureVo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBodyTemperature {
	private static MasterConfig masterConfig;
	private static String uuid;


	@Test
	public void test001Insert() throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo()
			 .setBodyTemperatureUuid(uuid)
			 .setHdrSourceName("aaa")
			 .setHdrSourceCreationDateTime(now)
			 .setHdrUserId("bbb")
			 .setHdrModality("ccc")
			 .setHdrSchemaNamespace("ddd")
			 .setHdrSchemaVersion("eee")
			 .setEffectiveTimeFrame(now)
			 .setDescriptiveStatistic("fff")
			 .setUserNotes("ggg")
			 .setMeasurementLocation("hhh")
			 .setBodyTemperatureUnit("iii")
			 .setBodyTemperatureValue(123.4F);
		BodyTemperatureDao.insert(masterConfig, bodyTemperatureVo);
		BodyTemperatureVo foundBodyTemperatureVo = BodyTemperatureDao.findByPk(masterConfig, bodyTemperatureVo);
		Assert.assertTrue( "insert BodyTemperatureVo", bodyTemperatureVo.equals(foundBodyTemperatureVo));
	}

	@Test
	public void test002Delete() throws Exception {
		BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo()
				 .setBodyTemperatureUuid(uuid);
		BodyTemperatureDao.deleteByPk(masterConfig, bodyTemperatureVo);
		BodyTemperatureVo foundBodyTemperatureVo = BodyTemperatureDao.findByPk(masterConfig, bodyTemperatureVo);
		Assert.assertNull( "delete BodyTemperatureVo", foundBodyTemperatureVo );
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		TestBodyTemperature.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestBodyTemperature.uuid = UUID.randomUUID().toString();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
}
