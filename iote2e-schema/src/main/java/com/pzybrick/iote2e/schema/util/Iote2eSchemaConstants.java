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
package com.pzybrick.iote2e.schema.util;

import org.apache.avro.util.Utf8;


/**
 * The Class Iote2eSchemaConstants.
 */
public class Iote2eSchemaConstants {
	
	/** The Constant PAIRNAME_SENSOR_NAME. */
	public static final Utf8 PAIRNAME_SENSOR_NAME = new Utf8("sensorName");
	
	/** The Constant PAIRNAME_SENSOR_VALUE. */
	public static final Utf8 PAIRNAME_SENSOR_VALUE = new Utf8("sensorValue");
	
	/** The Constant PAIRNAME_ACTUATOR_NAME. */
	public static final Utf8 PAIRNAME_ACTUATOR_NAME = new Utf8("actuatorName");
	
	/** The Constant PAIRNAME_ACTUATOR_VALUE. */
	public static final Utf8 PAIRNAME_ACTUATOR_VALUE = new Utf8("actuatorValue");
	
	/** The Constant PAIRNAME_ACTUATOR_VALUE_UPDATED_AT. */
	public static final Utf8 PAIRNAME_ACTUATOR_VALUE_UPDATED_AT = new Utf8("actuatorValueUpdatedAt");

}
