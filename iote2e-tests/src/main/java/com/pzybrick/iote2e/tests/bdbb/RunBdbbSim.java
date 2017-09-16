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
package com.pzybrick.iote2e.tests.bdbb;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.CompressionUtils;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.bdbb.FlightStatus;


/**
 * The Class RunBdbbSim.
 */
public class RunBdbbSim {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RunBdbbSim.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			MasterConfig.getInstance( args[0], args[1], args[2] );
			List<List<FlightStatus>> listFlightStatuss = TestPopulateCourse.populateSimFlight(args[4]);
			RunBdbbSim.injectSimData(listFlightStatuss, args[3]);
		}  catch( Exception e ) {
			logger.error(e.getMessage(),e);
		} finally {
			ConfigDao.disconnect();
		}
	}
	
	
	/**
	 * Inject sim data.
	 *
	 * @param listFlightStatuss the list flight statuss
	 * @param wsUrl the ws url
	 * @throws Exception the exception
	 */
	public static void injectSimData( List<List<FlightStatus>> listFlightStatuss, String wsUrl ) throws Exception {
		try {
			ClientSocketBdbbHandler clientSocketBdbbHandler = new ClientSocketBdbbHandler().setUrl(wsUrl);
			clientSocketBdbbHandler.connect();

			for( int offset=0 ; offset<TestPopulateCourse.NUM_ITERATIONS ; offset++ ) {
				for( List<FlightStatus> flightStatuss : listFlightStatuss ) {
					System.out.println("====================================================================");
					FlightStatus flightStatus = flightStatuss.get(offset);
					String rawJson = Iote2eUtils.getGsonInstance().toJson(flightStatus);
					System.out.println( rawJson );
					byte[] compressed = CompressionUtils.compress(rawJson.getBytes());
					clientSocketBdbbHandler.session.getBasicRemote().sendBinary(ByteBuffer.wrap(compressed));

				}
				try {
					Thread.sleep(TestPopulateCourse.FREQ_MSECS);
				} catch(Exception e ) {}
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
	}

}
