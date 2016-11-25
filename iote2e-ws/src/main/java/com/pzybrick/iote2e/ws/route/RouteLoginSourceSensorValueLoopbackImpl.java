package com.pzybrick.iote2e.ws.route;

import com.pzybrick.iote2e.common.utils.IotE2eUtils;
import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;
import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;
import com.pzybrick.iote2e.ws.socket.EntryPointServerSourceSensorValue;

public class RouteLoginSourceSensorValueLoopbackImpl implements RouteLoginSourceSensorValue {

	public void routeToTarget( LoginSourceSensorValue loginSourceSensorValue) throws Exception {
		LoginActuatorResponse loginActuatorResponse = LoginActuatorResponse.newBuilder()
				.setLoginUuid(loginSourceSensorValue.getLoginUuid())
				.setSourceUuid(loginSourceSensorValue.getSourceUuid())
				.setSensorName(loginSourceSensorValue.getSensorName())
				.setActuatorName(loginSourceSensorValue.getSensorName()+"ac1")
				.setActuatorValueUpdatedAt(IotE2eUtils.getDateNowUtc8601())
				.setActuatorValue(loginSourceSensorValue.getSensorValue())
				.build();
		EntryPointServerSourceSensorValue.toClientActuatorResponses.add(loginActuatorResponse);
	}
}
