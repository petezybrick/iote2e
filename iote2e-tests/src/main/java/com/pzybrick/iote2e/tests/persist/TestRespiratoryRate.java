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
import com.pzybrick.iote2e.stream.persist.RespiratoryRateDao;
import com.pzybrick.iote2e.stream.persist.RespiratoryRateVo;


/**
 * The Class TestRespiratoryRate.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRespiratoryRate {
	
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

	/**
	 * Test 002 delete.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void test002Delete() throws Exception {
		RespiratoryRateVo respiratoryRateVo = new RespiratoryRateVo()
				 .setRespiratoryRateUuid(uuid);
		RespiratoryRateDao.deleteByPk(masterConfig, respiratoryRateVo);
		RespiratoryRateVo foundRespiratoryRateVo = RespiratoryRateDao.findByPk(masterConfig, respiratoryRateVo);
		Assert.assertNull( "delete RespiratoryRateVo", foundRespiratoryRateVo );
	}

	/**
	 * Before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		TestRespiratoryRate.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
		TestRespiratoryRate.uuid = UUID.randomUUID().toString();
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
