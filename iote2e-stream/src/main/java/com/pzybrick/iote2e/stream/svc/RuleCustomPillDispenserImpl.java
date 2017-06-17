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

public class RuleCustomPillDispenserImpl implements RuleCustom {
	private static final Logger logger = LogManager.getLogger(RuleCustomPillDispenserImpl.class);
	private MasterConfig masterConfig;
	
	public void setMasterConfig( MasterConfig masterConfig ) {
		this.masterConfig = masterConfig;
	}

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
