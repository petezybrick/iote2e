package com.pzybrick.iote2e.stream.omh;

import static org.openmhealth.schema.domain.omh.BloodGlucoseUnit.MILLIGRAMS_PER_DECILITER;
import static org.openmhealth.schema.domain.omh.BloodSpecimenType.WHOLE_BLOOD;
import static org.openmhealth.schema.domain.omh.DescriptiveStatistic.MEDIAN;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToMeal.FASTING;
import static org.openmhealth.schema.domain.omh.TemporalRelationshipToSleep.BEFORE_SLEEPING;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodGlucoseUnit;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.DataPointAcquisitionProvenance;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.DataPointModality;
import org.openmhealth.schema.domain.omh.TimeFrame;
import org.openmhealth.schema.domain.omh.TypedUnitValue;

import com.pzybrick.iote2e.stream.omh.SimSchemaImpl.SimSchemaAmbientTempImpl;
import com.pzybrick.iote2e.stream.omh.SimSchemaImpl.SimSchemaBloodGlucoseImpl;

public class RunSim {
	private static final Logger logger = LogManager.getLogger(RunSim.class);
	private Map<String,SimSchema> simSchemasByName;

	public static void main(String[] args) {
		try {
			RunSim runSim = new RunSim();
			runSim.process();
		} catch(Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public RunSim() throws Exception {
		simSchemasByName = new HashMap<String,SimSchema>();
		simSchemasByName.put(BloodGlucose.SCHEMA_ID.getName(), new SimSchemaBloodGlucoseImpl());
		// for some reason AmbientTemperature SCHEMA_ID is private
		simSchemasByName.put( "ambient-temperature" , new SimSchemaAmbientTempImpl());
	}
	
	public void process() throws Exception {
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
	}

}
