package com.pzybrick.iote2e.stream.omh;

import java.nio.ByteBuffer;
import java.util.List;

import com.pzybrick.iote2e.common.config.MasterConfig;

public interface OmhRouterHandler {
	public void processRequests( List<ByteBuffer> byteBuffers ) throws Exception;
	public void init( MasterConfig masterConfig ) throws Exception;
}
