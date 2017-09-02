package com.pzybrick.iote2e.stream.bdbb;

import java.nio.ByteBuffer;
import java.util.List;

import com.pzybrick.iote2e.common.config.MasterConfig;

public interface BdbbRouterHandler {
	public void processRequests( List<ByteBuffer> byteBuffers ) throws Exception;
	public void init( MasterConfig masterConfig ) throws Exception;
}
