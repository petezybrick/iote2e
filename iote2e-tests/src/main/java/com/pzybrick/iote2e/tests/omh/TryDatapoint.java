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

import static java.time.ZoneOffset.UTC;
import static org.openmhealth.schema.domain.omh.BloodGlucoseUnit.MILLIGRAMS_PER_DECILITER;
import static org.openmhealth.schema.domain.omh.BloodSpecimenType.WHOLE_BLOOD;
import static org.openmhealth.schema.domain.omh.DescriptiveStatistic.MEDIAN;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToMeal.FASTING;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToSleep.BEFORE_SLEEPING;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodGlucoseUnit;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.DataPointAcquisitionProvenance;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.DataPointModality;
import org.openmhealth.schema.domain.omh.SchemaId;
import org.openmhealth.schema.domain.omh.TimeFrame;
import org.openmhealth.schema.domain.omh.TypedUnitValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * The Class TryDatapoint.
 */
public class TryDatapoint {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
	        String id = UUID.randomUUID().toString();
	        OffsetDateTime now = OffsetDateTime.now();
	        
	        DataPointHeader header = new DataPointHeader.Builder(id, BloodGlucose.SCHEMA_ID, now)
	        		.setUserId("testUser001")
	                .setAcquisitionProvenance(
	                        new DataPointAcquisitionProvenance.Builder("RunKeeper")
	                                .setSourceCreationDateTime(now)
	                                .setModality(DataPointModality.SENSED)
	                                .build()
	                )
	                .build();
			
	        TypedUnitValue<BloodGlucoseUnit> bloodGlucoseLevel = new TypedUnitValue<>(MILLIGRAMS_PER_DECILITER, 110);
	        BloodGlucose bloodGlucose = new BloodGlucose.Builder(bloodGlucoseLevel)
	                .setBloodSpecimenType(WHOLE_BLOOD)
	                .setTemporalRelationshipToMeal(FASTING)
	                .setTemporalRelationshipToSleep(BEFORE_SLEEPING)
	                .setEffectiveTimeFrame( new TimeFrame(now) )
	                // .setEffectiveTimeFrame()
	                .setDescriptiveStatistic(MEDIAN)
	                .setUserNotes("feeling fine")
	                .build();
	        
	        DataPoint<BloodGlucose> dataPoint = new DataPoint<BloodGlucose>(header, bloodGlucose);
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.registerModule(new JavaTimeModule());
	        
	        String rawJson = objectMapper.writeValueAsString(dataPoint);
	        System.out.println(rawJson);
	        
	        DataPoint after = objectMapper.readValue(rawJson, DataPoint.class);
	        System.out.println( after.getHeader().getBodySchemaId().getName() + " " + after.getHeader().getBodySchemaId().getVersion() );
	        System.out.println( after.getHeader().getUserId() );
	        System.out.println(after.getBody());
	        String rawJsonBody = objectMapper.writeValueAsString(after.getBody());
	        
	        BloodGlucose bgAfter = objectMapper.readValue(rawJsonBody, BloodGlucose.class);
	        System.out.println(bgAfter.getUserNotes());
	        
//			System.out.println("orginal length: " + rawJson.length() );
//			byte[] compressed = CompressionUtils.compress(rawJson.getBytes());
//			System.out.println("compressed length: " + compressed.length);

		} catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
		}

	}
}
