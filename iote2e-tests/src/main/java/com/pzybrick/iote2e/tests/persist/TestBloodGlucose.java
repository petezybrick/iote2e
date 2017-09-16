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


/**
 * The Class TestBloodGlucose.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBloodGlucose {
	
	/** The master config. */
	private static MasterConfig masterConfig;
	
	/** The uuid. */
	private static String uuid;


	/**
	 * Test 001 insert.
	 *
	 * @throws Exception the exception
	 */
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

	/**
	 * Test 002 delete.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void test002Delete() throws Exception {
		BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo()
				 .setBloodGlucoseUuid(uuid);
		BloodGlucoseDao.deleteByPk(masterConfig, bloodGlucoseVo);
		BloodGlucoseVo foundBloodGlucoseVo = BloodGlucoseDao.findByPk(masterConfig, bloodGlucoseVo);
		Assert.assertNull( "delete BloodGlucoseVo", foundBloodGlucoseVo );
	}

	/**
	 * Before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		TestBloodGlucose.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestBloodGlucose.uuid = UUID.randomUUID().toString();
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
