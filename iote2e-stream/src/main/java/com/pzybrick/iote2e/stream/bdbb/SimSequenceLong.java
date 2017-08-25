package com.pzybrick.iote2e.stream.bdbb;

import java.util.Random;

public class SimSequenceLong {
	private static Random random = new Random();
	private Long mid;
	private Long max;
	private Long min;
	private Long exceed;
	private Long incr;
	private Long prev;
	private Integer minPctExceeded;

	public Long nextLong() throws Exception {
		long value = 0;

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

	public Long getMid() {
		return mid;
	}

	public Long getMax() {
		return max;
	}

	public Long getMin() {
		return min;
	}

	public Long getExceed() {
		return exceed;
	}

	public Long getIncr() {
		return incr;
	}

	public Long getPrev() {
		return prev;
	}

	public Integer getMinPctExceeded() {
		return minPctExceeded;
	}

	public SimSequenceLong setMid(Long mid) {
		this.mid = mid;
		return this;
	}

	public SimSequenceLong setMax(Long max) {
		this.max = max;
		return this;
	}

	public SimSequenceLong setMin(Long min) {
		this.min = min;
		return this;
	}

	public SimSequenceLong setExceed(Long exceed) {
		this.exceed = exceed;
		return this;
	}

	public SimSequenceLong setIncr(Long incr) {
		this.incr = incr;
		return this;
	}

	public SimSequenceLong setPrev(Long prev) {
		this.prev = prev;
		return this;
	}

	public SimSequenceLong setMinPctExceeded(Integer minPctExceeded) {
		this.minPctExceeded = minPctExceeded;
		return this;
	}

}
