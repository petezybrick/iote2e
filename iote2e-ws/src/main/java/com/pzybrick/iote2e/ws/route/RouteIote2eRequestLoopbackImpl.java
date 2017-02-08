package com.pzybrick.iote2e.ws.route;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.avro.util.Utf8;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.ws.socket.EntryPointIote2eRequest;

public class RouteIote2eRequestLoopbackImpl implements RouteIote2eRequest {

	public void routeToTarget( Iote2eRequest iote2eRequest ) throws Exception {
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		pairs.put( new Utf8("testActuatorNamea"),  new Utf8("testActuatorValuea"));
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
		EntryPointIote2eRequest.toClientIote2eResults.add(iote2eResult);
	}
}
