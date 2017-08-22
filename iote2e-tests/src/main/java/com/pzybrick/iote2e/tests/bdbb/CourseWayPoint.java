package com.pzybrick.iote2e.tests.bdbb;

public class CourseWayPoint {
		private double lat;
		private double lng;
		private float alt;
		private long timeMillis;
		public CourseWayPoint(double lat, double lng, float alt, long timeMillis) {
			super();
			this.lat = lat;
			this.lng = lng;
			this.alt = alt;
			this.timeMillis = timeMillis;
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
		public CourseWayPoint setLat(long timeMillis) {
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
		public long getTimeMillis() {
			return timeMillis;
		}
		public CourseWayPoint setTimeMillis(long timeMillis) {
			this.timeMillis = timeMillis;
			return this;
		}

		@Override
		public String toString() {
			return "WayPoint [lat=" + lat + ", lng=" + lng + ", alt=" + alt + ", timeMillis=" + timeMillis + "]";
		}
		
	}