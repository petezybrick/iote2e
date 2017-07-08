package com.pzybrick.iote2e.stream.omh;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface SimSchema {
	public Object createBody( OffsetDateTime now, BigDecimal prevValue ) throws Exception;
}
