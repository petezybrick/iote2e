package com.pzybrick.iote2e.stream.persist;

import java.sql.Connection;

import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.HeartRate;
import org.openmhealth.schema.domain.omh.PhysicalActivity;
import org.openmhealth.schema.domain.omh.RespiratoryRate;

import com.pzybrick.iote2e.common.config.MasterConfig;

public abstract class OmhDao {
	
	/*
	 * TODO: This is a hack, ok for small number of vo's, needs to be refactored
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
