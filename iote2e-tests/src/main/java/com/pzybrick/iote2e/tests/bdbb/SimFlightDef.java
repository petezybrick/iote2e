package com.pzybrick.iote2e.tests.bdbb;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class SimFlightDef {
	@Expose
	private String airlineId;
	@Expose
	private String airframeModel;
	@Expose
	private String tailNumber;
	@Expose
	private String flightNumber;
	@Expose
	private String fromAirport;
	@Expose
	private String toAirport;
	@Expose
	private double startLat;
	@Expose
	private double startLng;
	@Expose
	private double endLat;
	@Expose
	private double endLng;
	@Expose
	private float endAltFt;
	@Expose
	private float startAltFt;
	@Expose
	private float cruiseAltFt;
	@Expose
	private float takeoffAirspeedKts;
	@Expose
	private float cruiseAirspeedKts;
	@Expose
	private float heading;
	@Expose
	private float landingAirspeedKts;
	@Expose
	private int numEngines;
	@Expose
	private String engineModel;
	
	
	public String getAirlineId() {
		return airlineId;
	}
	public String getAirframeModel() {
		return airframeModel;
	}
	public String getTailNumber() {
		return tailNumber;
	}
	public String getFromAirport() {
		return fromAirport;
	}
	public String getToAirport() {
		return toAirport;
	}
	public double getStartLat() {
		return startLat;
	}
	public double getStartLng() {
		return startLng;
	}
	public double getEndLat() {
		return endLat;
	}
	public double getEndLng() {
		return endLng;
	}
	public float getEndAltFt() {
		return endAltFt;
	}
	public float getStartAltFt() {
		return startAltFt;
	}
	public float getCruiseAltFt() {
		return cruiseAltFt;
	}
	public float getTakeoffAirspeedKts() {
		return takeoffAirspeedKts;
	}
	public float getCruiseAirspeedKts() {
		return cruiseAirspeedKts;
	}
	public float getLandingAirspeedKts() {
		return landingAirspeedKts;
	}
	public int getNumEngines() {
		return numEngines;
	}
	public String getEngineModel() {
		return engineModel;
	}
	public SimFlightDef setAirlineId(String airlineId) {
		this.airlineId = airlineId;
		return this;
	}
	public SimFlightDef setAirframeModel(String airframeModel) {
		this.airframeModel = airframeModel;
		return this;
	}
	public SimFlightDef setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
		return this;
	}
	public SimFlightDef setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
		return this;
	}
	public SimFlightDef setToAirport(String toAirport) {
		this.toAirport = toAirport;
		return this;
	}
	public SimFlightDef setStartLat(double startLat) {
		this.startLat = startLat;
		return this;
	}
	public SimFlightDef setStartLng(double startLng) {
		this.startLng = startLng;
		return this;
	}
	public SimFlightDef setEndLat(double endLat) {
		this.endLat = endLat;
		return this;
	}
	public SimFlightDef setEndLng(double endLng) {
		this.endLng = endLng;
		return this;
	}
	public SimFlightDef setEndAltFt(float endAltFt) {
		this.endAltFt = endAltFt;
		return this;
	}
	public SimFlightDef setStartAltFt(float startAltFt) {
		this.startAltFt = startAltFt;
		return this;
	}
	public SimFlightDef setCruiseAltFt(float cruiseAltFt) {
		this.cruiseAltFt = cruiseAltFt;
		return this;
	}
	public SimFlightDef setTakeoffAirspeedKts(float takeoffAirspeedKts) {
		this.takeoffAirspeedKts = takeoffAirspeedKts;
		return this;
	}
	public SimFlightDef setCruiseAirspeedKts(float cruiseAirspeedKts) {
		this.cruiseAirspeedKts = cruiseAirspeedKts;
		return this;
	}
	public SimFlightDef setLandingAirspeedKts(float landingAirspeedKts) {
		this.landingAirspeedKts = landingAirspeedKts;
		return this;
	}
	public SimFlightDef setNumEngines(int numEngines) {
		this.numEngines = numEngines;
		return this;
	}
	public SimFlightDef setEngineModel(String engineModel) {
		this.engineModel = engineModel;
		return this;
	}
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
	public float getHeading() {
		return heading;
	}
	public SimFlightDef setHeading(float heading) {
		this.heading = heading;
		return this;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public SimFlightDef setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
		return this;
	}
}
