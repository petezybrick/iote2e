package com.pzybrick.iote2e.tests.omh;

import java.time.OffsetDateTime;

import org.openmhealth.schema.domain.omh.SchemaId;

public interface SimSchema {
	public Object createBody( OffsetDateTime now, Object prevBody ) throws Exception;
	public SchemaId getSchemaId();
}
