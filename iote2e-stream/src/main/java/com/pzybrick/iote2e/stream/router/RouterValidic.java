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
package com.pzybrick.iote2e.stream.router;

import java.io.Serializable;
import java.nio.ByteBuffer;


/**
 * The Interface RouterOmh.
 */
public interface RouterValidic extends AutoCloseable, Serializable{
    
    /**
     * Adds the.
     *
     * @param byteBuffer the byte buffer
     * @throws Exception the exception
     */
    public void add(ByteBuffer byteBuffer) throws Exception;
    
    /**
     * Flush.
     *
     * @throws Exception the exception
     */
    public void flush() throws Exception;
    
    /**
     * Gets the batch size.
     *
     * @return the batch size
     */
    public int getBatchSize();
    
    /**
     * Sets the batch size.
     *
     * @param batchSize the new batch size
     */
    public void setBatchSize(int batchSize);
}
