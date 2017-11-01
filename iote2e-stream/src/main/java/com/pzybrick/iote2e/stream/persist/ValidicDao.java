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
package com.pzybrick.iote2e.stream.persist;

import java.sql.Connection;

import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.DataPoint;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.validic.Biometric;
import com.pzybrick.iote2e.stream.validic.Diabete;
import com.pzybrick.iote2e.stream.validic.ValidicBody;
import com.pzybrick.iote2e.stream.validic.ValidicMessage;


/**
 * The Class ValidicDao.
 */
public abstract class ValidicDao {
	
	/**
	 * Insert batch.
	 *
	 * @param con the con
	 * @param dataPoint the data point
	 * @throws Exception the exception
	 */
	public static void insertBatch( Connection con, ValidicMessage validicMessage ) throws Exception {
		for( ValidicBody body : validicMessage.getBodies() ) {
			if( body instanceof Biometric ) {
				BloodPressureVo bloodPressureVo = new BloodPressureVo(validicMessage.getHeader(), (Biometric)body);
				BloodPressureDao.insertBatchMode(con, bloodPressureVo);

				BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo(validicMessage.getHeader(), (Biometric)body);
				BodyTemperatureDao.insertBatchMode(con, bodyTemperatureVo);
				
			} else if( body instanceof Diabete ) {
				BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo(validicMessage.getHeader(), (Diabete)body);
				BloodGlucoseDao.insertBatchMode(con, bloodGlucoseVo);
			}
		} 
	}

	
	/**
	 * Insert each.
	 *
	 * @param masterConfig the master config
	 * @param dataPoint the data point
	 * @throws Exception the exception
	 */
	public static void insertEach( MasterConfig masterConfig, ValidicMessage validicMessage ) throws Exception {
		for( ValidicBody body : validicMessage.getBodies() ) {
			if( body instanceof Biometric ) {
				BloodPressureVo bloodPressureVo = new BloodPressureVo(validicMessage.getHeader(), (Biometric)body);
				BloodPressureDao.insert(masterConfig, bloodPressureVo);

				BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo(validicMessage.getHeader(), (Biometric)body);
				BodyTemperatureDao.insert(masterConfig, bodyTemperatureVo);

			} else if( body instanceof Diabete ) {
				BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo(validicMessage.getHeader(), (Diabete)body);
				BloodGlucoseDao.insert(masterConfig, bloodGlucoseVo);
			}
		}
	}
}
