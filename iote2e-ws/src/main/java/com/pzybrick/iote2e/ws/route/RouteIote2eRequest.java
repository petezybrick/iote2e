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
package com.pzybrick.iote2e.ws.route;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;


/**
 * The Interface RouteIote2eRequest.
 */
public interface RouteIote2eRequest {
	
	/**
	 * Route to target.
	 *
	 * @param Iote2eRequest the iote 2 e request
	 * @throws Exception the exception
	 */
	public void routeToTarget( Iote2eRequest Iote2eRequest ) throws Exception;
	
	/**
	 * Inits the.
	 *
	 * @param masterConfig the master config
	 * @throws Exception the exception
	 */
	public void init( MasterConfig masterConfig ) throws Exception;
}
