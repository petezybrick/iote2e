package com.pzybrick.iote2e.tests.bdbb;

import java.util.Random;

public class SimSequenceInteger {
	private static Random random = new Random();
	private Integer mid;
	private Integer max;
	private Integer min;
	private Integer exceed;
	private Integer incr;
	private Integer prev;
	private Integer minPctExceeded;

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

	public static Random getRandom() {
		return random;
	}

	public Integer getMid() {
		return mid;
	}

	public Integer getMax() {
		return max;
	}

	public Integer getMin() {
		return min;
	}

	public Integer getExceed() {
		return exceed;
	}

	public Integer getIncr() {
		return incr;
	}

	public Integer getPrev() {
		return prev;
	}

	public Integer getMinPctExceeded() {
		return minPctExceeded;
	}

	public SimSequenceInteger setMid(Integer mid) {
		this.mid = mid;
		return this;
	}

	public SimSequenceInteger setMax(Integer max) {
		this.max = max;
		return this;
	}

	public SimSequenceInteger setMin(Integer min) {
		this.min = min;
		return this;
	}

	public SimSequenceInteger setExceed(Integer exceed) {
		this.exceed = exceed;
		return this;
	}

	public SimSequenceInteger setIncr(Integer incr) {
		this.incr = incr;
		return this;
	}

	public SimSequenceInteger setPrev(Integer prev) {
		this.prev = prev;
		return this;
	}

	public SimSequenceInteger setMinPctExceeded(Integer minPctExceeded) {
		this.minPctExceeded = minPctExceeded;
		return this;
	}

}
