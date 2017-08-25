package com.pzybrick.iote2e.stream.bdbb;

import java.util.Random;

public class SimSequenceFloat {
	private static Random random = new Random();
	private Float mid;
	private Float max;
	private Float min;
	private Float exceed;
	private Float incr;
	private Float prev;
	private Integer minPctExceeded;

	public Float nextFloat() throws Exception {
		Float value = 0.0F;

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

	public Float getMid() {
		return mid;
	}

	public Float getMax() {
		return max;
	}

	public Float getMin() {
		return min;
	}

	public Float getExceed() {
		return exceed;
	}

	public Float getIncr() {
		return incr;
	}

	public Float getPrev() {
		return prev;
	}

	public Integer getMinPctExceeded() {
		return minPctExceeded;
	}

	public SimSequenceFloat setMid(Float mid) {
		this.mid = mid;
		return this;
	}

	public SimSequenceFloat setMax(Float max) {
		this.max = max;
		return this;
	}

	public SimSequenceFloat setMin(Float min) {
		this.min = min;
		return this;
	}

	public SimSequenceFloat setExceed(Float exceed) {
		this.exceed = exceed;
		return this;
	}

	public SimSequenceFloat setIncr(Float incr) {
		this.incr = incr;
		return this;
	}

	public SimSequenceFloat setPrev(Float prev) {
		this.prev = prev;
		return this;
	}

	public SimSequenceFloat setMinPctExceeded(Integer minPctExceeded) {
		this.minPctExceeded = minPctExceeded;
		return this;
	}

}
