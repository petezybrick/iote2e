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
package com.pzybrick.iote2e.stream.svc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.stream.persist.PillsDispensedDao;
import com.pzybrick.iote2e.stream.persist.PillsDispensedVo;
import com.pzybrick.iote2e.stream.pilldisp.PillDispenser;


/**
 * The Class RuleCustomPillDispenserImpl.
 */
public class RuleCustomPillDispenserImpl implements RuleCustom {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RuleCustomPillDispenserImpl.class);
	
	/** The master config. */
	private MasterConfig masterConfig;
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.svc.RuleCustom#setMasterConfig(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	public void setMasterConfig( MasterConfig masterConfig ) {
		this.masterConfig = masterConfig;
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.svc.RuleCustom#ruleEval(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, com.pzybrick.iote2e.schema.avro.Iote2eRequest)
	 */
	public List<RuleEvalResult> ruleEval(String loginUuid, String sourceUuid, String sensorName, String sensorValue,
			List<RuleEvalResult> ruleEvalResults, Iote2eRequest iote2eRequest ) throws Exception {
		String pillsDispensedState = iote2eRequest.getMetadata().get(PillDispenser.KEY_PILLS_DISPENSED_STATE).toString();
		CharSequence pillsDispensedUuid = iote2eRequest.getMetadata().get(PillDispenser.KEY_PILLS_DISPENSED_UUID);
		
		if( pillsDispensedState.equals(PillsDispensedVo.DispenseState.DISPENSED.toString() ) ) {
			// Convert Byte64 string to image, then process the image to count the circles, return number of circles as actuator value
			// The pill dispenser will blink LEDs green if delta=0, else blink LEDs red until subject presses button to confirm 
			int numPillsToDispense = -1;
			if( iote2eRequest.getMetadata().containsKey(PillDispenser.KEY_NUM_PILLS_TO_DISPENSE) ){
				numPillsToDispense = Integer.parseInt(iote2eRequest.getMetadata().get(PillDispenser.KEY_NUM_PILLS_TO_DISPENSE).toString() );
			}
			byte[] imageBytesFromB64 = Base64.getDecoder().decode(sensorValue);
			InputStream in = new ByteArrayInputStream(imageBytesFromB64);
			BufferedImage bufferedImage = ImageIO.read(in);
			int numPillsActualDispense = PillDispenser.countPills( bufferedImage );
			// If correct number of pills dispensed, then return 0, if fewer dispensed then negative, if too many then positive
			int delta = numPillsActualDispense - numPillsToDispense;
			logger.debug( ">>>> delta={}, numPillsToDispense={}, numPillsActualDispense={}", delta, numPillsToDispense, numPillsActualDispense );
			String actuatorTargetValue = String.valueOf( delta );
			Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
			metadata.put(PillDispenser.KEY_PILLS_DISPENSED_UUID, pillsDispensedUuid);
			metadata.put(PillDispenser.KEY_PILLS_DISPENSED_STATE, pillsDispensedState);
			RuleEvalResult ruleEvalResult = new RuleEvalResult(sensorName, actuatorTargetValue, metadata);
			ruleEvalResults.add(ruleEvalResult);
			PillsDispensedDao.updateDispensingToDispensed(masterConfig, pillsDispensedUuid.toString(), numPillsActualDispense, delta, imageBytesFromB64);
		
		} else if( pillsDispensedState.equals(PillsDispensedVo.DispenseState.CONFIRMED.toString() ) ) {
			// Subject pressed the button to confirm pills dispensed, now return Iote2eResult to turn off the flashing LED's 
			Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
			metadata.put(PillDispenser.KEY_PILLS_DISPENSED_UUID, pillsDispensedUuid);
			metadata.put(PillDispenser.KEY_PILLS_DISPENSED_STATE, PillsDispensedVo.DispenseState.CONFIRMED.toString());
			RuleEvalResult ruleEvalResult = new RuleEvalResult(sensorName, "off", metadata);
			ruleEvalResults.add(ruleEvalResult);
			PillsDispensedDao.updateDispensedToConfirmed(masterConfig, pillsDispensedUuid.toString() );
		}

		logger.debug(ruleEvalResults);
		return ruleEvalResults;
	}

}
