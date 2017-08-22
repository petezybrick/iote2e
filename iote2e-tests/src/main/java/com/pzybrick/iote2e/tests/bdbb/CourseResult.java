package com.pzybrick.iote2e.tests.bdbb;

import java.util.List;

public class CourseResult {
	private CourseRequest courseRequest;
	private List<CourseWayPoint> courseWayPoints;
	
	public CourseRequest getCourseRequest() {
		return courseRequest;
	}
	public List<CourseWayPoint> getCourseWayPoints() {
		return courseWayPoints;
	}
	public CourseResult setCourseRequest(CourseRequest courseRequest) {
		this.courseRequest = courseRequest;
		return this;
	}
	public CourseResult setCourseWayPoints(List<CourseWayPoint> courseWayPoints) {
		this.courseWayPoints = courseWayPoints;
		return this;
	}
	
}
