package com.pzybrick.iote2e.stream.bdbb;

public class CourseRequest {
	private String startDesc;
	private String endDesc;
	private double startLat;
	private double startLng;
	private double endLat;
	private double endLng;
	private float endAltFt;
	private float startAltFt;
	private float cruiseAltFt;
	private double numWayPts;
	private long startMsecs;
	private long freqMSecs;
	private float takeoffAirspeedKts;
	private float cruiseAirspeedKts;
	private float landingAirspeedKts;
	private float heading;

	public double getStartLat() {
		return startLat;
	}
	public double getStartLng() {
		return startLng;
	}
	public float getStartAltFt() {
		return startAltFt;
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
	public float getCruiseAltFt() {
		return cruiseAltFt;
	}
	public double getNumWayPts() {
		return numWayPts;
	}
	public long getFreqMSecs() {
		return freqMSecs;
	}
	public CourseRequest setStartLat(double startLat) {
		this.startLat = startLat;
		return this;
	}
	public CourseRequest setStartLng(double startLng) {
		this.startLng = startLng;
		return this;
	}
	public CourseRequest setStartAltFt(float startAltFt) {
		this.startAltFt = startAltFt;
		return this;
	}
	public CourseRequest setEndLat(double endLat) {
		this.endLat = endLat;
		return this;
	}
	public CourseRequest setEndLng(double endLng) {
		this.endLng = endLng;
		return this;
	}
	public CourseRequest setEndAltFt(float endAltFt) {
		this.endAltFt = endAltFt;
		return this;
	}
	public CourseRequest setCruiseAltFt(float cruiseAltFt) {
		this.cruiseAltFt = cruiseAltFt;
		return this;
	}
	public CourseRequest setNumWayPts(double numWayPts) {
		this.numWayPts = numWayPts;
		return this;
	}
	public CourseRequest setFreqMSecs(long freqMSecs) {
		this.freqMSecs = freqMSecs;
		return this;
	}
	public long getStartMsecs() {
		return startMsecs;
	}
	public CourseRequest setStartMsecs(long startMsecs) {
		this.startMsecs = startMsecs;
		return this;
	}
	@Override
	public String toString() {
		return "CourseRequest [startDesc=" + startDesc + ", endDesc=" + endDesc + ", startLat=" + startLat
				+ ", startLng=" + startLng + ", startAltFt=" + startAltFt + ", endLat=" + endLat + ", endLng=" + endLng
				+ ", endAltFt=" + endAltFt + ", cruiseAltFt=" + cruiseAltFt + ", numWayPts=" + numWayPts
				+ ", startMsecs=" + startMsecs + ", freqMSecs=" + freqMSecs + "]";
	}
	public String getStartDesc() {
		return startDesc;
	}
	public String getEndDesc() {
		return endDesc;
	}
	public CourseRequest setStartDesc(String startDesc) {
		this.startDesc = startDesc;
		return this;
	}
	public CourseRequest setEndDesc(String endDesc) {
		this.endDesc = endDesc;
		return this;
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
	public CourseRequest setTakeoffAirspeedKts(float takeoffAirspeedKts) {
		this.takeoffAirspeedKts = takeoffAirspeedKts;
		return this;
	}
	public CourseRequest setCruiseAirspeedKts(float cruiseAirspeedKts) {
		this.cruiseAirspeedKts = cruiseAirspeedKts;
		return this;
	}
	public CourseRequest setLandingAirspeedKts(float landingAirspeedKts) {
		this.landingAirspeedKts = landingAirspeedKts;
		return this;
	}
	public float getHeading() {
		return heading;
	}
	public CourseRequest setHeading(float heading) {
		this.heading = heading;
		return this;
	}
	
}
