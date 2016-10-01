package com.pzybrick.iote2e.ws.route;

import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;

public interface RouteLoginSourceSensorValue {
	public void routeToTarget( LoginSourceSensorValue loginSourceSensorValue ) throws Exception;
}
