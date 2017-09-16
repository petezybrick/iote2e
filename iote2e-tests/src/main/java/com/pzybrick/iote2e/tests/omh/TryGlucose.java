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
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToMeal.FASTING;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToSleep.BEFORE_SLEEPING;
// import static org.openmhealth.schema.domain.omh.TimeFrameFactory.FIXED_MONTH;

import java.time.OffsetDateTime;

import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodGlucoseUnit;
import org.openmhealth.schema.domain.omh.TimeFrame;
import org.openmhealth.schema.domain.omh.TypedUnitValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.utils.CompressionUtils;


/**
 * The Class TryGlucose.
 */
public class TryGlucose {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			String testJson = 
				"{\"effective_time_frame\":{\"date_time\":1499546066.953000000},\"descriptive_statistic\":\"median\",\"user_notes\":\"feeling fine\",\"blood_glucose\":{\"unit\":\"mg/dL\",\"value\":110},\"blood_specimen_type\":\"whole blood\",\"temporal_relationship_to_meal\":\"fasting\",\"temporal_relationship_to_sleep\":\"before sleeping\"}";
			System.out.println("orginal length: " + testJson.length() );
			byte[] compressed = CompressionUtils.compress(testJson.getBytes());
			System.out.println("compressed length: " + compressed.length);
			String decompressed = CompressionUtils.decompress(compressed).toString();
			System.out.println("decompressed: " + decompressed);
	        TypedUnitValue<BloodGlucoseUnit> bloodGlucoseLevel = new TypedUnitValue<>(MILLIGRAMS_PER_DECILITER, 110);
	        TimeFrame timeFrame = new TimeFrame(OffsetDateTime.now() );
	        BloodGlucose bloodGlucose = new BloodGlucose.Builder(bloodGlucoseLevel)
	                .setBloodSpecimenType(WHOLE_BLOOD)
	                .setTemporalRelationshipToMeal(FASTING)
	                .setTemporalRelationshipToSleep(BEFORE_SLEEPING)
	                .setEffectiveTimeFrame(timeFrame)
	                // .setEffectiveTimeFrame()
	                .setDescriptiveStatistic(MEDIAN)
	                .setUserNotes("feeling fine")
	                .build();
	        ObjectMapper objectMapper = new ObjectMapper();
//	        SimpleModule rfc3339Module = new SimpleModule("rfc3339Module");
//	        rfc3339Module.addSerializer(new Rfc3339OffsetDateTimeSerializer(OffsetDateTime.class));
//	        objectMapper.registerModule(rfc3339Module);
	        objectMapper.registerModule(new JavaTimeModule());
	        
	        String rawJson = objectMapper.writeValueAsString(bloodGlucose);
	        System.out.println(rawJson);
	        
	        BloodGlucose second = objectMapper.readValue(rawJson, BloodGlucose.class);
	        System.out.println(second.getEffectiveTimeFrame().getDateTime().toString());
	        System.out.println( objectMapper.writeValueAsString(second));
		} catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

}
