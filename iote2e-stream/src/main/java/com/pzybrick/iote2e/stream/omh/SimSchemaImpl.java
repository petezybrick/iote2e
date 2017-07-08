package com.pzybrick.iote2e.stream.omh;

import static org.openmhealth.schema.domain.omh.BloodGlucoseUnit.MILLIGRAMS_PER_DECILITER;
import static org.openmhealth.schema.domain.omh.BloodSpecimenType.WHOLE_BLOOD;
import static org.openmhealth.schema.domain.omh.DescriptiveStatistic.MEDIAN;
import static org.openmhealth.schema.domain.omh.TemperatureUnit.FAHRENHEIT;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToMeal.FASTING;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToSleep.BEFORE_SLEEPING;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Random;

import org.openmhealth.schema.domain.omh.AmbientTemperature;
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodGlucoseUnit;
import org.openmhealth.schema.domain.omh.TemperatureUnitValue;
import org.openmhealth.schema.domain.omh.TimeFrame;
import org.openmhealth.schema.domain.omh.TypedUnitValue;

public class SimSchemaImpl {
	private static Random random = new Random();
	
	public static class SimSchemaBloodGlucoseImpl implements SimSchema {
		@Override
		public Object createBody( OffsetDateTime now, BigDecimal prevValue ) throws Exception {
			final long mid = 110;
			final long max = 130;
			final long min = 90;
			final long exceed = 170;
			final long incr = 3;
			long value = 0;
			if( prevValue != null ) {
				if( (random.nextInt() % 2) == 1 ) value = prevValue.longValue() + incr;
				else value = prevValue.longValue() - incr;
				if( value < min ) value = min;
				else if( value > max ) value = max;
			} else value = mid;
			// 5% of the time use exceeded value
			if( random.nextInt(20) == 0 ) value = exceed;
			
	        TypedUnitValue<BloodGlucoseUnit> bloodGlucoseLevel = new TypedUnitValue<>(MILLIGRAMS_PER_DECILITER, value);
	        BloodGlucose bloodGlucose = new BloodGlucose.Builder(bloodGlucoseLevel)
	                .setBloodSpecimenType(WHOLE_BLOOD)
	                .setTemporalRelationshipToMeal(FASTING)
	                .setTemporalRelationshipToSleep(BEFORE_SLEEPING)
	                .setEffectiveTimeFrame( new TimeFrame(now) )
	                // .setEffectiveTimeFrame()
	                .setDescriptiveStatistic(MEDIAN)
	                .setUserNotes("feeling fine")
	                .build();
	        return bloodGlucose;
		}
		
	}
	
	public static class SimSchemaAmbientTempImpl implements SimSchema {
		@Override
		public Object createBody( OffsetDateTime now, BigDecimal prevValue ) throws Exception {
			final long mid = 80;
			final long max = 90;
			final long min = 70;
			final long exceed = 105;
			final long incr = 2;
			long value = 0;
			if( prevValue != null ) {
				if( (random.nextInt() % 2) == 1 ) value = prevValue.longValue() + incr;
				else value = prevValue.longValue() - incr;
				if( value < min ) value = min;
				else if( value > max ) value = max;
			} else value = mid;
			// 5% of the time use exceeded value
			if( random.nextInt(20) == 0 ) value = exceed;
	        AmbientTemperature ambientTemperature =
	                new AmbientTemperature.Builder(new TemperatureUnitValue(FAHRENHEIT, value))
	                        .setDescriptiveStatistic(MEDIAN)
	                        .setEffectiveTimeFrame(new TimeFrame(now))
	                        .setUserNotes("Temp is fine")
	                        .build();
	        return ambientTemperature;
		}
		
	}
}
