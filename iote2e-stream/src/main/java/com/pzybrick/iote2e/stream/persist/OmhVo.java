/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.stream.persist;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.openmhealth.schema.domain.omh.DataPointHeader;


/**
 * The Class OmhVo.
 */
public abstract class OmhVo {
	
	/** The hdr source name. */
	protected String hdrSourceName;
	
	/** The hdr source creation date time. */
	protected Timestamp hdrSourceCreationDateTime;
	
	/** The hdr user id. */
	protected String hdrUserId;
	
	/** The hdr modality. */
	protected String hdrModality;
	
	/** The hdr schema namespace. */
	protected String hdrSchemaNamespace;
	
	/** The hdr schema version. */
	protected String hdrSchemaVersion;
	
	/**
	 * Offset date time to millis.
	 *
	 * @param offsetDateTime the offset date time
	 * @return the long
	 */
	public static long offsetDateTimeToMillis( OffsetDateTime offsetDateTime ) {
		return offsetDateTime.atZoneSameInstant(ZoneId.of("Z")).toInstant().toEpochMilli();
	}
	
	/**
	 * Sets the header common.
	 *
	 * @param header the new header common
	 */
	protected void setHeaderCommon( DataPointHeader header ) {
		this.hdrSourceName = header.getAcquisitionProvenance().getSourceName();
		this.hdrSourceCreationDateTime = new Timestamp( offsetDateTimeToMillis(header.getAcquisitionProvenance().getSourceCreationDateTime()) ) ;
		this.hdrUserId = header.getUserId();
		this.hdrModality =  header.getAcquisitionProvenance().getModality().name();
		this.hdrSchemaNamespace = header.getSchemaId().getNamespace();
		this.hdrSchemaVersion = header.getSchemaId().getVersion().toString();
	}


	/**
	 * Gets the hdr source name.
	 *
	 * @return the hdr source name
	 */
	public String getHdrSourceName() {
		return hdrSourceName;
	}

	/**
	 * Gets the hdr source creation date time.
	 *
	 * @return the hdr source creation date time
	 */
	public Timestamp getHdrSourceCreationDateTime() {
		return hdrSourceCreationDateTime;
	}

	/**
	 * Gets the hdr user id.
	 *
	 * @return the hdr user id
	 */
	public String getHdrUserId() {
		return hdrUserId;
	}

	/**
	 * Gets the hdr modality.
	 *
	 * @return the hdr modality
	 */
	public String getHdrModality() {
		return hdrModality;
	}

	/**
	 * Gets the hdr schema namespace.
	 *
	 * @return the hdr schema namespace
	 */
	public String getHdrSchemaNamespace() {
		return hdrSchemaNamespace;
	}

	/**
	 * Gets the hdr schema version.
	 *
	 * @return the hdr schema version
	 */
	public String getHdrSchemaVersion() {
		return hdrSchemaVersion;
	}

}
