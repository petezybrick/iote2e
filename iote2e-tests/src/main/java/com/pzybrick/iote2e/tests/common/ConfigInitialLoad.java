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
package com.pzybrick.iote2e.tests.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.reflect.TypeToken;
import com.pzybrick.iote2e.common.persist.CassandraBaseDao;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.persist.ConfigVo;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.stream.svc.ActuatorState;
import com.pzybrick.iote2e.stream.svc.RuleLoginSourceSensor;


/**
 * The Class ConfigInitialLoad.
 */
public class ConfigInitialLoad {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ConfigInitialLoad.class);


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			ConfigInitialLoad configInitialLoad = new ConfigInitialLoad();
			configInitialLoad.initialLoad( args[0] );
		} catch( Exception e ) {
			logger.error(e.getLocalizedMessage(), e);
			System.exit(8);
		}
	}
	
	/**
	 * Initial load.
	 *
	 * @param pathToConfigFiles the path to config files
	 * @throws Exception the exception
	 */
	public void initialLoad(String pathToConfigFiles) throws Exception {
		try {
			List<ConfigVo> configVos = new ArrayList<ConfigVo>();
			if( !pathToConfigFiles.endsWith("/" ) ) pathToConfigFiles = pathToConfigFiles + "/";
			CassandraBaseDao.connect(System.getenv("CASSANDRA_CONTACT_POINT"), "iote2e");
			ConfigDao.dropKeyspace("iote2e");
			ConfigDao.createKeyspace("iote2e", "SimpleStrategy", 3);			
			ConfigDao.useKeyspace("iote2e");
			ConfigDao.dropTable();
			ConfigDao.createTable();
			File path = new File( pathToConfigFiles);
			File[] files = path.listFiles();
			for( File file : files ) {
				if( file.isFile() ) {
					InputStream inputStream = new FileInputStream(file);
					String configJson = getStringFromInputStream(inputStream);
					inputStream.close();
					String configName = file.getName().substring( 0, file.getName().indexOf("."));
					configVos.add( new ConfigVo(configName, configJson));
				}
			}
			ConfigDao.insertConfigBatch(configVos);
			ActuatorStateDao.dropTable();
			ActuatorStateDao.createTable();
			String rawJson = ConfigDao.findConfigJson("actuator_state");
			List<ActuatorState> actuatorStates = ActuatorStateDao.createActuatorStatesFromJson(rawJson);
			ActuatorStateDao.insertActuatorStateBatch(actuatorStates);
		} catch( Exception e ) {
			throw e;
		} finally {
			ConfigDao.disconnect();
			ActuatorStateDao.disconnect();
		}
	}

	
	/**
	 * Gets the string from input stream.
	 *
	 * @param is the is
	 * @return the string from input stream
	 */
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
