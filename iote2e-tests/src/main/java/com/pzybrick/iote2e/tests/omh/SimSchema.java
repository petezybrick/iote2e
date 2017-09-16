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
package com.pzybrick.iote2e.tests.omh;

import java.time.OffsetDateTime;

import org.openmhealth.schema.domain.omh.SchemaId;


/**
 * The Interface SimSchema.
 */
public interface SimSchema {
	
	/**
	 * Creates the body.
	 *
	 * @param now the now
	 * @param prevBody the prev body
	 * @return the object
	 * @throws Exception the exception
	 */
	public Object createBody( OffsetDateTime now, Object prevBody ) throws Exception;
	
	/**
	 * Gets the schema id.
	 *
	 * @return the schema id
	 */
	public SchemaId getSchemaId();
}
