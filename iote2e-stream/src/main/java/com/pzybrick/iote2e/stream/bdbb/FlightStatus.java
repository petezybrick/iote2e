package com.pzybrick.iote2e.stream.bdbb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class FlightStatus {
	@Expose
	private String flightStatusUuid;
	@Expose
	private Long flightStatusTs;
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
	private float airspeed;
	@Expose
	private float heading;
	@Expose
	private List<EngineStatus> engineStatuss;
	
	
	public FlightStatus( ) {
		super();
	}
	
	public FlightStatus( ResultSet rs ) throws SQLException {
		super();
		this.flightStatusUuid = rs.getString("flight_status_uuid");
		this.airframeUuid = rs.getString("airframe_Uuid");
		this.flightNumber = rs.getString("flight_number");
		this.fromAirport = rs.getString("from_airport");
		this.toAirport = rs.getString("to_airport");
		this.lat = rs.getFloat("lat");
		this.lng = rs.getFloat("lng");
		this.alt = rs.getFloat("alt");
		this.airspeed = rs.getFloat("airspeed");
		this.heading = rs.getFloat("heading");
		this.flightStatusTs = rs.getTimestamp("flight_status_ts").getTime();
		// this.insertTs = rs.getTimestamp("insert_ts");
	}
	
	
	public String getFlightStatusUuid() {
		return flightStatusUuid;
	}
	public Long getFlightStatusTs() {
		return flightStatusTs;
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
	public FlightStatus setFlightStatusUuid(String flightStatusUuid) {
		this.flightStatusUuid = flightStatusUuid;
		return this;
	}
	public FlightStatus setFlightStatusTs(Long flightStatusTs) {
		this.flightStatusTs = flightStatusTs;
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
		return "FlightStatus [statusUuid=" + flightStatusUuid + ", statusTs=" + flightStatusTs + ", airframeUuid=" + airframeUuid
				+ ", flightNumber=" + flightNumber + ", fromAirport=" + fromAirport + ", toAirport=" + toAirport
				+ ", lat=" + lat + ", lng=" + lng + ", alt=" + alt + ", airspeed=" + airspeed + ", heading=" + heading
				+ ", engineStatuss=" + engineStatuss + "]";
	}
	public float getAirspeed() {
		return airspeed;
	}
	public float getHeading() {
		return heading;
	}
	public FlightStatus setAirspeed(float airspeed) {
		this.airspeed = airspeed;
		return this;
	}
	public FlightStatus setHeading(float heading) {
		this.heading = heading;
		return this;
	}
	
}
