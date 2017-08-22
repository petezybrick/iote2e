package com.pzybrick.iote2e.tests.bdbb;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PopulateCourse {
	private static final Logger logger = LogManager.getLogger(PopulateCourse.class);

	public static void main(String[] args) {
		try {
			PopulateCourse populateCourse = new PopulateCourse();
			populateCourse.process();
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
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
		
		// JFK to Munich
		courseResults.add( CreateCourse.run(
			new CourseRequest().setStartDesc("JFK").setEndDesc("MUC").setStartMsecs(baseTimeMillis)
			.setStartLat(40.6398262).setStartLng(-73.7787443).setStartAltFt(10)
			.setEndLat(48.3538888889).setEndLng(11.7861111111).setEndAltFt(1486)
			.setCruiseAltFt(32000).setNumWayPts(120).setFreqMSecs(1000)));
			
		// SFO to Narita
		courseResults.add( CreateCourse.run(
			new CourseRequest().setStartDesc("SFO").setEndDesc("NRT").setStartMsecs(baseTimeMillis)
			.setStartLat(37.6188237).setStartLng(-122.3758047).setStartAltFt(13)
			.setEndLat(35.771987).setEndLng(140.39285).setEndAltFt(130)
			.setCruiseAltFt(34000).setNumWayPts(120).setFreqMSecs(1000)));
		
		for( CourseResult courseResult : courseResults ) {
			System.out.println(courseResult.getCourseRequest().getStartDesc() + " to " + courseResult.getCourseRequest().getEndDesc() );
			for( CourseWayPoint courseWayPoint : courseResult.getCourseWayPoints()) {
				System.out.println("\t" + courseWayPoint);
			}
		}
	}
	
	

}
