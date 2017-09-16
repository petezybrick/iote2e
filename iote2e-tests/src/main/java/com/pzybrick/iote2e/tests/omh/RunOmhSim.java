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


/**
 * The Class RunOmhSim.
 */
public class RunOmhSim {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RunOmhSim.class);
	
	/** The sleep interval ms. */
	private static long SLEEP_INTERVAL_MS = 1000L;
	
	/** The sim schemas by name. */
	private Map<String,SimSchema> simSchemasByName;
	
	/** The sorted sim schema names. */
	private List<String> sortedSimSchemaNames;
	
	/** The prev bodies by name login. */
	private Map<String,Object> prevBodiesByNameLogin;
	
	/** The sim users file path. */
	private String simUsersFilePath;
	
	/** The sim users offset. */
	private Integer simUsersOffset;
	
	/** The sim users num users. */
	private Integer simUsersNumUsers;
	
	/** The ws endpoint. */
	private String wsEndpoint;
	
	/** The max loops. */
	private Integer maxLoops;
	
	/** The subset omh users. */
	private List<OmhUser> subsetOmhUsers;
	
	/** The client socket omh handler. */
	private ClientSocketOmhHandler clientSocketOmhHandler;

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			// Args: simUsersFilePath; simUsersOffset; simUsersNumUsers wsEndpoint
			// Args: "iote2e-shared/data/simOmhUsers.csv" 1 5 "ws://localhost:8092/omh/"
			ClientSocketOmhHandler clientSocketOmhHandler = new ClientSocketOmhHandler().setUrl(args[4]);
			RunOmhSim runOmhSim = new RunOmhSim( ).setSimUsersFilePath(args[0]).setSimUsersOffset( Integer.parseInt(args[1]) )
					.setSimUsersNumUsers( Integer.parseInt(args[2]) ).setMaxLoops(Integer.parseInt(args[3])).setWsEndpoint(args[4])
					.setClientSocketOmhHandler(clientSocketOmhHandler);					
			runOmhSim.process();
		} catch(Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Instantiates a new run omh sim.
	 *
	 * @throws Exception the exception
	 */
	public RunOmhSim() throws Exception {
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
	
	/**
	 * Process.
	 *
	 * @throws Exception the exception
	 */
	public void process() throws Exception {
		this.subsetOmhUsers = SimOmhUsers.getInstance(simUsersFilePath).getOmhUsers().subList(simUsersOffset, simUsersOffset + simUsersNumUsers );
		clientSocketOmhHandler.connect();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        int numLoops = 0;
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
					logger.debug("length before: {}, after: {}", rawJson.length(), compressed.length);
					logger.debug("rawJson {}", rawJson );
				}
			}
			try {
				long msSleep = wakeupAt - System.currentTimeMillis();
				System.out.println(msSleep);
				Thread.sleep(msSleep);
			} catch(Exception e ) {}
			numLoops++;
			if( maxLoops > 0 && numLoops == maxLoops ) break;
			
		}
	}

	/**
	 * Gets the sim schemas by name.
	 *
	 * @return the sim schemas by name
	 */
	public Map<String, SimSchema> getSimSchemasByName() {
		return simSchemasByName;
	}

	/**
	 * Gets the sorted sim schema names.
	 *
	 * @return the sorted sim schema names
	 */
	public List<String> getSortedSimSchemaNames() {
		return sortedSimSchemaNames;
	}

	/**
	 * Gets the prev bodies by name login.
	 *
	 * @return the prev bodies by name login
	 */
	public Map<String, Object> getPrevBodiesByNameLogin() {
		return prevBodiesByNameLogin;
	}

	/**
	 * Gets the sim users file path.
	 *
	 * @return the sim users file path
	 */
	public String getSimUsersFilePath() {
		return simUsersFilePath;
	}

	/**
	 * Gets the sim users offset.
	 *
	 * @return the sim users offset
	 */
	public Integer getSimUsersOffset() {
		return simUsersOffset;
	}

	/**
	 * Gets the sim users num users.
	 *
	 * @return the sim users num users
	 */
	public Integer getSimUsersNumUsers() {
		return simUsersNumUsers;
	}

	/**
	 * Gets the ws endpoint.
	 *
	 * @return the ws endpoint
	 */
	public String getWsEndpoint() {
		return wsEndpoint;
	}

	/**
	 * Gets the subset omh users.
	 *
	 * @return the subset omh users
	 */
	public List<OmhUser> getSubsetOmhUsers() {
		return subsetOmhUsers;
	}

	/**
	 * Sets the sim schemas by name.
	 *
	 * @param simSchemasByName the sim schemas by name
	 * @return the run omh sim
	 */
	public RunOmhSim setSimSchemasByName(Map<String, SimSchema> simSchemasByName) {
		this.simSchemasByName = simSchemasByName;
		return this;
	}

	/**
	 * Sets the sorted sim schema names.
	 *
	 * @param sortedSimSchemaNames the sorted sim schema names
	 * @return the run omh sim
	 */
	public RunOmhSim setSortedSimSchemaNames(List<String> sortedSimSchemaNames) {
		this.sortedSimSchemaNames = sortedSimSchemaNames;
		return this;
	}

	/**
	 * Sets the prev bodies by name login.
	 *
	 * @param prevBodiesByNameLogin the prev bodies by name login
	 * @return the run omh sim
	 */
	public RunOmhSim setPrevBodiesByNameLogin(Map<String, Object> prevBodiesByNameLogin) {
		this.prevBodiesByNameLogin = prevBodiesByNameLogin;
		return this;
	}

	/**
	 * Sets the sim users file path.
	 *
	 * @param simUsersFilePath the sim users file path
	 * @return the run omh sim
	 */
	public RunOmhSim setSimUsersFilePath(String simUsersFilePath) {
		this.simUsersFilePath = simUsersFilePath;
		return this;
	}

	/**
	 * Sets the sim users offset.
	 *
	 * @param simUsersOffset the sim users offset
	 * @return the run omh sim
	 */
	public RunOmhSim setSimUsersOffset(Integer simUsersOffset) {
		this.simUsersOffset = simUsersOffset;
		return this;
	}

	/**
	 * Sets the sim users num users.
	 *
	 * @param simUsersNumUsers the sim users num users
	 * @return the run omh sim
	 */
	public RunOmhSim setSimUsersNumUsers(Integer simUsersNumUsers) {
		this.simUsersNumUsers = simUsersNumUsers;
		return this;
	}

	/**
	 * Sets the ws endpoint.
	 *
	 * @param wsEndpoint the ws endpoint
	 * @return the run omh sim
	 */
	public RunOmhSim setWsEndpoint(String wsEndpoint) {
		this.wsEndpoint = wsEndpoint;
		return this;
	}

	/**
	 * Sets the subset omh users.
	 *
	 * @param subsetOmhUsers the subset omh users
	 * @return the run omh sim
	 */
	public RunOmhSim setSubsetOmhUsers(List<OmhUser> subsetOmhUsers) {
		this.subsetOmhUsers = subsetOmhUsers;
		return this;
	}

	/**
	 * Gets the client socket omh handler.
	 *
	 * @return the client socket omh handler
	 */
	public ClientSocketOmhHandler getClientSocketOmhHandler() {
		return clientSocketOmhHandler;
	}

	/**
	 * Sets the client socket omh handler.
	 *
	 * @param clientSocketOmhHandler the client socket omh handler
	 * @return the run omh sim
	 */
	public RunOmhSim setClientSocketOmhHandler(ClientSocketOmhHandler clientSocketOmhHandler) {
		this.clientSocketOmhHandler = clientSocketOmhHandler;
		return this;
	}

	/**
	 * Gets the max loops.
	 *
	 * @return the max loops
	 */
	public Integer getMaxLoops() {
		return maxLoops;
	}

	/**
	 * Sets the max loops.
	 *
	 * @param maxLoops the max loops
	 * @return the run omh sim
	 */
	public RunOmhSim setMaxLoops(Integer maxLoops) {
		this.maxLoops = maxLoops;
		return this;
	}

}
