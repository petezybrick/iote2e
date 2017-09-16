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
package com.pzybrick.iote2e.tests.bdbb;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class SimFlightDef.
 */
@Generated("org.jsonschema2pojo")
public class SimFlightDef {
	
	/** The airline id. */
	@Expose
	private String airlineId;
	
	/** The airframe model. */
	@Expose
	private String airframeModel;
	
	/** The tail number. */
	@Expose
	private String tailNumber;
	
	/** The flight number. */
	@Expose
	private String flightNumber;
	
	/** The from airport. */
	@Expose
	private String fromAirport;
	
	/** The to airport. */
	@Expose
	private String toAirport;
	
	/** The start lat. */
	@Expose
	private double startLat;
	
	/** The start lng. */
	@Expose
	private double startLng;
	
	/** The end lat. */
	@Expose
	private double endLat;
	
	/** The end lng. */
	@Expose
	private double endLng;
	
	/** The end alt ft. */
	@Expose
	private float endAltFt;
	
	/** The start alt ft. */
	@Expose
	private float startAltFt;
	
	/** The cruise alt ft. */
	@Expose
	private float cruiseAltFt;
	
	/** The takeoff airspeed kts. */
	@Expose
	private float takeoffAirspeedKts;
	
	/** The cruise airspeed kts. */
	@Expose
	private float cruiseAirspeedKts;
	
	/** The heading. */
	@Expose
	private float heading;
	
	/** The landing airspeed kts. */
	@Expose
	private float landingAirspeedKts;
	
	/** The num engines. */
	@Expose
	private int numEngines;
	
	/** The engine model. */
	@Expose
	private String engineModel;
	
	
	/**
	 * Gets the airline id.
	 *
	 * @return the airline id
	 */
	public String getAirlineId() {
		return airlineId;
	}
	
	/**
	 * Gets the airframe model.
	 *
	 * @return the airframe model
	 */
	public String getAirframeModel() {
		return airframeModel;
	}
	
	/**
	 * Gets the tail number.
	 *
	 * @return the tail number
	 */
	public String getTailNumber() {
		return tailNumber;
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
	 * Gets the start lat.
	 *
	 * @return the start lat
	 */
	public double getStartLat() {
		return startLat;
	}
	
	/**
	 * Gets the start lng.
	 *
	 * @return the start lng
	 */
	public double getStartLng() {
		return startLng;
	}
	
	/**
	 * Gets the end lat.
	 *
	 * @return the end lat
	 */
	public double getEndLat() {
		return endLat;
	}
	
	/**
	 * Gets the end lng.
	 *
	 * @return the end lng
	 */
	public double getEndLng() {
		return endLng;
	}
	
	/**
	 * Gets the end alt ft.
	 *
	 * @return the end alt ft
	 */
	public float getEndAltFt() {
		return endAltFt;
	}
	
	/**
	 * Gets the start alt ft.
	 *
	 * @return the start alt ft
	 */
	public float getStartAltFt() {
		return startAltFt;
	}
	
	/**
	 * Gets the cruise alt ft.
	 *
	 * @return the cruise alt ft
	 */
	public float getCruiseAltFt() {
		return cruiseAltFt;
	}
	
	/**
	 * Gets the takeoff airspeed kts.
	 *
	 * @return the takeoff airspeed kts
	 */
	public float getTakeoffAirspeedKts() {
		return takeoffAirspeedKts;
	}
	
	/**
	 * Gets the cruise airspeed kts.
	 *
	 * @return the cruise airspeed kts
	 */
	public float getCruiseAirspeedKts() {
		return cruiseAirspeedKts;
	}
	
	/**
	 * Gets the landing airspeed kts.
	 *
	 * @return the landing airspeed kts
	 */
	public float getLandingAirspeedKts() {
		return landingAirspeedKts;
	}
	
	/**
	 * Gets the num engines.
	 *
	 * @return the num engines
	 */
	public int getNumEngines() {
		return numEngines;
	}
	
	/**
	 * Gets the engine model.
	 *
	 * @return the engine model
	 */
	public String getEngineModel() {
		return engineModel;
	}
	
	/**
	 * Sets the airline id.
	 *
	 * @param airlineId the airline id
	 * @return the sim flight def
	 */
	public SimFlightDef setAirlineId(String airlineId) {
		this.airlineId = airlineId;
		return this;
	}
	
	/**
	 * Sets the airframe model.
	 *
	 * @param airframeModel the airframe model
	 * @return the sim flight def
	 */
	public SimFlightDef setAirframeModel(String airframeModel) {
		this.airframeModel = airframeModel;
		return this;
	}
	
	/**
	 * Sets the tail number.
	 *
	 * @param tailNumber the tail number
	 * @return the sim flight def
	 */
	public SimFlightDef setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
		return this;
	}
	
