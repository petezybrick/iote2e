package com.pzybrick.iote2e.tests.bdbb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;

public class PopulateCourse {
	private static final Logger logger = LogManager.getLogger(PopulateCourse.class);
	public static Integer NUM_ITERATIONS = 120;
	public static Long FREQ_MSECS = 1000L;

	
	public static void main(String[] args) {
		try {
			PopulateCourse populateCourse = new PopulateCourse();
			//populateCourse.process();
			List<List<FlightStatus>> listFlightStatuss = populateCourse.populateSimFlight();
			for( List<FlightStatus> flightStatuss : listFlightStatuss ) {
				for( FlightStatus flightStatus : flightStatuss ) {
					System.out.println("====================================================================");
					String rawJson = Iote2eUtils.getGsonInstance().toJson(flightStatus);
					System.out.println( rawJson );
					//System.out.println( flightStatus );
				}
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public static List<List<FlightStatus>> populateSimFlight() throws Exception {
		long baseTimeMillis = System.currentTimeMillis();
		List<List<FlightStatus>> listFlightStatuss = new ArrayList<List<FlightStatus>>();
		
		// LAX to JFK
		Airframe airframe = new Airframe()
				.setAirframeUuid("af1-af1-af1-af1-af1")
				.setAirframeUuid("DE")
				.setModel("B787")
				.setTailNumber("N11111")
				.setEngines( new ArrayList<Engine>() );
		airframe.getEngines().add( new Engine().setAirframeUuid("af1-af1-af1-af1-af1")
				.setEngineUuid("af1e1-af1e1-af1e1-af1e1-af1e1")
				.setModel("GEnx")
				.setEngineNumber(1) );		
		airframe.getEngines().add( new Engine().setAirframeUuid("af1-af1-af1-af1-af1")
				.setEngineUuid("af1e2-af1e2-af1e2-af1e2-af1e2")
				.setModel("GEnx")
				.setEngineNumber(2) );

		CourseResult courseResult = CreateCourse.run(
			new CourseRequest().setStartDesc("LAX").setEndDesc("JFK").setStartMsecs(baseTimeMillis)
			.setStartLat(33.942791).setStartLng(-118.410042).setStartAltFt(125)
			.setEndLat(40.6398262).setEndLng(-73.7787443).setEndAltFt(10)
			.setCruiseAltFt(30000).setNumWayPts(NUM_ITERATIONS).setFreqMSecs(FREQ_MSECS));
		List<FlightStatus> flightStatuss = new ArrayList<FlightStatus>();
		listFlightStatuss.add(flightStatuss);
		for( int i=0 ; i<NUM_ITERATIONS ; i++ ) {
			CourseWayPoint courseWayPoint = courseResult.getCourseWayPoints().get(i);
			FlightStatus flightStatus = new FlightStatus()
					.setAirframeUuid(airframe.getAirframeUuid())
					.setAlt(courseWayPoint.getAlt())
					.setLat(courseWayPoint.getLat())
					.setLng(courseWayPoint.getLng())
					.setFlightNumber("DE111")
					.setFromAirport("LAX")
					.setToAirport("JFK")
					.setStatusTs(courseWayPoint.getTimeMillis())
					.setStatusUuid(UUID.randomUUID().toString())
					.setEngineStatuss( new ArrayList<EngineStatus>() );
			flightStatuss.add(flightStatus);
			for( Engine engine : airframe.getEngines() ) {
				flightStatus.getEngineStatuss().add(
						new EngineStatus()
							.setEngineUuid(engine.getEngineUuid())
							.setEngineNumber(engine.getEngineNumber())
							.setExhaustGasTempC(788.6F)
							.setN1Pct(95.4F)
							.setN2Pct(94.6F)
							.setOilPressure(64.0F)
							.setOilTempC(34.9F)
						);
			}
			
		}
		
		return listFlightStatuss;
	}
	
	// Airframes and Engines
	public static List<Airframe> populateAirframesEngines() {
		List<Airframe> airframes = new ArrayList<Airframe>();
		
		Airframe airframe = new Airframe()
				.setAirframeUuid("af1-af1-af1-af1-af1")
				.setAirframeUuid("DE")
				.setModel("B787")
				.setTailNumber("N11111")
				.setEngines( new ArrayList<Engine>() );
		airframe.getEngines().add( new Engine().setAirframeUuid("af1-af1-af1-af1-af1")
				.setEngineUuid("af1e1-af1e1-af1e1-af1e1-af1e1")
				.setModel("GEnx")
				.setEngineNumber(1) );		
		airframe.getEngines().add( new Engine().setAirframeUuid("af1-af1-af1-af1-af1")
				.setEngineUuid("af1e2-af1e2-af1e2-af1e2-af1e2")
				.setModel("GEnx")
				.setEngineNumber(2) );
		airframes.add( airframe );
		
		airframe = new Airframe()
				.setAirframeUuid("af2-af2-af2-af2-af2")
				.setAirframeUuid("AA")
				.setModel("B777")
				.setTailNumber("N22222")
				.setEngines( new ArrayList<Engine>() );
		airframe.getEngines().add( new Engine().setAirframeUuid("af2-af2-af2-af2-af2")
				.setEngineUuid("af2e1-af2e1-af2e1-af2e1-af2e1")
				.setModel("GE90")
				.setEngineNumber(1) );		
		airframe.getEngines().add( new Engine().setAirframeUuid("af2-af2-af2-af2-af2")
				.setEngineUuid("af2e2-af2e2-af2e2-af2e2-af2e2")
				.setModel("GE90")
				.setEngineNumber(2) );
		airframes.add( airframe );
		
		airframe = new Airframe()
				.setAirframeUuid("af3-af3-af3-af3-af3")
				.setAirframeUuid("LH")
				.setModel("A340-600")
				.setTailNumber("N33333")
				.setEngines( new ArrayList<Engine>() );
		airframe.getEngines().add( new Engine().setAirframeUuid("af3-af3-af3-af3-af3")
				.setEngineUuid("af3e1-af3e1-af3e1-af3e1-af3e1")
				.setModel("RR Trent 500")
				.setEngineNumber(1) );		
		airframe.getEngines().add( new Engine().setAirframeUuid("af3-af3-af3-af3-af3")
				.setEngineUuid("af3e2-af3e2-af3e2-af3e2-af3e2")
				.setModel("RR Trent 500")
				.setEngineNumber(2) );
		airframes.add( airframe );
		
		return airframes;
	}

	
	public void process() throws Exception {
		long baseTimeMillis = System.currentTimeMillis();
		List<CourseResult> courseResults = new ArrayList<CourseResult>();

		// LAX to JFK
		courseResults.add( CreateCourse.run(
			new CourseRequest().setStartDesc("LAX").setEndDesc("JFK").setStartMsecs(baseTimeMillis)
			.setStartLat(33.942791).setStartLng(-118.410042).setStartAltFt(125)
			.setEndLat(40.6398262).setEndLng(-73.7787443).setEndAltFt(10)
			.setCruiseAltFt(30000).setNumWayPts(120).setFreqMSecs(1000)));
			
		// SFO to Narita
		courseResults.add( CreateCourse.run(
			new CourseRequest().setStartDesc("SFO").setEndDesc("NRT").setStartMsecs(baseTimeMillis)
			.setStartLat(37.6188237).setStartLng(-122.3758047).setStartAltFt(13)
			.setEndLat(35.771987).setEndLng(140.39285).setEndAltFt(130)
			.setCruiseAltFt(34000).setNumWayPts(120).setFreqMSecs(1000)));
		
		// JFK to Munich
		courseResults.add( CreateCourse.run(
			new CourseRequest().setStartDesc("JFK").setEndDesc("MUC").setStartMsecs(baseTimeMillis)
			.setStartLat(40.6398262).setStartLng(-73.7787443).setStartAltFt(10)
			.setEndLat(48.3538888889).setEndLng(11.7861111111).setEndAltFt(1486)
			.setCruiseAltFt(32000).setNumWayPts(120).setFreqMSecs(1000)));
		
		for( CourseResult courseResult : courseResults ) {
			System.out.println(courseResult.getCourseRequest().getStartDesc() + " to " + courseResult.getCourseRequest().getEndDesc() );
			for( CourseWayPoint courseWayPoint : courseResult.getCourseWayPoints()) {
				System.out.println("\t" + courseWayPoint);
			}
		}
	}
	
	

}
