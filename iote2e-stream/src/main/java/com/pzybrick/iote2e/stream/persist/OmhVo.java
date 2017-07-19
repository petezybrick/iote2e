package com.pzybrick.iote2e.stream.persist;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.openmhealth.schema.domain.omh.DataPointHeader;

public abstract class OmhVo {
	protected String hdrSourceName;
	protected Timestamp hdrSourceCreationDateTime;
	protected String hdrUserId;
	protected String hdrModality;
	protected String hdrSchemaNamespace;
	protected String hdrSchemaVersion;
	
	public static long offsetDateTimeToMillis( OffsetDateTime offsetDateTime ) {
		return offsetDateTime.atZoneSameInstant(ZoneId.of("Z")).toInstant().toEpochMilli();
	}
	
	protected void setHeaderCommon( DataPointHeader header ) {
		this.hdrSourceName = header.getAcquisitionProvenance().getSourceName();
		this.hdrSourceCreationDateTime = new Timestamp( offsetDateTimeToMillis(header.getAcquisitionProvenance().getSourceCreationDateTime()) ) ;
		this.hdrUserId = header.getUserId();
		this.hdrModality =  header.getAcquisitionProvenance().getModality().name();
		this.hdrSchemaNamespace = header.getSchemaId().getNamespace();
		this.hdrSchemaVersion = header.getSchemaId().getVersion().toString();
	}


	public String getHdrSourceName() {
		return hdrSourceName;
	}

	public Timestamp getHdrSourceCreationDateTime() {
		return hdrSourceCreationDateTime;
	}

	public String getHdrUserId() {
		return hdrUserId;
	}

	public String getHdrModality() {
		return hdrModality;
	}

	public String getHdrSchemaNamespace() {
		return hdrSchemaNamespace;
	}

	public String getHdrSchemaVersion() {
		return hdrSchemaVersion;
	}

}
