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

import java.util.Random;


/**
 * The Class SimSequenceInteger.
 */
public class SimSequenceInteger {
	
	/** The random. */
	private static Random random = new Random();
	
	/** The mid. */
	private Integer mid;
	
	/** The max. */
	private Integer max;
	
	/** The min. */
	private Integer min;
	
	/** The exceed. */
	private Integer exceed;
	
	/** The incr. */
	private Integer incr;
	
	/** The prev. */
	private Integer prev;
	
	/** The min pct exceeded. */
	private Integer minPctExceeded;

	/**
	 * Next integer.
	 *
	 * @return the integer
	 * @throws Exception the exception
	 */
	public Integer nextInteger() throws Exception {
		Integer value = 0;
		
		if (prev != null) {
			if( prev == min ) {
				value = prev + incr;
			} else if( prev == max ) {
				value = prev - incr;
			}
			else if ((random.nextInt() % 2) == 1 )
				value = prev + incr;
			else
				value = prev - incr;
			if (value < min)
				value = min;
			else if (value > max)
				value = max;
		} else
			value = mid;
		prev = value;
		if (random.nextInt(100) <= minPctExceeded) {
			value = exceed;
			prev = mid;
		}
		return value;

	}

	/**
	 * Gets the random.
	 *
	 * @return the random
	 */
	public static Random getRandom() {
		return random;
	}

	/**
	 * Gets the mid.
	 *
	 * @return the mid
	 */
	public Integer getMid() {
		return mid;
	}

	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public Integer getMax() {
		return max;
	}

	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	public Integer getMin() {
		return min;
	}

	/**
	 * Gets the exceed.
	 *
	 * @return the exceed
	 */
	public Integer getExceed() {
		return exceed;
	}

	/**
	 * Gets the incr.
	 *
	 * @return the incr
	 */
	public Integer getIncr() {
		return incr;
	}

	/**
	 * Gets the prev.
	 *
	 * @return the prev
	 */
	public Integer getPrev() {
		return prev;
	}

	/**
	 * Gets the min pct exceeded.
	 *
	 * @return the min pct exceeded
	 */
	public Integer getMinPctExceeded() {
		return minPctExceeded;
	}

	/**
	 * Sets the mid.
	 *
	 * @param mid the mid
	 * @return the sim sequence integer
	 */
	public SimSequenceInteger setMid(Integer mid) {
		this.mid = mid;
		return this;
	}

	/**
	 * Sets the max.
	 *
	 * @param max the max
	 * @return the sim sequence integer
	 */
	public SimSequenceInteger setMax(Integer max) {
		this.max = max;
		return this;
	}

	/**
	 * Sets the min.
	 *
	 * @param min the min
	 * @return the sim sequence integer
	 */
	public SimSequenceInteger setMin(Integer min) {
		this.min = min;
		return this;
	}

	/**
	 * Sets the exceed.
	 *
	 * @param exceed the exceed
	 * @return the sim sequence integer
	 */
	public SimSequenceInteger setExceed(Integer exceed) {
		this.exceed = exceed;
		return this;
	}

	/**
	 * Sets the incr.
	 *
	 * @param incr the incr
	 * @return the sim sequence integer
	 */
	public SimSequenceInteger setIncr(Integer incr) {
		this.incr = incr;
		return this;
	}

	/**
	 * Sets the prev.
	 *
	 * @param prev the prev
	 * @return the sim sequence integer
	 */
	public SimSequenceInteger setPrev(Integer prev) {
		this.prev = prev;
		return this;
	}

	/**
	 * Sets the min pct exceeded.
	 *
	 * @param minPctExceeded the min pct exceeded
	 * @return the sim sequence integer
	 */
	public SimSequenceInteger setMinPctExceeded(Integer minPctExceeded) {
		this.minPctExceeded = minPctExceeded;
		return this;
	}

}
