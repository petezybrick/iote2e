package com.pzybrick.iote2e.tests.omh;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.DataPointAcquisitionProvenance;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.DataPointModality;
import org.openmhealth.schema.domain.omh.HeartRate;
import org.openmhealth.schema.domain.omh.PhysicalActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.utils.CompressionUtils;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaAmbientTempImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBloodGlucoseImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBloodPressureImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaHeartRateImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaHkWorkoutImpl;

public class RunSim {
	private static final Logger logger = LogManager.getLogger(RunSim.class);
	private static long SLEEP_INTERVAL_MS = 1000L;
	private Map<String,SimSchema> simSchemasByName;
	private List<String> sortedSimSchemaNames;
	private Map<String,Object> prevBodiesByNameLogin;
	private String simUsersFilePath;
	private Integer simUsersOffset;
	private Integer sumUsersNumUsers;
	private List<OmhUser> subsetOmhUsers;

	
	public static void main(String[] args) {
		try {
			String simUsersFilePath = "../iote2e-tests/iote2e-shared/data/simOmhUsers.csv";
			Integer simUsersOffset = 1;
			Integer sumUsersNumUsers = 5;
			RunSim runSim = new RunSim( simUsersFilePath, simUsersOffset, sumUsersNumUsers );
			runSim.process();
		} catch(Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public RunSim(String simUsersFilePath, Integer simUsersOffset, Integer sumUsersNumUsers) throws Exception {
		this.simUsersFilePath = simUsersFilePath;
		this.simUsersOffset = simUsersOffset;
		this.sumUsersNumUsers = sumUsersNumUsers;
		this.prevBodiesByNameLogin = new HashMap<String,Object>();
		this.simSchemasByName = new HashMap<String,SimSchema>();
		// for some reason AmbientTemperature SCHEMA_ID is private
		this.simSchemasByName.put( "ambient-temperature" , new SimSchemaAmbientTempImpl());
		this.simSchemasByName.put(BloodGlucose.SCHEMA_ID.getName(), new SimSchemaBloodGlucoseImpl());
		this.simSchemasByName.put(BloodPressure.SCHEMA_ID.getName(), new SimSchemaBloodPressureImpl());
		this.simSchemasByName.put(HeartRate.SCHEMA_ID.getName(), new SimSchemaHeartRateImpl());
		this.simSchemasByName.put(PhysicalActivity.SCHEMA_ID.getName(), new SimSchemaHkWorkoutImpl());
		this.sortedSimSchemaNames = new ArrayList<String>( this.simSchemasByName.keySet());
		Collections.sort(this.sortedSimSchemaNames);
		this.subsetOmhUsers = SimOmhUsers.getInstance(simUsersFilePath).getOmhUsers().subList(simUsersOffset, simUsersOffset + sumUsersNumUsers );
	}
	
	public void process() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
		while( true ) {
			long wakeupAt = System.currentTimeMillis() + SLEEP_INTERVAL_MS;
			for( OmhUser omhUser : subsetOmhUsers ) {
		        OffsetDateTime now = OffsetDateTime.now();
				for( String schemaName : sortedSimSchemaNames ) {
					SimSchema simSchema = simSchemasByName.get(schemaName);
			        String id = UUID.randomUUID().toString();
			        DataPointHeader header = new DataPointHeader.Builder(id, simSchema.getSchemaId(), now)
			        		.setUserId( omhUser.getEmail() )
			                .setAcquisitionProvenance(
			                        new DataPointAcquisitionProvenance.Builder("RunKeeper")
			                                .setSourceCreationDateTime(now)
			                                .setModality(DataPointModality.SENSED)
			                                .build()
			                )
			                .build();
			        
			        String keyPrevBody = schemaName + "|" + omhUser.getEmail();
			        Object prevBody = prevBodiesByNameLogin.get( keyPrevBody );
			        Object body = simSchema.createBody(now, prevBody);
			        prevBodiesByNameLogin.put( keyPrevBody, body );
			        
			        DataPoint dataPoint = new DataPoint( header, body );
			        String rawJson = objectMapper.writeValueAsString(dataPoint);
					byte[] compressed = CompressionUtils.compress(rawJson.getBytes());
					//System.out.println("length before: " + rawJson.length() + ", after: " + compressed.length);
			        System.out.println(rawJson);
				}
			}
			try {
				long msSleep = wakeupAt - System.currentTimeMillis();
				System.out.println(msSleep);
				Thread.sleep(msSleep);
			} catch(Exception e ) {}
			
		}
	}

}
