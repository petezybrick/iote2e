package com.pzybrick.iote2e.tests.omh;

import static java.math.BigDecimal.ONE;
import static org.openmhealth.schema.domain.omh.DescriptiveStatistic.MEDIAN;

import java.time.OffsetDateTime;

import org.openmhealth.schema.domain.omh.KcalUnit;
import org.openmhealth.schema.domain.omh.KcalUnitValue;
import org.openmhealth.schema.domain.omh.LengthUnit;
import org.openmhealth.schema.domain.omh.LengthUnitValue;
import org.openmhealth.schema.domain.omh.PhysicalActivity;
import org.openmhealth.schema.domain.omh.TimeFrame;
import org.openmhealth.schema.domain.omh.TypedUnitValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TryHkWorkout {

	public static void main(String[] args) {
		try {
	        LengthUnitValue distance = new LengthUnitValue(LengthUnit.MILE, ONE);
	        KcalUnitValue caloriesBurned = new KcalUnitValue(KcalUnit.KILOCALORIE, 15.7);
	        TimeFrame timeFrame = new TimeFrame(OffsetDateTime.now() );
	        PhysicalActivity physicalActivity = new PhysicalActivity.Builder("HKWorkoutActivityTypeCycling")
	                .setDistance(distance)
	                .setEffectiveTimeFrame(timeFrame)
	                .setCaloriesBurned(caloriesBurned)
	                .build();
			
			
			
			
//	        TypedUnitValue<HKWorkoutActivityUnit> hkWorkoutActivityUnit = new TypedUnitValue<>(HKWorkoutActivityUnit.CYCLING,123);
//	        TimeFrame timeFrame = new TimeFrame(OffsetDateTime.now() );
//	        HKWorkoutActivity hkWorkoutActivity = new HKWorkoutActivity.Builder(hkWorkoutActivityUnit)
//	                .setEffectiveTimeFrame(timeFrame)
//	                // .setEffectiveTimeFrame()
//	                .setDescriptiveStatistic(MEDIAN)
//	                .setUserNotes("feeling fine")
//	                .build();
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.registerModule(new JavaTimeModule());
	        
	        String rawJson = objectMapper.writeValueAsString(physicalActivity);
	        System.out.println(rawJson);
	        

		} catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

}
