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
package com.pzybrick.iote2e.ws.route;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.ws.socket.ThreadEntryPointIote2eRequest;



/**
 * The Class RouteIote2eRequestLoopbackImpl.
 */
public class RouteIote2eRequestLoopbackImpl implements RouteIote2eRequest {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RouteIote2eRequestLoopbackImpl.class);
	
	/** The master config. */
	private MasterConfig masterConfig;
	
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.ws.route.RouteIote2eRequest#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	@Override
	public void init(MasterConfig masterConfig) throws Exception {
		this.masterConfig = masterConfig;
	}
	
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.ws.route.RouteIote2eRequest#routeToTarget(com.pzybrick.iote2e.schema.avro.Iote2eRequest)
	 */
	public void routeToTarget( Iote2eRequest iote2eRequest ) throws Exception {
		logger.debug(iote2eRequest.toString());
		String actuatorValue = "testActuatorValuea";
		CharSequence sensorName = null;
		if( !iote2eRequest.getPairs().values().isEmpty()) {
			Iterator<Map.Entry<CharSequence, CharSequence>> it = iote2eRequest.getPairs().entrySet().iterator();
			Map.Entry<CharSequence, CharSequence> entry = it.next();
			sensorName = entry.getKey();
			String sensorValue = entry.getValue().toString();
			actuatorValue = actuatorValue + sensorValue;
		}
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME), sensorName );
		pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME), new Utf8("testActuatorNamea"));
		pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE), new Utf8("testActuatorValuea"));
		pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE_UPDATED_AT), new Utf8(Iote2eUtils.getDateNowUtc8601()));

		
		String resultTimestamp = Iote2eUtils.getDateNowUtc8601();
		String resultUuid = UUID.randomUUID().toString();
		Iote2eResult iote2eResult = Iote2eResult.newBuilder()
				.setLoginName(iote2eRequest.getLoginName())
				.setSourceName(iote2eRequest.getSourceName())
				.setSourceType(iote2eRequest.getSourceType())
				.setRequestUuid(iote2eRequest.getRequestUuid())
				.setRequestTimestamp(iote2eRequest.getRequestTimestamp())
				.setOperation(OPERATION.ACTUATOR_VALUES)
				.setResultCode(0)
				.setResultUuid(resultUuid)
				.setResultTimestamp(resultTimestamp)
				.setPairs(pairs)
				.build();
		ThreadEntryPointIote2eRequest.toClientIote2eResults.add(iote2eResult);
	}

}
