package com.pzybrick.iote2e.ws.route;

import java.nio.ByteBuffer;

import com.pzybrick.iote2e.common.config.MasterConfig;

public interface RouteBdbbByteBuffer {
	public void routeToTarget( ByteBuffer byteBuffer ) throws Exception;
	public void init( MasterConfig masterConfig ) throws Exception;
}
