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


/**
 * The Class CourseRequest.
 */
public class CourseRequest {
	
	/** The from airport. */
	private String fromAirport;
	
	/** The to airport. */
	private String toAirport;
	
	/** The start lat. */
	private double startLat;
	
	/** The start lng. */
	private double startLng;
	
	/** The end lat. */
	private double endLat;
	
	/** The end lng. */
	private double endLng;
	
	/** The end alt ft. */
	private float endAltFt;
	
	/** The start alt ft. */
	private float startAltFt;
	
	/** The cruise alt ft. */
	private float cruiseAltFt;
	
	/** The num way pts. */
	private double numWayPts;
	
	/** The start msecs. */
	private long startMsecs;
	
	/** The freq M secs. */
	private long freqMSecs;
	
	/** The takeoff airspeed kts. */
	private float takeoffAirspeedKts;
	
	/** The cruise airspeed kts. */
	private float cruiseAirspeedKts;
	
	/** The landing airspeed kts. */
	private float landingAirspeedKts;
	
	/** The heading. */
	private float heading;

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
	 * Gets the start alt ft.
	 *
	 * @return the start alt ft
	 */
	public float getStartAltFt() {
		return startAltFt;
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
	 * Gets the cruise alt ft.
	 *
	 * @return the cruise alt ft
	 */
	public float getCruiseAltFt() {
		return cruiseAltFt;
	}
	
	/**
	 * Gets the num way pts.
	 *
	 * @return the num way pts
	 */
	public double getNumWayPts() {
		return numWayPts;
	}
	
	/**
	 * Gets the freq M secs.
	 *
	 * @return the freq M secs
	 */
	public long getFreqMSecs() {
		return freqMSecs;
	}
	
	/**
	 * Sets the start lat.
	 *
	 * @param startLat the start lat
	 * @return the course request
	 */
	public CourseRequest setStartLat(double startLat) {
		this.startLat = startLat;
		return this;
	}
	
	/**
	 * Sets the start lng.
	 *
	 * @param startLng the start lng
	 * @return the course request
	 */
	public CourseRequest setStartLng(double startLng) {
		this.startLng = startLng;
		return this;
	}
	
	/**
	 * Sets the start alt ft.
	 *
	 * @param startAltFt the start alt ft
	 * @return the course request
	 */
	public CourseRequest setStartAltFt(float startAltFt) {
		this.startAltFt = startAltFt;
		return this;
	}
	
	/**
	 * Sets the end lat.
	 *
	 * @param endLat the end lat
	 * @return the course request
	 */
	public CourseRequest setEndLat(double endLat) {
		this.endLat = endLat;
		return this;
	}
	
	/**
	 * Sets the end lng.
	 *
	 * @param endLng the end lng
	 * @return the course request
	 */
	public CourseRequest setEndLng(double endLng) {
		this.endLng = endLng;
		return this;
	}
	
	/**
	 * Sets the end alt ft.
	 *
	 * @param endAltFt the end alt ft
	 * @return the course request
	 */
	public CourseRequest setEndAltFt(float endAltFt) {
		this.endAltFt = endAltFt;
		return this;
	}
	
	/**
	 * Sets the cruise alt ft.
	 *
	 * @param cruiseAltFt the cruise alt ft
	 * @return the course request
	 */
	public CourseRequest setCruiseAltFt(float cruiseAltFt) {
		this.cruiseAltFt = cruiseAltFt;
		return this;
	}
	
	/**
	 * Sets the num way pts.
	 *
	 * @param numWayPts the num way pts
	 * @return the course request
	 */
	public CourseRequest setNumWayPts(double numWayPts) {
		this.numWayPts = numWayPts;
		return this;
	}
	
	/**
	 * Sets the freq M secs.
	 *
	 * @param freqMSecs the freq M secs
	 * @return the course request
	 */
	public CourseRequest setFreqMSecs(long freqMSecs) {
		this.freqMSecs = freqMSecs;
		return this;
	}
	
	/**
	 * Gets the start msecs.
	 *
	 * @return the start msecs
	 */
	public long getStartMsecs() {
		return startMsecs;
	}
	
	/**
	 * Sets the start msecs.
	 *
	 * @param startMsecs the start msecs
	 * @return the course request
	 */
	public CourseRequest setStartMsecs(long startMsecs) {
		this.startMsecs = startMsecs;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CourseRequest [fromAirport=" + fromAirport + ", toAirport=" + toAirport + ", startLat=" + startLat
				+ ", startLng=" + startLng + ", endLat=" + endLat + ", endLng=" + endLng + ", endAltFt=" + endAltFt
				+ ", startAltFt=" + startAltFt + ", cruiseAltFt=" + cruiseAltFt + ", numWayPts=" + numWayPts
				+ ", startMsecs=" + startMsecs + ", freqMSecs=" + freqMSecs + ", takeoffAirspeedKts="
				+ takeoffAirspeedKts + ", cruiseAirspeedKts=" + cruiseAirspeedKts + ", landingAirspeedKts="
				+ landingAirspeedKts + ", heading=" + heading + "]";
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
	 * Sets the from airport.
	 *
	 * @param fromAirport the from airport
	 * @return the course request
	 */
	public CourseRequest setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
		return this;
	}
	
	/**
	 * Sets the to airport.
	 *
	 * @param toAirport the to airport
	 * @return the course request
	 */
	public CourseRequest setToAirport(String toAirport) {
		this.toAirport = toAirport;
		return this;
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
	 * Sets the takeoff airspeed kts.
	 *
	 * @param takeoffAirspeedKts the takeoff airspeed kts
	 * @return the course request
	 */
	public CourseRequest setTakeoffAirspeedKts(float takeoffAirspeedKts) {
		this.takeoffAirspeedKts = takeoffAirspeedKts;
		return this;
	}
	
	/**
	 * Sets the cruise airspeed kts.
	 *
	 * @param cruiseAirspeedKts the cruise airspeed kts
	 * @return the course request
	 */
	public CourseRequest setCruiseAirspeedKts(float cruiseAirspeedKts) {
		this.cruiseAirspeedKts = cruiseAirspeedKts;
		return this;
	}
	
	/**
	 * Sets the landing airspeed kts.
	 *
	 * @param landingAirspeedKts the landing airspeed kts
	 * @return the course request
	 */
	public CourseRequest setLandingAirspeedKts(float landingAirspeedKts) {
		this.landingAirspeedKts = landingAirspeedKts;
		return this;
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
	 * @return the course request
	 */
	public CourseRequest setHeading(float heading) {
		this.heading = heading;
		return this;
	}
	
}
