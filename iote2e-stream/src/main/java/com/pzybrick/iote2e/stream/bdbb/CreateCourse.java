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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The Class CreateCourse.
 */
public class CreateCourse {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(CreateCourse.class);

	
	/**
	 * Run.
	 *
	 * @param courseRequest the course request
	 * @return the course result
	 * @throws Exception the exception
	 */
	public static CourseResult run( CourseRequest courseRequest ) throws Exception {
		double wayPtDeltaLat = (courseRequest.getEndLat() - courseRequest.getStartLat()) / (courseRequest.getNumWayPts()-1);  // From airport is a waypoint
		double wayPtDeltaLng = (courseRequest.getEndLng() - courseRequest.getStartLng()) / (courseRequest.getNumWayPts()-1);
		long timeMillis = courseRequest.getStartMsecs();
		
		List<CourseWayPoint> courseWayPoints = new ArrayList<CourseWayPoint>();
		for( int i=0 ; i< courseRequest.getNumWayPts() ; i++ ) {
			double lat = courseRequest.getStartLat() + (i * wayPtDeltaLat);
			double lng = courseRequest.getStartLng() + (i * wayPtDeltaLng);
			float alt = courseRequest.getStartAltFt() + (i*1000);
			if( alt > courseRequest.getCruiseAltFt() ) alt = courseRequest.getCruiseAltFt();
			float airspeed = courseRequest.getTakeoffAirspeedKts() + (i*50);
			if( airspeed > courseRequest.getCruiseAirspeedKts() ) airspeed = courseRequest.getCruiseAirspeedKts();
			CourseWayPoint wayPoint = new CourseWayPoint()
					.setLat(lat).setLng(lng).setAlt(alt)
					.setAirspeed(airspeed).setHeading(courseRequest.getHeading()).setTimeMillis(timeMillis);
			courseWayPoints.add( wayPoint );
			timeMillis += courseRequest.getFreqMSecs();
		}
		// this is a hack to set the descent altitudes and landing airspeed
		float nextEndAltFt = courseRequest.getEndAltFt();
		float nextEndAirspeed = courseRequest.getLandingAirspeedKts();
		for( int i=courseWayPoints.size()-1 ; i>0 ; i-- ) {
			CourseWayPoint wayPoint = courseWayPoints.get(i);
			wayPoint.setAlt(nextEndAltFt);
			wayPoint.setAirspeed(nextEndAirspeed);
			if( nextEndAltFt == courseRequest.getCruiseAltFt() 
					&& nextEndAirspeed == courseRequest.getCruiseAirspeedKts() ) break;
			nextEndAltFt += 1000;
			if( nextEndAltFt > courseRequest.getCruiseAltFt()  ) nextEndAltFt = courseRequest.getCruiseAltFt();
			nextEndAirspeed += 50;
			if( nextEndAirspeed > courseRequest.getCruiseAirspeedKts() ) 
				nextEndAirspeed = courseRequest.getCruiseAirspeedKts();
		}
		
		return new CourseResult().setCourseRequest(courseRequest).setCourseWayPoints(courseWayPoints);
	}
}
