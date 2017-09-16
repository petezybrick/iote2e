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
package com.pzybrick.iote2e.stream.bdbb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class FlightStatus.
 */
@Generated("org.jsonschema2pojo")
public class FlightStatus {
	
	/** The flight status uuid. */
	@Expose
	private String flightStatusUuid;
	
	/** The flight status ts. */
	@Expose
	private Long flightStatusTs;
	
	/** The airframe uuid. */
	@Expose
	private String airframeUuid;
	
	/** The flight number. */
	@Expose
	private String flightNumber;
	
	/** The from airport. */
	@Expose
	private String fromAirport;
	
	/** The to airport. */
	@Expose
	private String toAirport;
	
	/** The lat. */
	@Expose
	private double lat;
	
	/** The lng. */
	@Expose
	private double lng;
	
	/** The alt. */
	@Expose
	private float alt;
	
	/** The airspeed. */
	@Expose
	private float airspeed;
	
	/** The heading. */
	@Expose
	private float heading;
	
	/** The engine statuss. */
	@Expose
	private List<EngineStatus> engineStatuss;
	
	
	/**
	 * Instantiates a new flight status.
	 */
	public FlightStatus( ) {
		super();
	}
	
	/**
	 * Instantiates a new flight status.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
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
	
	
	/**
	 * Gets the flight status uuid.
	 *
	 * @return the flight status uuid
	 */
	public String getFlightStatusUuid() {
		return flightStatusUuid;
	}
	
	/**
	 * Gets the flight status ts.
	 *
	 * @return the flight status ts
	 */
	public Long getFlightStatusTs() {
		return flightStatusTs;
	}
	
	/**
	 * Gets the airframe uuid.
	 *
	 * @return the airframe uuid
	 */
	public String getAirframeUuid() {
		return airframeUuid;
	}
	
	/**
	 * Gets the flight number.
	 *
	 * @return the flight number
	 */
	public String getFlightNumber() {
		return flightNumber;
	}
	
	/**
	 * Gets the from airport.
	 *
	 * @return the from airport
	 */
	public String getFromAirport() {
		return fromAirport;
	}
	
	/**
	 * Gets the to airport.
	 *
	 * @return the to airport
	 */
	public String getToAirport() {
		return toAirport;
	}
	
	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * Gets the lng.
	 *
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}
	
	/**
	 * Gets the alt.
	 *
	 * @return the alt
	 */
	public float getAlt() {
		return alt;
	}
	
	/**
	 * Gets the engine statuss.
	 *
	 * @return the engine statuss
	 */
	public List<EngineStatus> getEngineStatuss() {
		return engineStatuss;
	}
	
	/**
	 * Sets the flight status uuid.
	 *
	 * @param flightStatusUuid the flight status uuid
	 * @return the flight status
	 */
	public FlightStatus setFlightStatusUuid(String flightStatusUuid) {
		this.flightStatusUuid = flightStatusUuid;
		return this;
	}
	
	/**
	 * Sets the flight status ts.
	 *
	 * @param flightStatusTs the flight status ts
	 * @return the flight status
	 */
	public FlightStatus setFlightStatusTs(Long flightStatusTs) {
		this.flightStatusTs = flightStatusTs;
		return this;
	}
	
	/**
	 * Sets the airframe uuid.
	 *
	 * @param airframeUuid the airframe uuid
	 * @return the flight status
	 */
	public FlightStatus setAirframeUuid(String airframeUuid) {
		this.airframeUuid = airframeUuid;
		return this;
	}
	
	/**
	 * Sets the flight number.
	 *
	 * @param flightNumber the flight number
	 * @return the flight status
	 */
	public FlightStatus setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
		return this;
	}
	
	/**
	 * Sets the from airport.
	 *
	 * @param fromAirport the from airport
	 * @return the flight status
	 */
	public FlightStatus setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
		return this;
	}
	
	/**
	 * Sets the to airport.
	 *
	 * @param toAirport the to airport
	 * @return the flight status
	 */
	public FlightStatus setToAirport(String toAirport) {
		this.toAirport = toAirport;
		return this;	
	}
	
	/**
	 * Sets the lat.
	 *
	 * @param lat the lat
	 * @return the flight status
	 */
	public FlightStatus setLat(double lat) {
		this.lat = lat;
		return this;
	}
	
	/**
	 * Sets the lng.
	 *
	 * @param lng the lng
	 * @return the flight status
	 */
	public FlightStatus setLng(double lng) {
		this.lng = lng;
		return this;
	}
	
	/**
	 * Sets the alt.
	 *
	 * @param alt the alt
	 * @return the flight status
	 */
	public FlightStatus setAlt(float alt) {
		this.alt = alt;
		return this;
	}
	
	/**
	 * Sets the engine statuss.
	 *
	 * @param engineStatuss the engine statuss
	 * @return the flight status
	 */
	public FlightStatus setEngineStatuss(List<EngineStatus> engineStatuss) {
		this.engineStatuss = engineStatuss;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FlightStatus [statusUuid=" + flightStatusUuid + ", statusTs=" + flightStatusTs + ", airframeUuid=" + airframeUuid
				+ ", flightNumber=" + flightNumber + ", fromAirport=" + fromAirport + ", toAirport=" + toAirport
				+ ", lat=" + lat + ", lng=" + lng + ", alt=" + alt + ", airspeed=" + airspeed + ", heading=" + heading
				+ ", engineStatuss=" + engineStatuss + "]";
	}
	
	/**
	 * Gets the airspeed.
	 *
	 * @return the airspeed
	 */
	public float getAirspeed() {
		return airspeed;
	}
	
	/**
	 * Gets the heading.
	 *
	 * @return the heading
	 */
	public float getHeading() {
		return heading;
	}
	
	/**
	 * Sets the airspeed.
	 *
	 * @param airspeed the airspeed
	 * @return the flight status
	 */
	public FlightStatus setAirspeed(float airspeed) {
		this.airspeed = airspeed;
		return this;
	}
	
	/**
	 * Sets the heading.
	 *
	 * @param heading the heading
	 * @return the flight status
	 */
	public FlightStatus setHeading(float heading) {
		this.heading = heading;
		return this;
	}
	
}
