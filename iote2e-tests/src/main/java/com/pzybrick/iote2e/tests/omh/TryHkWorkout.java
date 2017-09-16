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


/**
 * The Class TryHkWorkout.
 */
public class TryHkWorkout {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
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
