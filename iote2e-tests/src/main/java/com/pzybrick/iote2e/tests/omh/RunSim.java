package com.pzybrick.iote2e.tests.omh;

import java.nio.ByteBuffer;
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
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBloodGlucoseImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBloodPressureImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBodyTempImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaHeartRateImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaHkWorkoutImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaRespiratoryRateImpl;

public class RunSim {
	private static final Logger logger = LogManager.getLogger(RunSim.class);
	private static long SLEEP_INTERVAL_MS = 1000L;
	private Map<String,SimSchema> simSchemasByName;
	private List<String> sortedSimSchemaNames;
	private Map<String,Object> prevBodiesByNameLogin;
	private String simUsersFilePath;
	private Integer simUsersOffset;
	private Integer simUsersNumUsers;
	private String wsEndpoint;
	private List<OmhUser> subsetOmhUsers;
	private ClientSocketOmhHandler clientSocketOmhHandler;

	
	public static void main(String[] args) {
		try {
			// Args: simUsersFilePath; simUsersOffset; simUsersNumUsers wsEndpoint
			// Args: "iote2e-shared/data/simOmhUsers.csv" 1 5 "ws://localhost:8092/omh/"
			ClientSocketOmhHandler clientSocketOmhHandler = new ClientSocketOmhHandler().setUrl(args[3]);
			RunSim runSim = new RunSim( ).setSimUsersFilePath(args[0]).setSimUsersOffset( Integer.parseInt(args[1]) )
					.setSimUsersNumUsers( Integer.parseInt(args[2]) ).setWsEndpoint(args[3])
					.setClientSocketOmhHandler(clientSocketOmhHandler);					
			runSim.process();
		} catch(Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public RunSim() throws Exception {
		this.prevBodiesByNameLogin = new HashMap<String,Object>();
		this.simSchemasByName = new HashMap<String,SimSchema>();
		this.simSchemasByName.put(BloodGlucose.SCHEMA_ID.getName(), new SimSchemaBloodGlucoseImpl());
		this.simSchemasByName.put(BloodPressure.SCHEMA_ID.getName(), new SimSchemaBloodPressureImpl());
		// for some reason AmbientTemperature SCHEMA_ID is private
		this.simSchemasByName.put( "body-temperature" , new SimSchemaBodyTempImpl());
		this.simSchemasByName.put(HeartRate.SCHEMA_ID.getName(), new SimSchemaHeartRateImpl());
		this.simSchemasByName.put(PhysicalActivity.SCHEMA_ID.getName(), new SimSchemaHkWorkoutImpl());
		// for some reason RespiratoryRate SCHEMA_ID is private
		this.simSchemasByName.put( "respiratory-rate" , new SimSchemaRespiratoryRateImpl());
		this.sortedSimSchemaNames = new ArrayList<String>( this.simSchemasByName.keySet());
		Collections.sort(this.sortedSimSchemaNames);
	}
	
	public void process() throws Exception {
		this.subsetOmhUsers = SimOmhUsers.getInstance(simUsersFilePath).getOmhUsers().subList(simUsersOffset, simUsersOffset + simUsersNumUsers );
		clientSocketOmhHandler.connect();
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
					clientSocketOmhHandler.session.getBasicRemote().sendBinary(ByteBuffer.wrap(compressed));
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

	public Map<String, SimSchema> getSimSchemasByName() {
		return simSchemasByName;
	}

	public List<String> getSortedSimSchemaNames() {
		return sortedSimSchemaNames;
	}

	public Map<String, Object> getPrevBodiesByNameLogin() {
		return prevBodiesByNameLogin;
	}

	public String getSimUsersFilePath() {
		return simUsersFilePath;
	}

	public Integer getSimUsersOffset() {
		return simUsersOffset;
	}

	public Integer getSimUsersNumUsers() {
		return simUsersNumUsers;
	}

	public String getWsEndpoint() {
		return wsEndpoint;
	}

	public List<OmhUser> getSubsetOmhUsers() {
		return subsetOmhUsers;
	}

	public RunSim setSimSchemasByName(Map<String, SimSchema> simSchemasByName) {
		this.simSchemasByName = simSchemasByName;
		return this;
	}

	public RunSim setSortedSimSchemaNames(List<String> sortedSimSchemaNames) {
		this.sortedSimSchemaNames = sortedSimSchemaNames;
		return this;
	}

	public RunSim setPrevBodiesByNameLogin(Map<String, Object> prevBodiesByNameLogin) {
		this.prevBodiesByNameLogin = prevBodiesByNameLogin;
		return this;
	}

	public RunSim setSimUsersFilePath(String simUsersFilePath) {
		this.simUsersFilePath = simUsersFilePath;
		return this;
	}

	public RunSim setSimUsersOffset(Integer simUsersOffset) {
		this.simUsersOffset = simUsersOffset;
		return this;
	}

	public RunSim setSimUsersNumUsers(Integer simUsersNumUsers) {
		this.simUsersNumUsers = simUsersNumUsers;
		return this;
	}

	public RunSim setWsEndpoint(String wsEndpoint) {
		this.wsEndpoint = wsEndpoint;
		return this;
	}

	public RunSim setSubsetOmhUsers(List<OmhUser> subsetOmhUsers) {
		this.subsetOmhUsers = subsetOmhUsers;
		return this;
	}

	public ClientSocketOmhHandler getClientSocketOmhHandler() {
		return clientSocketOmhHandler;
	}

	public RunSim setClientSocketOmhHandler(ClientSocketOmhHandler clientSocketOmhHandler) {
		this.clientSocketOmhHandler = clientSocketOmhHandler;
		return this;
	}

}
