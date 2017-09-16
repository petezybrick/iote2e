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
package com.pzybrick.iote2e.tests.omh;

import static org.openmhealth.schema.domain.omh.BloodGlucoseUnit.MILLIGRAMS_PER_DECILITER;
import static org.openmhealth.schema.domain.omh.BloodSpecimenType.WHOLE_BLOOD;
import static org.openmhealth.schema.domain.omh.DescriptiveStatistic.MEDIAN;
import static org.openmhealth.schema.domain.omh.TemperatureUnit.FAHRENHEIT;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToMeal.FASTING;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToSleep.BEFORE_SLEEPING;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Random;

import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodGlucoseUnit;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.BloodPressureUnit;
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.BodyTemperature.MeasurementLocation;
import org.openmhealth.schema.domain.omh.DescriptiveStatistic;
import org.openmhealth.schema.domain.omh.DiastolicBloodPressure;
import org.openmhealth.schema.domain.omh.HeartRate;
import org.openmhealth.schema.domain.omh.HeartRateUnit;
import org.openmhealth.schema.domain.omh.KcalUnit;
import org.openmhealth.schema.domain.omh.KcalUnitValue;
import org.openmhealth.schema.domain.omh.LengthUnit;
import org.openmhealth.schema.domain.omh.LengthUnitValue;
import org.openmhealth.schema.domain.omh.PhysicalActivity;
import org.openmhealth.schema.domain.omh.PositionDuringMeasurement;
import org.openmhealth.schema.domain.omh.RespiratoryRate;
import org.openmhealth.schema.domain.omh.RespiratoryRate.RespirationUnit;
import org.openmhealth.schema.domain.omh.SchemaId;
import org.openmhealth.schema.domain.omh.SchemaSupport;
import org.openmhealth.schema.domain.omh.SystolicBloodPressure;
import org.openmhealth.schema.domain.omh.TemperatureUnitValue;
import org.openmhealth.schema.domain.omh.TemporalRelationshipToPhysicalActivity;
import org.openmhealth.schema.domain.omh.TimeFrame;
import org.openmhealth.schema.domain.omh.TypedUnitValue;


/**
 * The Class SimSchemaImpl.
 */
public class SimSchemaImpl {
	
	/** The random. */
	private static Random random = new Random();
	
	/** The Constant MIN_PCT_EXCEED. */
	private static final int MIN_PCT_EXCEED = 2;
	
	/**
	 * The Class SimSchemaBloodGlucoseImpl.
	 */
	public static class SimSchemaBloodGlucoseImpl implements SimSchema {
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#getSchemaId()
		 */
		@Override
		public SchemaId getSchemaId() {
			return BloodGlucose.SCHEMA_ID;
		}
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#createBody(java.time.OffsetDateTime, java.lang.Object)
		 */
		@Override// TODO Auto-generated method stub

