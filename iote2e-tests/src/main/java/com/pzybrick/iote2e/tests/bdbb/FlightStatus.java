package com.pzybrick.iote2e.tests.bdbb;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class FlightStatus {
	@Expose
	private String statusUuid;
	@Expose
	private Long statusTs;
	@Expose
	private String airframeUuid;
	@Expose
	private String flightNumber;
	@Expose
	private String fromAirport;
	@Expose
	private String toAirport;
	@Expose
	private double lat;
	@Expose
	private double lng;
	@Expose
	private float alt;
	@Expose
	private List<EngineStatus> engineStatuss;
	
	
	public String getStatusUuid() {
		return statusUuid;
	}
	public Long getStatusTs() {
		return statusTs;
	}
	public String getAirframeUuid() {
		return airframeUuid;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public String getFromAirport() {
		return fromAirport;
	}
	public String getToAirport() {
		return toAirport;
	}
	public double getLat() {
		return lat;
	}
	public double getLng() {
		return lng;
	}
	public float getAlt() {
		return alt;
	}
	public List<EngineStatus> getEngineStatuss() {
		return engineStatuss;
	}
	public FlightStatus setStatusUuid(String statusUuid) {
		this.statusUuid = statusUuid;
		return this;
	}
	public FlightStatus setStatusTs(Long statusTs) {
		this.statusTs = statusTs;
		return this;
	}
	public FlightStatus setAirframeUuid(String airframeUuid) {
		this.airframeUuid = airframeUuid;
		return this;
	}
	public FlightStatus setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
		return this;
	}
	public FlightStatus setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
		return this;
	}
	public FlightStatus setToAirport(String toAirport) {
		this.toAirport = toAirport;
		return this;	
	}
	public FlightStatus setLat(double lat) {
		this.lat = lat;
		return this;
	}
	public FlightStatus setLng(double lng) {
		this.lng = lng;
		return this;
	}
	public FlightStatus setAlt(float alt) {
		this.alt = alt;
		return this;
	}
	public FlightStatus setEngineStatuss(List<EngineStatus> engineStatuss) {
		this.engineStatuss = engineStatuss;
		return this;
	}
	@Override
	public String toString() {
		return "FlightStatus [statusUuid=" + statusUuid + ", statusTs=" + statusTs + ", airframeUuid=" + airframeUuid
				+ ", flightNumber=" + flightNumber + ", fromAirport=" + fromAirport + ", toAirport=" + toAirport
				+ ", lat=" + lat + ", lng=" + lng + ", alt=" + alt + ", engineStatuss=" + engineStatuss + "]";
	}
	
}
