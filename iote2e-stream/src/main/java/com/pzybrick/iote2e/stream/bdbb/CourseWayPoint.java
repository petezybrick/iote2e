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
 * The Class CourseWayPoint.
 */
public class CourseWayPoint {
		
		/** The lat. */
		private double lat;
		
		/** The lng. */
		private double lng;
		
		/** The alt. */
		private float alt;
		
		/** The time millis. */
		private long timeMillis;
		
		/** The airspeed. */
		private float airspeed;
		
		/** The heading. */
		private float heading;
		
		/**
		 * Instantiates a new course way point.
		 */
		public CourseWayPoint() {
			super();
		}

		/**
		 * Gets the lat.
		 *
		 * @return the lat
		 */
		public double getLat() {
			return lat;
		}

		/**
		 * Gets the lng.
		 *
		 * @return the lng
		 */
		public double getLng() {
			return lng;
		}

		/**
		 * Gets the alt.
		 *
		 * @return the alt
		 */
		public float getAlt() {
			return alt;
		}

		/**
		 * Gets the time millis.
		 *
		 * @return the time millis
		 */
		public long getTimeMillis() {
			return timeMillis;
		}

		/**
		 * Gets the airspeed.
		 *
		 * @return the airspeed
		 */
		public float getAirspeed() {
			return airspeed;
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
		 * Sets the lat.
		 *
		 * @param lat the lat
		 * @return the course way point
		 */
		public CourseWayPoint setLat(double lat) {
			this.lat = lat;
			return this;
		}

		/**
		 * Sets the lng.
		 *
		 * @param lng the lng
		 * @return the course way point
		 */
		public CourseWayPoint setLng(double lng) {
			this.lng = lng;
			return this;
		}

		/**
		 * Sets the alt.
		 *
		 * @param alt the alt
		 * @return the course way point
		 */
		public CourseWayPoint setAlt(float alt) {
			this.alt = alt;
			return this;
		}

		/**
		 * Sets the time millis.
		 *
		 * @param timeMillis the time millis
		 * @return the course way point
		 */
		public CourseWayPoint setTimeMillis(long timeMillis) {
			this.timeMillis = timeMillis;
			return this;
		}

		/**
		 * Sets the airspeed.
		 *
		 * @param airspeed the airspeed
		 * @return the course way point
		 */
		public CourseWayPoint setAirspeed(float airspeed) {
			this.airspeed = airspeed;
			return this;
		}

		/**
		 * Sets the heading.
		 *
		 * @param heading the heading
		 * @return the course way point
		 */
		public CourseWayPoint setHeading(float heading) {
			this.heading = heading;
			return this;
		}
		

		
	}