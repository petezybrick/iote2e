package com.pzybrick.iote2e.tests.bdbb;

import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.bdbb.Airframe;
import com.pzybrick.iote2e.stream.bdbb.CourseRequest;
import com.pzybrick.iote2e.stream.bdbb.CourseResult;
import com.pzybrick.iote2e.stream.bdbb.CourseWayPoint;
import com.pzybrick.iote2e.stream.bdbb.CreateCourse;
import com.pzybrick.iote2e.stream.bdbb.Engine;
import com.pzybrick.iote2e.stream.bdbb.EngineStatus;
import com.pzybrick.iote2e.stream.bdbb.FlightStatus;
import com.pzybrick.iote2e.stream.bdbb.SimSequenceFloat;
import com.pzybrick.iote2e.stream.persist.EngineStatusDao;
import com.pzybrick.iote2e.stream.persist.FlightStatusDao;
import com.pzybrick.iote2e.stream.persist.PooledDataSource;

public class TestPopulateCourse {
	private static final Logger logger = LogManager.getLogger(TestPopulateCourse.class);
	public static Integer NUM_ITERATIONS = 120;
	public static Long FREQ_MSECS = 1000L;
	public static Integer MIN_PCT_EXCEEDED = 5;
	public static boolean IS_TRUNCATE_TABLES = true;

	
	public static void main(String[] args) {
		try {
			MasterConfig.getInstance( args[0], args[1], args[2] );
			List<List<FlightStatus>> listFlightStatuss = TestPopulateCourse.populateSimFlight( args[3]);
			//TestPopulateCourse.dumpToConsole(listFlightStatuss);
			TestPopulateCourse.populateTables(listFlightStatuss);
		}  catch( Exception e ) {
			logger.error(e.getMessage(),e);
		} finally {
			ConfigDao.disconnect();
		}
	}
	
	
	public static void populateTables( List<List<FlightStatus>> listFlightStatuss ) throws Exception {
		logger.info("start");
		try ( Connection con = PooledDataSource.getInstance(MasterConfig.getInstance()).getConnection();
				Statement stmt = con.createStatement()) {

			con.setAutoCommit(false);
			try {
				// Truncate tables
				if( IS_TRUNCATE_TABLES ) {
					stmt.execute("DELETE FROM engine_status");
					stmt.execute("DELETE FROM flight_status");
					con.commit();
				}
				for( int offset=0 ; offset<NUM_ITERATIONS ; offset++ ) {
					for( List<FlightStatus> flightStatuss : listFlightStatuss ) {
						FlightStatus flightStatus = flightStatuss.get(offset);
						
						// This verifies POJO->JSON and JSON->POJO will work ok, i.e. same as when POJO is turned
						//  into JSON and sent over Kafka, then turned back into POJO in SparkStreaming
						String rawJson = Iote2eUtils.getGsonInstance().toJson(flightStatus);
						flightStatus = Iote2eUtils.getGsonInstance().fromJson(rawJson, FlightStatus.class);
						
						FlightStatusDao.insertBatchMode(con, flightStatus);
						for( EngineStatus engineStatus : flightStatus.getEngineStatuss() ) {
							EngineStatusDao.insertBatchMode(con, engineStatus);
						}

					}
				}
				con.commit();
				logger.info("done");
			} catch(SQLException sqlEx ) {
				con.rollback();
				throw sqlEx;
			}
	
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}

	
	public static void dumpToConsole( List<List<FlightStatus>> listFlightStatuss ) {
		try {
			// TODO: 
			for( int offset=0 ; offset<NUM_ITERATIONS ; offset++ ) {
				for( List<FlightStatus> flightStatuss : listFlightStatuss ) {
					System.out.println("====================================================================");
					FlightStatus flightStatus = flightStatuss.get(offset);
					// TODO: Populate FlightStatus and EngineStatus tables
					System.out.println(flightStatus);
					for( EngineStatus engineStatus : flightStatus.getEngineStatuss() ) {
						System.out.println("\t" + engineStatus);
					}
					
					String rawJson = Iote2eUtils.getGsonInstance().toJson(flightStatus);
					//System.out.println( rawJson );
					//System.out.println( flightStatus );
				}
//				try {
//					Thread.sleep(FREQ_MSECS);
//				} catch(Exception e ) {}
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
	}
	
	
	public static List<List<FlightStatus>> populateSimFlight( String pathSimCourseDefJson ) throws Exception {
		long baseTimeMillis = System.currentTimeMillis();
		List<List<FlightStatus>> listFlightStatuss = new ArrayList<List<FlightStatus>>();
		
		String rawJson = FileUtils.readFileToString(new File(pathSimCourseDefJson));
		SimCourseDef simCourseDef = Iote2eUtils.getGsonInstance().fromJson(rawJson, SimCourseDef.class );
		int offsetAirframe = 1;
		for( SimFlightDef simFlightDef : simCourseDef.getSimFlightDefs() ) {
			listFlightStatuss.add( simFlightStatus( baseTimeMillis, simCourseDef, simFlightDef, offsetAirframe ) );
			offsetAirframe++;
		}

		return listFlightStatuss;
	}
	
	
	private static List<FlightStatus> simFlightStatus( long baseTimeMillis, SimCourseDef simCourseDef, SimFlightDef simFlightDef, int offsetAirframe ) throws Exception {
		final String templateAirframeUuid = "af%d-af%d-af%d-af%d-af%d";
		final String templateEngineUuid = "af%de%d-af%de%d-af%de%d-af%de%d-af%de%d";
		
		String airframeUuid = String.format(templateAirframeUuid, offsetAirframe, offsetAirframe, offsetAirframe, offsetAirframe, offsetAirframe );
		Airframe airframe = new Airframe()
				.setAirframeUuid(airframeUuid)
				.setAirlineId(simFlightDef.getAirlineId())
				.setModel(simFlightDef.getAirframeModel())
				.setTailNumber(simFlightDef.getTailNumber())
				.setEngines( new ArrayList<Engine>() );
		for( int engineNumber=1 ; engineNumber <= simFlightDef.getNumEngines() ; engineNumber++ ) {
			String engineUuid = String.format(templateEngineUuid, offsetAirframe, engineNumber, offsetAirframe, engineNumber, offsetAirframe, engineNumber, offsetAirframe, engineNumber, offsetAirframe, engineNumber );
			airframe.getEngines().add( new Engine().setAirframeUuid(airframeUuid)
					.setEngineUuid(engineUuid)
					.setModel(simFlightDef.getEngineModel())
					.setEngineNumber(engineNumber) );
		}
		// heading calc http://www.movable-type.co.uk/scripts/latlong.html
		CourseResult courseResult = CreateCourse.run(
			new CourseRequest().setFromAirport(simFlightDef.getFromAirport()).setToAirport(simFlightDef.getToAirport()).setStartMsecs(baseTimeMillis)
			.setStartLat(simFlightDef.getStartLat()).setStartLng(simFlightDef.getStartLng()).setStartAltFt(simFlightDef.getStartAltFt())
			.setEndLat(simFlightDef.getEndLat()).setEndLng(simFlightDef.getEndLng()).setEndAltFt(simFlightDef.getEndAltFt()).setHeading(simFlightDef.getHeading())
			.setTakeoffAirspeedKts(simFlightDef.getTakeoffAirspeedKts()).setCruiseAirspeedKts(simFlightDef.getCruiseAirspeedKts()).setLandingAirspeedKts(simFlightDef.getLandingAirspeedKts())
			.setCruiseAltFt(simFlightDef.getCruiseAltFt()).setNumWayPts(simCourseDef.getNumWayPts()).setFreqMSecs(simCourseDef.getFreqMsecs()));
		List<FlightStatus> flightStatuss = new ArrayList<FlightStatus>();
		// list of engine status simulation sequences
		List<SimEngineStatus> simEngineStatuss = new ArrayList<SimEngineStatus>();
		for( int j=0 ; j < airframe.getEngines().size() ; j++ ) {
			simEngineStatuss.add( new SimEngineStatus() );
		}
		for( int i=0 ; i<NUM_ITERATIONS ; i++ ) {
			CourseWayPoint courseWayPoint = courseResult.getCourseWayPoints().get(i);
			FlightStatus flightStatus = new FlightStatus()
					.setAirframeUuid(airframe.getAirframeUuid())
					.setAlt(courseWayPoint.getAlt())
					.setLat(courseWayPoint.getLat())
					.setLng(courseWayPoint.getLng())
					.setAirspeed(courseWayPoint.getAirspeed())
					.setHeading(courseWayPoint.getHeading())
					.setFlightNumber(simFlightDef.getFlightNumber())
					.setFromAirport(simFlightDef.getFromAirport())
					.setToAirport(simFlightDef.getToAirport())
					.setFlightStatusTs(courseWayPoint.getTimeMillis())
					.setFlightStatusUuid(UUID.randomUUID().toString())
					.setEngineStatuss( new ArrayList<EngineStatus>() );
			flightStatuss.add(flightStatus);
			for( int k=0 ; k<airframe.getEngines().size() ; k++ ) {
				Engine engine = airframe.getEngines().get(k);
				SimEngineStatus simEngineStatus = simEngineStatuss.get(k);
				flightStatus.getEngineStatuss().add(
						new EngineStatus()
							.setEngineStatusUuid(UUID.randomUUID().toString())
							.setFlightStatusUuid(flightStatus.getFlightStatusUuid())
							.setEngineUuid(engine.getEngineUuid())
							.setEngineNumber(engine.getEngineNumber())
							.setExhaustGasTempC(simEngineStatus.getExhaustGasTempCSim().nextFloat())
							.setN1Pct(simEngineStatus.getN1PctSim().nextFloat())
							.setN2Pct(simEngineStatus.getN2PctSim().nextFloat())
							.setOilPressure(simEngineStatus.getOilPressureSim().nextFloat())
							.setOilTempC(simEngineStatus.getOilTempCSim().nextFloat())
							.setEngineStatusTs(courseWayPoint.getTimeMillis())
						);
			}
		}
		return flightStatuss;
	}

	
	private static class SimEngineStatus {
		private SimSequenceFloat oilTempCSim;
		private SimSequenceFloat oilPressureSim;
		private SimSequenceFloat exhaustGasTempCSim;
		private SimSequenceFloat n1PctSim;
		private SimSequenceFloat n2PctSim;
		
		public SimEngineStatus() {
			this.oilTempCSim = new SimSequenceFloat()
					.setExceed(45F)
					.setIncr(.7F)
					.setMax(38F)
					.setMid(34.9F)
					.setMin(30F)
					.setMinPctExceeded(MIN_PCT_EXCEEDED);			
			this.oilPressureSim = new SimSequenceFloat()
					.setExceed(90F)
					.setIncr(1.3F)
					.setMax(73F)
					.setMid(64.0F)
					.setMin(55F)
					.setMinPctExceeded(MIN_PCT_EXCEEDED);
			this.exhaustGasTempCSim = new SimSequenceFloat()
					.setExceed(950F)
					.setIncr(7.7F)
					.setMax(805F)
					.setMid(788.6F)
					.setMin(765F)
					.setMinPctExceeded(MIN_PCT_EXCEEDED);
			this.n1PctSim = new SimSequenceFloat()
					.setExceed(110F)
					.setIncr(1.2F)
					.setMax(98.2F)
					.setMid(95.4F)
					.setMin(90F)
					.setMinPctExceeded(MIN_PCT_EXCEEDED);
			this.n2PctSim = new SimSequenceFloat()
					.setExceed(105F)
					.setIncr(.7F)
					.setMax(97.2F)
					.setMid(94.6F)
					.setMin(89.2F)
					.setMinPctExceeded(MIN_PCT_EXCEEDED);
		}

		public SimSequenceFloat getOilTempCSim() {
			return oilTempCSim;
		}

		public SimSequenceFloat getOilPressureSim() {
			return oilPressureSim;
		}

		public SimSequenceFloat getExhaustGasTempCSim() {
			return exhaustGasTempCSim;
		}

		public SimSequenceFloat getN1PctSim() {
			return n1PctSim;
		}

		public SimSequenceFloat getN2PctSim() {
			return n2PctSim;
		}

		public SimEngineStatus setOilTempCSim(SimSequenceFloat oilTempCSim) {
			this.oilTempCSim = oilTempCSim;
			return this;
		}

		public SimEngineStatus setOilPressureSim(SimSequenceFloat oilPressureSim) {
			this.oilPressureSim = oilPressureSim;
			return this;
		}

		public SimEngineStatus setExhaustGasTempCSim(SimSequenceFloat exhaustGasTempCSim) {
			this.exhaustGasTempCSim = exhaustGasTempCSim;
			return this;
		}

		public SimEngineStatus setN1PctSim(SimSequenceFloat n1PctSim) {
			this.n1PctSim = n1PctSim;
			return this;
		}

		public SimEngineStatus setN2PctSim(SimSequenceFloat n2PctSim) {
			this.n2PctSim = n2PctSim;
			return this;
		}

	}
	

}
