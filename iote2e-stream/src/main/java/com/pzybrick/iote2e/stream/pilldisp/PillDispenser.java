package com.pzybrick.iote2e.stream.pilldisp;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.cache.CacheException;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.ignite.IgniteGridConnection;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.stream.persist.PillsDispensedDao;
import com.pzybrick.iote2e.stream.persist.PillsDispensedVo;
import com.pzybrick.iote2e.stream.persist.PillsDispensedVo.DispenseState;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;

public class PillDispenser {
	private static final Logger logger = LogManager.getLogger(PillDispenser.class);
	public static final String SOURCE_TYPE = "pilldisp";
	public static final CharSequence KEY_PILLS_DISPENSED_UUID =  new Utf8("PILLS_DISPENSED_UUID");
	public static final CharSequence KEY_PILLS_DISPENSED_STATE =  new Utf8("PILLS_DISPENSED_STATE");
	public static final CharSequence KEY_NUM_PILLS_TO_DISPENSE =  new Utf8("NUM_PILLS_TO_DISPENSE");
	
	public static void main( String[] args ) {
		try {
			String masterConfigJsonKey = args[0];
			String contactPoint = args[1]; 
			String keyspaceName = args[2];
			MasterConfig masterConfig = MasterConfig.getInstance( masterConfigJsonKey, contactPoint, keyspaceName );
			PillDispenser pillDispenser = new PillDispenser();
			pillDispenser.dispensePending( masterConfig );
			ConfigDao.disconnect();

		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void dispensePending( MasterConfig masterConfig ) throws Exception {
		List<PillsDispensedVo> pillsDispensedVos = PillsDispensedDao.sqlFindByDispenseState(masterConfig, DispenseState.PENDING );
		logger.info("Processing {} pills_dispensed Pending entries", pillsDispensedVos.size());
		if( !pillsDispensedVos.isEmpty() ) {
			IgniteGridConnection igniteGridConnection = new IgniteGridConnection().connect(masterConfig);
			Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();
			for( PillsDispensedVo pillsDispensedVo : pillsDispensedVos ) {
				String pkIgnite = pillsDispensedVo.getLoginName()+"|"+pillsDispensedVo.getSourceName()+"|";
				
				Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
				metadata.put(KEY_PILLS_DISPENSED_UUID, new Utf8(pillsDispensedVo.getPillsDispensedUuid()));
				metadata.put(KEY_PILLS_DISPENSED_STATE, new Utf8(DispenseState.DISPENSING.toString()));
				metadata.put(KEY_NUM_PILLS_TO_DISPENSE, new Utf8( String.valueOf(pillsDispensedVo.getNumToDispense()) ));
				
				Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
				pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME), new Utf8("NA"));
				pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME), new Utf8(pillsDispensedVo.getActuatorName() ));
				pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE), new Utf8( String.valueOf(pillsDispensedVo.getNumToDispense()) ));
				pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE_UPDATED_AT), new Utf8( Iote2eUtils.getDateNowUtc8601() ));
				
				Iote2eResult iote2eResult = Iote2eResult.newBuilder()
					.setPairs(pairs)
					.setMetadata(metadata)
					.setLoginName(new Utf8(pillsDispensedVo.getLoginName()))
					.setSourceName(new Utf8(pillsDispensedVo.getSourceName()))
					.setSourceType(new Utf8(SOURCE_TYPE))
					.setOperation(OPERATION.ACTUATOR_VALUES)
					.setRequestUuid(new Utf8(UUID.randomUUID().toString()))
					.setRequestTimestamp(new Utf8(Iote2eUtils.getDateNowUtc8601()))
					.setResultCode(0)
					.setResultTimestamp( new Utf8(Iote2eUtils.getDateNowUtc8601()))
					.setResultUuid( new Utf8(UUID.randomUUID().toString()))
					.build();
				
				boolean isSuccess = false;
				Exception lastException = null;
				long timeoutAt = System.currentTimeMillis() + (15*1000L);
				while( System.currentTimeMillis() < timeoutAt ) {
					try {
						igniteGridConnection.getCache().put(pkIgnite, iote2eResultReuseItem.toByteArray(iote2eResult));
						isSuccess = true;
						logger.info("cache.put successful, cache name={}, pk={}, pillsDispensedVo={}", igniteGridConnection.getCache().getName(), pkIgnite, pillsDispensedVo.toString() );
						break;
					} catch( CacheException cacheException ) {
						lastException = cacheException;
						logger.warn("cache.put failed with CacheException, will retry, cntRetry={}"  );
						try { Thread.sleep(1000L); } catch(Exception e ) {}
					} catch( Exception e ) {
						logger.error(e.getMessage(),e);
						throw e;
					}
				}
				if( !isSuccess ) {
					logger.error("Ignite cache write failure, pk={}, pillsDispensedVo={}, lastException: {}", pkIgnite, pillsDispensedVo.toString(), lastException.getLocalizedMessage(), lastException);
					throw new Exception( lastException);
				}
				PillsDispensedDao.updatePendingToDispensing(masterConfig, pillsDispensedVo.getPillsDispensedUuid());
			}
			try {
				if( igniteGridConnection != null )  {
					// Be careful - ignite is a singleton, only close after last usage
					igniteGridConnection.getCache().close();
					igniteGridConnection.getIgnite().close();
					igniteGridConnection = null;
				}
			} catch (Exception e) {
				logger.warn("Ignite close failure", e);
			}
		}
	}
	
	
	public static int countPills( BufferedImage image ) throws Exception {
		final float PIXEL_THRESHOLD = 225;
		GrayF32 input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);
		GrayU8 binary = new GrayU8(input.width,input.height);
		for( int x = 0 ; x<input.width ; x++ ) {
			for( int y=0 ; y<input.height ; y++ ) {
				int binout = input.get(x, y) < PIXEL_THRESHOLD ? 0 : 1;
				binary.set(x, y, binout );
			}
		}
		List<Contour> contours = BinaryImageOps.contour(binary, ConnectRule.EIGHT,null);
		return contours.size();
	}
}
