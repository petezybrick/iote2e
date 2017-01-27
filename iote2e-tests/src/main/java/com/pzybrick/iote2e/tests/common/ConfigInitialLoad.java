package com.pzybrick.iote2e.tests.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.pzybrick.iote2e.ruleproc.persist.ConfigDao;
import com.pzybrick.iote2e.ruleproc.persist.ConfigVo;

public class ConfigInitialLoad {
	private static final Logger logger = LogManager.getLogger(ConfigInitialLoad.class);
	private static final String[] CONFIG_FILES_TO_LOAD = {
			"actuator_state.json",
			"master_basic_unit_test_local_config.json",
			"master_ignite_unit_test_local_config.json",
			"master_kafka_unit_test_local_config.json",
			"master_ksi_unit_test_local_config.json",
			"master_basic_unit_test_docker_config.json",
			"master_ignite_unit_test_docker_config.json",
			"master_kafka_unit_test_docker_config.json",
			"master_ksi_unit_test_docker_config.json",
			"rule_def_item.json",
			"rule_login_source_sensor.json",
			};

	public static void main(String[] args) {
		try {
			int x = org.apache.ignite.events.EventType.EVT_TASK_STARTED;
			ConfigInitialLoad configInitialLoad = new ConfigInitialLoad();
			configInitialLoad.initialLoad( args[0] );
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			System.exit(8);
		}
	}
	
	public void initialLoad(String pathToConfigFiles) throws Exception {
		try {
			List<ConfigVo> configVos = new ArrayList<ConfigVo>();
			if( !pathToConfigFiles.endsWith("/" ) ) pathToConfigFiles = pathToConfigFiles + "/";
			try {
				ConfigDao.createKeyspace("iote2e", "SimpleStrategy", 3);
			} catch( Exception e ) {
				if( e.getCause() instanceof com.datastax.driver.core.exceptions.AlreadyExistsException )
					logger.warn("Keyspace iote2e already exists");
				else throw e;
			}
			
			ConfigDao.useKeyspace("iote2e");
			ConfigDao.dropTable();
			ConfigDao.createTable();
			for( String configFileName : CONFIG_FILES_TO_LOAD ) {
				InputStream inputStream = new FileInputStream(pathToConfigFiles + configFileName );
				String configJson = getStringFromInputStream(inputStream);
				inputStream.close();
				String configName = configFileName.substring( 0, configFileName.indexOf("."));
				configVos.add( new ConfigVo(configName, configJson));
			}
			ConfigDao.insertConfigBatch(configVos);
		} catch( Exception e ) {
			throw e;
		} finally {
			ConfigDao.disconnect();
		}
	}
	
	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();

	}

}
