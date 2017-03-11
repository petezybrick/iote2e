package com.pzybrick.iote2e.stream.request;

import java.util.List;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public interface Iote2eRequestRouterHandler {
	public void processRequests( List<Iote2eRequest> iote2eRequests ) throws Exception;
	public void init( MasterConfig masterConfig ) throws Exception;
}