		public Object createBody( OffsetDateTime now, Object prevBody ) throws Exception {
			final long mid = 110;
			final long max = 130;
			final long min = 90;
			final long exceed = 170;
			final long incr = 3;
			long value = 0;// TODO Auto-generated method stub

			if( prevBody != null ) {
				long prevValue = ((BloodGlucose)prevBody).getBloodGlucose().getValue().longValue();
				if( (random.nextInt() % 2) == 1 ) value = prevValue + incr;
				else value = prevValue - incr;
				if( value < min ) value = min;
				else if( value > max ) value = max;
			} else value = mid;
			// 5% of the time use exceeded value
			String userNotes = "Feeling fine";
			if( random.nextInt(100) <= MIN_PCT_EXCEED ) {
				value = exceed;
				userNotes = "light headed";
			}
			
	        TypedUnitValue<BloodGlucoseUnit> bloodGlucoseLevel = new TypedUnitValue<>(MILLIGRAMS_PER_DECILITER, value);
	        BloodGlucose bloodGlucose = new BloodGlucose.Builder(bloodGlucoseLevel)
	                .setBloodSpecimenType(WHOLE_BLOOD)
	                .setTemporalRelationshipToMeal(FASTING)
	                .setTemporalRelationshipToSleep(BEFORE_SLEEPING)
	                .setEffectiveTimeFrame( new TimeFrame(now) )
	                // .setEffectiveTimeFrame()
	                .setDescriptiveStatistic(MEDIAN)
	                .setUserNotes(userNotes)
	                .build();
	        return bloodGlucose;
		}
		
	}

	
	/**
	 * The Class SimSchemaBloodPressureImpl.
	 */
	public static class SimSchemaBloodPressureImpl implements SimSchema {
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#getSchemaId()
		 */
		@Override
		public SchemaId getSchemaId() {
			return BloodPressure.SCHEMA_ID;
		}
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#createBody(java.time.OffsetDateTime, java.lang.Object)
		 */
		@Override
		public Object createBody( OffsetDateTime now, Object prevBody ) throws Exception {
			// Systolic
			final long midSys = 120;
			final long maxSys = 130;
			final long minSys = 110;
			final long exceedSys = 150;
			final long incrSys = 5;
			long valueSys = 0;
			if( prevBody != null ) {
				long prevValueSys = ((BloodPressure)prevBody).getSystolicBloodPressure().getValue().longValue();
				if( (random.nextInt() % 2) == 1 ) valueSys = prevValueSys + incrSys;
				else valueSys = prevValueSys - incrSys;
				if( valueSys < minSys ) valueSys = minSys;
				else if( valueSys > maxSys ) valueSys = maxSys;
			} else valueSys = midSys;
			// 5% of the time use exceeded value
			String userNotes = "Feeling fine";
			if( random.nextInt(100) <= MIN_PCT_EXCEED ) {
				valueSys = exceedSys;
				userNotes = "dizzy";
			}
			
			// Diastolic
			final long midDia = 80;
			final long maxDia = 90;
			final long minDia = 70;
			final long exceedDia = 105;
			final long incrDia = 5;
			long valueDia = 0;
			if( prevBody != null ) {
				long prevValueDia = ((BloodPressure)prevBody).getDiastolicBloodPressure().getValue().longValue();
				if( (random.nextInt() % 2) == 1 ) valueDia = prevValueDia + incrDia;
				else valueDia = prevValueDia - incrDia;
				if( valueDia < minDia ) valueDia = minDia;
				else if( valueDia > maxDia ) valueDia = maxDia;
			} else valueDia = midDia;
			// 5% of the time use exceeded value
			if( random.nextInt(100) <= MIN_PCT_EXCEED ) {
				valueDia = exceedDia;
				userNotes = "dizzy";
			}
			
			SystolicBloodPressure systolicBloodPressure = new SystolicBloodPressure(BloodPressureUnit.MM_OF_MERCURY, BigDecimal.valueOf(valueSys));
			DiastolicBloodPressure diastolicBloodPressure = new DiastolicBloodPressure(BloodPressureUnit.MM_OF_MERCURY, BigDecimal.valueOf(valueDia));			
	        BloodPressure bloodPressure = new BloodPressure.Builder(systolicBloodPressure, diastolicBloodPressure)
	                .setPositionDuringMeasurement(PositionDuringMeasurement.SITTING)
	                .setEffectiveTimeFrame(new TimeFrame(now))
	                .setDescriptiveStatistic(MEDIAN)
	                .setUserNotes(userNotes)
	                .build();
	        return bloodPressure;
		}
		
	}
	
	
	/**
	 * The Class SimSchemaBodyTempImpl.
	 */
	public static class SimSchemaBodyTempImpl implements SimSchema {
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#getSchemaId()
		 */
		@Override
		public SchemaId getSchemaId() {
			// for some reason SCHEMA_ID is private for body-temperature
			return new SchemaId(SchemaSupport.OMH_NAMESPACE, "body-temperature", "1.0");
		}
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#createBody(java.time.OffsetDateTime, java.lang.Object)
		 */
		@Override
		public Object createBody( OffsetDateTime now, Object prevBody ) throws Exception {
			final double mid = 98.6;
			final double max = 99.2;
			final double min = 98.0;
			final double exceed = 104;
			final double incr = .2;
			double value = 0;
			if( prevBody != null ) {
				double prevValue = ((BodyTemperature)prevBody).getBodyTemperature().getValue().doubleValue();
				if( (random.nextInt() % 2) == 1 ) value = prevValue + incr;
				else value = prevValue - incr;
				if( value < min ) value = min;
				else if( value > max ) value = max;
			} else value = mid;
			// 5% of the time use exceeded value
			String userNotes = "Feeling fine";
			if( random.nextInt(100) <= MIN_PCT_EXCEED ) {
				value = exceed;
				userNotes = "dizzy";
			}
			BigDecimal bdValue = new BigDecimal(value).setScale(1,  RoundingMode.HALF_UP);
			BodyTemperature bodyTemperature =
	                new BodyTemperature.Builder(new TemperatureUnitValue(FAHRENHEIT, bdValue))
	                        .setDescriptiveStatistic(DescriptiveStatistic.MAXIMUM)
	                        .setMeasurementLocation(MeasurementLocation.ORAL)
	                        .setEffectiveTimeFrame(new TimeFrame(now))
	                        .setUserNotes(userNotes)
	                        .build();
	        return bodyTemperature;
		}
	}
	
	
	/**
	 * The Class SimSchemaHeartRateImpl.
	 */
	public static class SimSchemaHeartRateImpl implements SimSchema {
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#getSchemaId()
		 */
		@Override
		public SchemaId getSchemaId() {
			return HeartRate.SCHEMA_ID;
		}
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#createBody(java.time.OffsetDateTime, java.lang.Object)
		 */
		@Override
		public Object createBody( OffsetDateTime now, Object prevBody ) throws Exception {
			final long mid = 72;
			final long max = 85;
			final long min = 65;
			final long exceed = 100;
			final long incr = 3;
			long value = 0;
			if( prevBody != null ) {
				long prevValue = ((HeartRate)prevBody).getHeartRate().getValue().longValue();
				if( (random.nextInt() % 2) == 1 ) value = prevValue + incr;
				else value = prevValue - incr;
				if( value < min ) value = min;
				else if( value > max ) value = max;
			} else value = mid;
			// 5% of the time use exceeded value
			String userNotes = "Feeling fine";
			if( random.nextInt(100) <= MIN_PCT_EXCEED ) {
				value = exceed;
				userNotes = "dizzy";
			}
			
			TypedUnitValue<HeartRateUnit> heartRateUnit = new TypedUnitValue<>(HeartRateUnit.BEATS_PER_MINUTE, value);
	        HeartRate heartRate = new HeartRate.Builder(heartRateUnit)
	        		.setEffectiveTimeFrame(new TimeFrame(now))
	                .setTemporalRelationshipToPhysicalActivity(TemporalRelationshipToPhysicalActivity.AT_REST)
	                .setUserNotes(userNotes)
	                .build();

	        return heartRate;
		}
		
	}

	
	/**
	 * The Class SimSchemaHkWorkoutImpl.
	 */
	public static class SimSchemaHkWorkoutImpl implements SimSchema {
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#getSchemaId()
		 */
		@Override
		public SchemaId getSchemaId() {
			return PhysicalActivity.SCHEMA_ID;
		}
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#createBody(java.time.OffsetDateTime, java.lang.Object)
		 */
		@Override
		public Object createBody( OffsetDateTime now, Object prevBody ) throws Exception {
			// Distance
			final long midDistance = 15;
			final long maxDistance = 25;
			final long minDistance = 5;
			final long exceedDistance = 50;
			final long incrDistance = 5;
			long valueDistance = 0;
			if( prevBody != null ) {
				long prevValueDistance = ((PhysicalActivity)prevBody).getDistance().getValue().longValue();
				if( (random.nextInt() % 2) == 1 ) valueDistance = prevValueDistance + incrDistance;
				else valueDistance = prevValueDistance - incrDistance;
				if( valueDistance < minDistance ) valueDistance = minDistance;
				else if( valueDistance > maxDistance ) valueDistance = maxDistance;
			} else valueDistance = midDistance;
			// 5% of the time use exceeded value
			if( random.nextInt(100) <= MIN_PCT_EXCEED ) {
				valueDistance = exceedDistance;
			}
			
			// KCals
			final long midKcals = 500;
			final long maxKcals = 1050;
			final long minKcals = 250;
			final long exceedKcals = 2000;
			final long incrKcals = 100;
			long valueKcals = 0;
			if( prevBody != null ) {
				long prevValueKcals = ((PhysicalActivity)prevBody).getCaloriesBurned().getValue().longValue();
				if( (random.nextInt() % 2) == 1 ) valueKcals = prevValueKcals + incrKcals;
				else valueKcals = prevValueKcals - incrKcals;
				if( valueKcals < minKcals ) valueKcals = minKcals;
				else if( valueKcals > maxKcals ) valueKcals = maxKcals;
			} else valueKcals = midKcals;
			// 5% of the time use exceeded value
			String userNotes = "Feeling fine";
			if( random.nextInt(100) <= MIN_PCT_EXCEED ) {
				valueKcals = exceedKcals;
				userNotes = "dizzy";
			}

	        LengthUnitValue distance = new LengthUnitValue(LengthUnit.MILE, new BigDecimal(valueDistance));
	        KcalUnitValue caloriesBurned = new KcalUnitValue(KcalUnit.KILOCALORIE, new BigDecimal(valueKcals));
	        TimeFrame timeFrame = new TimeFrame(OffsetDateTime.now() );
	        PhysicalActivity physicalActivity = new PhysicalActivity.Builder("HKWorkoutActivityTypeCycling")
	        		.setUserNotes(userNotes)
	                .setDistance(distance)
	                .setEffectiveTimeFrame(timeFrame)
	                .setCaloriesBurned(caloriesBurned)
	                .build();

	        return physicalActivity;
		}
	}
	
	
	/**
	 * The Class SimSchemaRespiratoryRateImpl.
	 */
	public static class SimSchemaRespiratoryRateImpl implements SimSchema {
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#getSchemaId()
		 */
		@Override
		public SchemaId getSchemaId() {
			// for some reason SCHEMA_ID is private for respiratory-rate
			return new SchemaId(SchemaSupport.OMH_NAMESPACE, "respiratory-rate", "1.0");
		}
		
