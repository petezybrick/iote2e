package com.pzybrick.iote2e.tests.pilldisp;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.stream.persist.PillsDispensedDao;

public class DumpPillsDispensedImages {

	public static void main(String[] args) {
		try {
			String masterConfigKey = args[0];
			String contactPoint = args[1];
			String keyspaceName = args[2];
			
			String tmpDir = System.getProperty("java.io.tmpdir") + "/";
			MasterConfig masterConfig = MasterConfig.getInstance( masterConfigKey, contactPoint, keyspaceName );
			List<String> allUuids = PillsDispensedDao.findAllPillsDispensedUuids( masterConfig );
			for( String uuid : allUuids ) {
				byte[] bytes =  PillsDispensedDao.findImageBytesByPillsDispensedUuid(masterConfig, uuid);
				String pathNameExt = tmpDir + uuid + ".png";
				System.out.println("Writing: " + pathNameExt );
				FileOutputStream fos = new FileOutputStream(new File(pathNameExt));
				fos.write(bytes);
				fos.close();
			}
			ConfigDao.connect(contactPoint, keyspaceName);
			System.out.println();
		} catch(Exception e) {
			System.out.println(e);
		}

	}

}
