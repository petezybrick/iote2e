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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.persist.ConfigVo;




/**
 * The Class TestConfigDao.
 */
public class TestConfigDao {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestConfigDao.class);
	
	/** The Constant TEST_KEYSPACE_NAME. */
	private static final String TEST_KEYSPACE_NAME = "iote2e";
	
	/** The Constant TEST_KEYSPACE_REPLICATION_STRATEGY. */
	private static final String TEST_KEYSPACE_REPLICATION_STRATEGY = "SimpleStrategy";
	
	/** The Constant TEST_KEYSPACE_REPLICATION_FACTOR. */
	private static final int TEST_KEYSPACE_REPLICATION_FACTOR = 3;


	/**
	 * Test sequence.
	 */
	@Test
	public void testSequence() {
		try {
			//Iote2eCommonDao.dropKeyspace(TEST_KEYSPACE_NAME);
			//Iote2eCommonDao.createKeyspace(TEST_KEYSPACE_NAME, TEST_KEYSPACE_REPLICATION_STRATEGY, TEST_KEYSPACE_REPLICATION_FACTOR);
			ConfigDao.useKeyspace(TEST_KEYSPACE_NAME);
			ConfigDao.dropTable();
			ConfigDao.isTableExists(TEST_KEYSPACE_NAME);
			
			ConfigDao.createTable();

			ConfigDao.insertConfig( createConfigSingle());
			ConfigDao.insertConfigBatch( createConfigBatch());
			
			String pk = "testName2b";
			String configJson = ConfigDao.findConfigJson(pk);
			logger.info(configJson);
			Assert.assertEquals("test json", "testJson2b", configJson );
			
			logger.info("configJson - before update {}", configJson );
			ConfigDao.updateConfigJson(pk, "testJson2bb");
			configJson = ConfigDao.findConfigJson(pk);
			logger.info("configJson - after update {}", configJson );
			Assert.assertEquals("configJson after update", "testJson2bb", configJson);
			
			ConfigDao.deleteRow(pk);
			configJson = ConfigDao.findConfigJson(pk);
			logger.info("configJson after delete: {}",configJson);
			Assert.assertNull("configJson after delete", configJson);
			
			long cntBefore = ConfigDao.count();
			logger.info("cntBefore {}", cntBefore);
			Assert.assertEquals("cnt before truncate", 4, cntBefore );
			ConfigDao.truncate();
			long cntAfter = ConfigDao.count();
			logger.info("cntAfter {}", cntAfter);
			Assert.assertEquals("cnt after truncate", 0, cntAfter );
			
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage());			
		} finally {
			ConfigDao.disconnect();
		}
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

	
	/**
	 * Creates the config single.
	 *
	 * @return the config vo
	 */
	private static ConfigVo createConfigSingle() {
		return new ConfigVo("testName1","testJson1");
	}
	
	/**
	 * Creates the config batch.
	 *
	 * @return the list
	 */
	private static List<ConfigVo> createConfigBatch() {
		List<ConfigVo> configVos = new ArrayList<ConfigVo>();
		configVos.add( new ConfigVo("testName2a","testJson2a"));
		configVos.add( new ConfigVo("testName2b","testJson2b"));
		configVos.add( new ConfigVo("testName2c","testJson2c"));
		configVos.add( new ConfigVo("testName2d","testJson2d"));
		return configVos;
	}

}