	/**
	 * Sets the from airport.
	 *
	 * @param fromAirport the from airport
	 * @return the sim flight def
	 */
	public SimFlightDef setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
		return this;
	}
	
	/**
	 * Sets the to airport.
	 *
	 * @param toAirport the to airport
	 * @return the sim flight def
	 */
	public SimFlightDef setToAirport(String toAirport) {
		this.toAirport = toAirport;
		return this;
	}
	
	/**
	 * Sets the start lat.
	 *
	 * @param startLat the start lat
	 * @return the sim flight def
	 */
	public SimFlightDef setStartLat(double startLat) {
		this.startLat = startLat;
		return this;
	}
	
	/**
	 * Sets the start lng.
	 *
	 * @param startLng the start lng
	 * @return the sim flight def
	 */
	public SimFlightDef setStartLng(double startLng) {
		this.startLng = startLng;
		return this;
	}
	
	/**
	 * Sets the end lat.
	 *
	 * @param endLat the end lat
	 * @return the sim flight def
	 */
	public SimFlightDef setEndLat(double endLat) {
		this.endLat = endLat;
		return this;
	}
	
	/**
	 * Sets the end lng.
	 *
	 * @param endLng the end lng
	 * @return the sim flight def
	 */
	public SimFlightDef setEndLng(double endLng) {
		this.endLng = endLng;
		return this;
	}
	
	/**
	 * Sets the end alt ft.
	 *
	 * @param endAltFt the end alt ft
	 * @return the sim flight def
	 */
	public SimFlightDef setEndAltFt(float endAltFt) {
		this.endAltFt = endAltFt;
		return this;
	}
	
	/**
	 * Sets the start alt ft.
	 *
	 * @param startAltFt the start alt ft
	 * @return the sim flight def
	 */
	public SimFlightDef setStartAltFt(float startAltFt) {
		this.startAltFt = startAltFt;
		return this;
	}
	
	/**
	 * Sets the cruise alt ft.
	 *
	 * @param cruiseAltFt the cruise alt ft
	 * @return the sim flight def
	 */
	public SimFlightDef setCruiseAltFt(float cruiseAltFt) {
		this.cruiseAltFt = cruiseAltFt;
		return this;
	}
	
	/**
	 * Sets the takeoff airspeed kts.
	 *
	 * @param takeoffAirspeedKts the takeoff airspeed kts
	 * @return the sim flight def
	 */
	public SimFlightDef setTakeoffAirspeedKts(float takeoffAirspeedKts) {
		this.takeoffAirspeedKts = takeoffAirspeedKts;
		return this;
	}
	
	/**
	 * Sets the cruise airspeed kts.
	 *
	 * @param cruiseAirspeedKts the cruise airspeed kts
	 * @return the sim flight def
	 */
	public SimFlightDef setCruiseAirspeedKts(float cruiseAirspeedKts) {
		this.cruiseAirspeedKts = cruiseAirspeedKts;
		return this;
	}
	
	/**
	 * Sets the landing airspeed kts.
	 *
	 * @param landingAirspeedKts the landing airspeed kts
	 * @return the sim flight def
	 */
	public SimFlightDef setLandingAirspeedKts(float landingAirspeedKts) {
		this.landingAirspeedKts = landingAirspeedKts;
		return this;
	}
	
	/**
	 * Sets the num engines.
	 *
	 * @param numEngines the num engines
	 * @return the sim flight def
	 */
	public SimFlightDef setNumEngines(int numEngines) {
		this.numEngines = numEngines;
		return this;
	}
	
	/**
	 * Sets the engine model.
	 *
	 * @param engineModel the engine model
	 * @return the sim flight def
	 */
	public SimFlightDef setEngineModel(String engineModel) {
		this.engineModel = engineModel;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimFlightDef [airlineId=" + airlineId + ", airframeModel=" + airframeModel + ", tailNumber="
				+ tailNumber + ", flightNumber=" + flightNumber + ", fromAirport=" + fromAirport + ", toAirport="
				+ toAirport + ", startLat=" + startLat + ", startLng=" + startLng + ", endLat=" + endLat + ", endLng="
				+ endLng + ", endAltFt=" + endAltFt + ", startAltFt=" + startAltFt + ", cruiseAltFt=" + cruiseAltFt
				+ ", takeoffAirspeedKts=" + takeoffAirspeedKts + ", cruiseAirspeedKts=" + cruiseAirspeedKts
				+ ", heading=" + heading + ", landingAirspeedKts=" + landingAirspeedKts + ", numEngines=" + numEngines
				+ ", engineModel=" + engineModel + "]";
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
	 * Sets the heading.
	 *
	 * @param heading the heading
	 * @return the sim flight def
	 */
	public SimFlightDef setHeading(float heading) {
		this.heading = heading;
		return this;
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
	 * Sets the flight number.
	 *
	 * @param flightNumber the flight number
	 * @return the sim flight def
	 */
	public SimFlightDef setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
		return this;
	}
}
