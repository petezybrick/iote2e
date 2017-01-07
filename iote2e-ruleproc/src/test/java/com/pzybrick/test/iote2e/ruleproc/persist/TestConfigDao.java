package com.pzybrick.test.iote2e.ruleproc.persist;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pzybrick.iote2e.ruleproc.persist.ConfigDao;
import com.pzybrick.iote2e.ruleproc.persist.ConfigVo;
import com.pzybrick.iote2e.ruleproc.svc.LoginSourceSensorActuator;



public class TestConfigDao {
	private static final Logger logger = LogManager.getLogger(TestConfigDao.class);
	private static final String TEST_KEYSPACE_NAME = "iote2e";
	private static final String TEST_KEYSPACE_REPLICATION_STRATEGY = "SimpleStrategy";
	private static final int TEST_KEYSPACE_REPLICATION_FACTOR = 3;


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
	
	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	
	private static ConfigVo createConfigSingle() {
		return new ConfigVo("testName1","testJson1");
	}
	
	private static List<ConfigVo> createConfigBatch() {
		List<ConfigVo> configVos = new ArrayList<ConfigVo>();
		configVos.add( new ConfigVo("testName2a","testJson2a"));
		configVos.add( new ConfigVo("testName2b","testJson2b"));
		configVos.add( new ConfigVo("testName2c","testJson2c"));
		configVos.add( new ConfigVo("testName2d","testJson2d"));
		return configVos;
	}

}