		/* (non-Javadoc)
		 * @see com.pzybrick.iote2e.tests.omh.SimSchema#createBody(java.time.OffsetDateTime, java.lang.Object)
		 */
		@Override
		public Object createBody( OffsetDateTime now, Object prevBody ) throws Exception {
			final double mid = 12;
			final double max = 9;
			final double min = 15;
			final double exceed = 23;
			final double incr = .5;
			double value = 0;
			if( prevBody != null ) {
				double prevValue = ((RespiratoryRate)prevBody).getRespiratoryRate().getValue().doubleValue();
				if( (random.nextInt() % 2) == 1 ) value = prevValue + incr;
				else value = prevValue - incr;
				if( value < min ) value = min;
				else if( value > max ) value = max;
			} else value = mid;
			// 5% of the time use exceeded value
			String userNotes = "Feeling fine";
			if( random.nextInt(100) <= MIN_PCT_EXCEED ) {
				value = exceed;
				userNotes = "short of breath";
			}
			RespiratoryRate respiratoryRate =
	                new RespiratoryRate.Builder(new TypedUnitValue<>(RespirationUnit.BREATHS_PER_MINUTE, value))
	                        .setDescriptiveStatistic(DescriptiveStatistic.AVERAGE)
	                        .setTemporalRelationshipToPhysicalActivity(TemporalRelationshipToPhysicalActivity.AT_REST)
	                        .setEffectiveTimeFrame(new TimeFrame(now))
	                        .setUserNotes(userNotes)
	                        .build();
	        return respiratoryRate;
		}
	}
	
}
