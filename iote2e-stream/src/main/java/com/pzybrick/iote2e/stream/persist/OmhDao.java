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

import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.HeartRate;
import org.openmhealth.schema.domain.omh.PhysicalActivity;
import org.openmhealth.schema.domain.omh.RespiratoryRate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pzybrick.iote2e.common.config.MasterConfig;


/**
 * The Class OmhDao.
 */
public abstract class OmhDao {
	
	/*
	 * TODO: This is a hack, ok for small number of vo's, needs to be refactored
	 */

	/**
	 * Insert batch.
	 *
	 * @param con the con
	 * @param dataPoint the data point
	 * @param objectMapper the object mapper
	 * @param rawBody the raw body
	 * @throws Exception the exception
	 */
	// TODO: having issues with generics on Body after sending over kafka, gets unmarshalled into java.util.LinkedHashMap for some reason
	public static void insertBatch( Connection con, DataPoint dataPoint, ObjectMapper objectMapper, String rawBody ) throws Exception {
		if (BloodGlucose.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			BloodGlucose  bloodGlucose = objectMapper.readValue(rawBody, BloodGlucose.class);
			BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo(dataPoint.getHeader(), bloodGlucose );
			BloodGlucoseDao.insertBatchMode(con, bloodGlucoseVo);
		} else if (BloodPressure.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			BloodPressure bloodPressure = objectMapper.readValue(rawBody, BloodPressure.class);
			BloodPressureVo bloodPressureVo = new BloodPressureVo(dataPoint.getHeader(), bloodPressure );
			BloodPressureDao.insertBatchMode(con, bloodPressureVo);
		} else if ("body-temperature".equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			BodyTemperature bodyTemperature = objectMapper.readValue(rawBody, BodyTemperature.class);
			BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo(dataPoint.getHeader(), bodyTemperature );
			BodyTemperatureDao.insertBatchMode(con, bodyTemperatureVo);
		} else if (HeartRate.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			HeartRate heartRate = objectMapper.readValue(rawBody, HeartRate.class);
			HeartRateVo heartRateVo = new HeartRateVo(dataPoint.getHeader(), heartRate );
			HeartRateDao.insertBatchMode(con, heartRateVo);
		} else if (PhysicalActivity.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			PhysicalActivity physicalActivity = objectMapper.readValue(rawBody, PhysicalActivity.class);
			HkWorkoutVo hkWorkoutVo = new HkWorkoutVo(dataPoint.getHeader(), physicalActivity );
			HkWorkoutDao.insertBatchMode(con, hkWorkoutVo);
		} else if ("respiratory-rate".equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			RespiratoryRate respiratoryRate = objectMapper.readValue(rawBody, RespiratoryRate.class);
			RespiratoryRateVo RespiratoryRateVo = new RespiratoryRateVo(dataPoint.getHeader(), respiratoryRate );
			RespiratoryRateDao.insertBatchMode(con, RespiratoryRateVo);
		}
	}

	
	/**
	 * Insert each.
	 *
	 * @param masterConfig the master config
	 * @param dataPoint the data point
	 * @param objectMapper the object mapper
	 * @param rawBody the raw body
	 * @throws Exception the exception
	 */
	public static void insertEach( MasterConfig masterConfig, DataPoint dataPoint, ObjectMapper objectMapper, String rawBody ) throws Exception {
		if (BloodGlucose.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			BloodGlucose  bloodGlucose = objectMapper.readValue(rawBody, BloodGlucose.class);
			BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo(dataPoint.getHeader(), bloodGlucose );
			BloodGlucoseDao.insert(masterConfig, bloodGlucoseVo);
		} else if (BloodPressure.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			BloodPressure bloodPressure = objectMapper.readValue(rawBody, BloodPressure.class);
			BloodPressureVo bloodPressureVo = new BloodPressureVo(dataPoint.getHeader(), bloodPressure );
			BloodPressureDao.insert(masterConfig, bloodPressureVo);
		} else if ("body-temperature".equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			BodyTemperature bodyTemperature = objectMapper.readValue(rawBody, BodyTemperature.class);
			BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo(dataPoint.getHeader(), bodyTemperature );
			BodyTemperatureDao.insert(masterConfig, bodyTemperatureVo);
		} else if (HeartRate.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			HeartRate heartRate = objectMapper.readValue(rawBody, HeartRate.class);
			HeartRateVo heartRateVo = new HeartRateVo(dataPoint.getHeader(), heartRate );
			HeartRateDao.insert(masterConfig, heartRateVo);
		} else if (PhysicalActivity.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			PhysicalActivity physicalActivity = objectMapper.readValue(rawBody, PhysicalActivity.class);
			HkWorkoutVo hkWorkoutVo = new HkWorkoutVo(dataPoint.getHeader(), physicalActivity );
			HkWorkoutDao.insert(masterConfig, hkWorkoutVo);
		} else if ("respiratory-rate".equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			RespiratoryRate respiratoryRate = objectMapper.readValue(rawBody, RespiratoryRate.class);
			RespiratoryRateVo RespiratoryRateVo = new RespiratoryRateVo(dataPoint.getHeader(), respiratoryRate );
			RespiratoryRateDao.insert(masterConfig, RespiratoryRateVo);
		}		
	}
	
	
	/**
	 * Insert batch.
	 *
	 * @param con the con
	 * @param dataPoint the data point
	 * @throws Exception the exception
	 */
	public static void insertBatch( Connection con, DataPoint dataPoint ) throws Exception {
		if (BloodGlucose.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<BloodGlucose> dpBody = dataPoint;
			BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo(dpBody.getHeader(), dpBody.getBody());
			BloodGlucoseDao.insertBatchMode(con, bloodGlucoseVo);
		} else if (BloodPressure.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<BloodPressure> dpBody = dataPoint;
			BloodPressureVo bloodPressureVo = new BloodPressureVo(dpBody.getHeader(), dpBody.getBody());
			BloodPressureDao.insertBatchMode(con, bloodPressureVo);
		} else if ("body-temperature".equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<BodyTemperature> dpBody = dataPoint;
			BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo(dpBody.getHeader(), dpBody.getBody());
			BodyTemperatureDao.insertBatchMode(con, bodyTemperatureVo);
		} else if (HeartRate.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<HeartRate> dpBody = dataPoint;
			HeartRateVo heartRateVo = new HeartRateVo(dpBody.getHeader(), dpBody.getBody());
			HeartRateDao.insertBatchMode(con, heartRateVo);
		} else if (PhysicalActivity.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<PhysicalActivity> dpBody = dataPoint;
			HkWorkoutVo hkWorkoutVo = new HkWorkoutVo(dpBody.getHeader(), dpBody.getBody());
			HkWorkoutDao.insertBatchMode(con, hkWorkoutVo);
		} else if ("respiratory-rate".equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<RespiratoryRate> dpBody = dataPoint;
			RespiratoryRateVo RespiratoryRateVo = new RespiratoryRateVo(dpBody.getHeader(), dpBody.getBody());
			RespiratoryRateDao.insertBatchMode(con, RespiratoryRateVo);
		}
	}

	
	/**
	 * Insert each.
	 *
	 * @param masterConfig the master config
	 * @param dataPoint the data point
	 * @throws Exception the exception
	 */
	public static void insertEach( MasterConfig masterConfig, DataPoint dataPoint ) throws Exception {
		if (BloodGlucose.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<BloodGlucose> dpBody = dataPoint;
			BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo(dpBody.getHeader(), dpBody.getBody());
			BloodGlucoseDao.insert(masterConfig, bloodGlucoseVo);
		} else if (BloodPressure.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<BloodPressure> dpBody = dataPoint;
			BloodPressureVo bloodPressureVo = new BloodPressureVo(dpBody.getHeader(), dpBody.getBody());
			BloodPressureDao.insert(masterConfig, bloodPressureVo);
		} else if ("body-temperature".equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<BodyTemperature> dpBody = dataPoint;
			BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo(dpBody.getHeader(), dpBody.getBody());
			BodyTemperatureDao.insert(masterConfig, bodyTemperatureVo);
		} else if (HeartRate.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<HeartRate> dpBody = dataPoint;
			HeartRateVo heartRateVo = new HeartRateVo(dpBody.getHeader(), dpBody.getBody());
			HeartRateDao.insert(masterConfig, heartRateVo);
		} else if (PhysicalActivity.SCHEMA_ID.getName().equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<PhysicalActivity> dpBody = dataPoint;
			HkWorkoutVo hkWorkoutVo = new HkWorkoutVo(dpBody.getHeader(), dpBody.getBody());
			HkWorkoutDao.insert(masterConfig, hkWorkoutVo);
		} else if ("respiratory-rate".equals(dataPoint.getHeader().getBodySchemaId().getName())) {
			DataPoint<RespiratoryRate> dpBody = dataPoint;
			RespiratoryRateVo RespiratoryRateVo = new RespiratoryRateVo(dpBody.getHeader(), dpBody.getBody());
			RespiratoryRateDao.insert(masterConfig, RespiratoryRateVo);
		}		
	}
}
