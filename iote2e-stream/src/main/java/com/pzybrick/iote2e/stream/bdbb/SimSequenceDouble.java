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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;


/**
 * The Class SimSequenceDouble.
 */
public class SimSequenceDouble {
	
	/** The random. */
	private static Random random = new Random();
	
	/** The mid. */
	private BigDecimal mid;
	
	/** The max. */
	private BigDecimal max;
	
	/** The min. */
	private BigDecimal min;
	
	/** The exceed. */
	private BigDecimal exceed;
	
	/** The incr. */
	private BigDecimal incr;
	
	/** The prev. */
	private BigDecimal prev;
	
	/** The min pct exceeded. */
	private Integer minPctExceeded;

	/**
	 * Next double.
	 *
	 * @return the double
	 * @throws Exception the exception
	 */
	public Double nextDouble() throws Exception {
		BigDecimal value = new BigDecimal(0.0D).setScale(2,  RoundingMode.HALF_UP);
		if (prev != null) {
			if( prev.equals(min) ) {
				value = value.add(prev).add(incr);
			} else if( prev.equals(max) ) {
				value = value.add(prev).subtract(incr);
			}
			else if ((random.nextInt(100) % 2) == 1 )
				value = value.add(prev).add(incr);
			else
				value = value.add(prev).subtract(incr);
			if (value.compareTo(min) < 0 )
				value = min;
			else if (value.compareTo(max) > 0)
				value = max;
		} else
			value = mid;
		prev = value;
		if (random.nextInt(100) <= minPctExceeded) {
			value = exceed;
			prev = mid;
		}
		return value.doubleValue();
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
	public Double getMid() {
		return mid.doubleValue();
	}

	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public Double getMax() {
		return max.doubleValue();
	}

	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	public Double getMin() {
		return min.doubleValue();
	}

	/**
	 * Gets the exceed.
	 *
	 * @return the exceed
	 */
	public Double getExceed() {
		return exceed.doubleValue();
	}

	/**
	 * Gets the incr.
	 *
	 * @return the incr
	 */
	public Double getIncr() {
		return incr.doubleValue();
	}

	/**
	 * Gets the prev.
	 *
	 * @return the prev
	 */
	public Double getPrev() {
		return prev.doubleValue();
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
	 * @return the sim sequence double
	 */
	public SimSequenceDouble setMid(Double mid) {
		this.mid = new BigDecimal(mid).setScale(2, RoundingMode.HALF_UP);
		return this;
	}

	/**
	 * Sets the max.
	 *
	 * @param max the max
	 * @return the sim sequence double
	 */
	public SimSequenceDouble setMax(Double max) {
		this.max = new BigDecimal(max).setScale(2, RoundingMode.HALF_UP);
		return this;
	}

	/**
	 * Sets the min.
	 *
	 * @param min the min
	 * @return the sim sequence double
	 */
	public SimSequenceDouble setMin(Double min) {
		this.min = new BigDecimal(min).setScale(2, RoundingMode.HALF_UP);
		return this;
	}

	/**
	 * Sets the exceed.
	 *
	 * @param exceed the exceed
	 * @return the sim sequence double
	 */
	public SimSequenceDouble setExceed(Double exceed) {
		this.exceed = new BigDecimal(exceed).setScale(2, RoundingMode.HALF_UP);
		return this;
	}

	/**
	 * Sets the incr.
	 *
	 * @param incr the incr
	 * @return the sim sequence double
	 */
	public SimSequenceDouble setIncr(Double incr) {
		this.incr = new BigDecimal(incr).setScale(2, RoundingMode.HALF_UP);
		return this;
	}

	/**
	 * Sets the prev.
	 *
	 * @param prev the prev
	 * @return the sim sequence double
	 */
	public SimSequenceDouble setPrev(Double prev) {
		this.prev = new BigDecimal(prev).setScale(2, RoundingMode.HALF_UP);
		return this;
	}

	/**
	 * Sets the min pct exceeded.
	 *
	 * @param minPctExceeded the min pct exceeded
	 * @return the sim sequence double
	 */
	public SimSequenceDouble setMinPctExceeded(Integer minPctExceeded) {
		this.minPctExceeded = minPctExceeded;
		return this;
	}

}
