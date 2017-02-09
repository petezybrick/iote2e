package com.pzybrick.iote2e.tests.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.persist.ConfigVo;

public class ConfigInitialLoad {
	private static final Logger logger = LogManager.getLogger(ConfigInitialLoad.class);


	public static void main(String[] args) {
		try {
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
