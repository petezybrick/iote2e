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

import java.util.List;


/**
 * The Class CourseResult.
 */
public class CourseResult {
	
	/** The course request. */
	private CourseRequest courseRequest;
	
	/** The course way points. */
	private List<CourseWayPoint> courseWayPoints;
	
	/**
	 * Gets the course request.
	 *
	 * @return the course request
	 */
	public CourseRequest getCourseRequest() {
		return courseRequest;
	}
	
	/**
	 * Gets the course way points.
	 *
	 * @return the course way points
	 */
	public List<CourseWayPoint> getCourseWayPoints() {
		return courseWayPoints;
	}
	
	/**
	 * Sets the course request.
	 *
	 * @param courseRequest the course request
	 * @return the course result
	 */
	public CourseResult setCourseRequest(CourseRequest courseRequest) {
		this.courseRequest = courseRequest;
		return this;
	}
	
	/**
	 * Sets the course way points.
	 *
	 * @param courseWayPoints the course way points
	 * @return the course result
	 */
	public CourseResult setCourseWayPoints(List<CourseWayPoint> courseWayPoints) {
		this.courseWayPoints = courseWayPoints;
		return this;
	}
	
}
