package com.pzybrick.iote2e.stream.bdbb;

public class CourseWayPoint {
		private double lat;
		private double lng;
		private float alt;
		private long timeMillis;
		private float airspeed;
		private float heading;
		
		public CourseWayPoint() {
			super();
		}

		public double getLat() {
			return lat;
		}

		public double getLng() {
			return lng;
		}

		public float getAlt() {
			return alt;
		}

		public long getTimeMillis() {
			return timeMillis;
		}

		public float getAirspeed() {
			return airspeed;
		}

		public float getHeading() {
			return heading;
		}

		public CourseWayPoint setLat(double lat) {
			this.lat = lat;
			return this;
		}

		public CourseWayPoint setLng(double lng) {
			this.lng = lng;
			return this;
		}

		public CourseWayPoint setAlt(float alt) {
			this.alt = alt;
			return this;
		}

		public CourseWayPoint setTimeMillis(long timeMillis) {
			this.timeMillis = timeMillis;
			return this;
		}

		public CourseWayPoint setAirspeed(float airspeed) {
			this.airspeed = airspeed;
			return this;
		}

		public CourseWayPoint setHeading(float heading) {
			this.heading = heading;
			return this;
		}
		

		
	}