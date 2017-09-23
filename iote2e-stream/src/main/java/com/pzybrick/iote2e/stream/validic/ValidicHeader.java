package com.pzybrick.iote2e.stream.validic;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidicHeader {
	@JsonProperty("uuid")
    private String uuid;
	@JsonProperty("user_id")
    private String userId;
	@JsonProperty("start_date_time")
    private OffsetDateTime startDateTime;
	@JsonProperty("end_date_time")
    private OffsetDateTime endDateTime;
	@JsonProperty("creation_date_time")
    private OffsetDateTime creationDateTime;
	@JsonProperty("additional_properties")
    private Map<String, Object> additionalProperties = new HashMap<>();
    
	public String getUuid() {
		return uuid;
	}
	public String getUserId() {
		return userId;
	}
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public OffsetDateTime getCreationDateTime() {
		return creationDateTime;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public ValidicHeader setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}
	public ValidicHeader setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	public ValidicHeader setCreationDateTime(OffsetDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
		return this;
	}
	public ValidicHeader setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
		return this;
	}
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public OffsetDateTime getEndDateTime() {
		return endDateTime;
	}
	public ValidicHeader setEndDateTime(OffsetDateTime endDateTime) {
		this.endDateTime = endDateTime;
		return this;
	}
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public OffsetDateTime getStartDateTime() {
		return startDateTime;
	}
	public ValidicHeader setStartDateTime(OffsetDateTime startDateTime) {
		this.startDateTime = startDateTime;
		return this;
	}
	@Override
	public String toString() {
		return "ValidicHeader [uuid=" + uuid + ", userId=" + userId + ", startDateTime=" + startDateTime
				+ ", endDateTime=" + endDateTime + ", creationDateTime=" + creationDateTime + ", additionalProperties="
				+ additionalProperties + "]";
	}
    
    
}
