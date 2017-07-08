package com.pzybrick.iote2e.stream.omh;

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

public class TryGlucose {

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
