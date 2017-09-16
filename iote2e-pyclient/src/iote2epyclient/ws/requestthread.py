# Copyright 2016, 2017 Peter Zybrick and others.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""
RequestThread 
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import threading
import time
import logging
import avro
from Queue import Queue,Empty
from iote2epyclient.schema.iote2erequest import Iote2eRequest


logger = logging.getLogger(__name__)

class RequestThread( threading.Thread):
    """
    Thread to handle Iote2eRequest's
    """
    
    def __init__(self, requestQueue,processSensorActuator):
        logger.info("Constructing")
        threading.Thread.__init__(self)
        self.requestQueue = requestQueue
        self.processSensorActuator = processSensorActuator
        self.isShutdown = False

    
    def run(self, *args):
        logger.info("RequestThread run start")

        while True:
            try:
                if self.isShutdown: 
                    break
                #iote2eRequest = self.requestQueue.get(True, 2)
                iote2eRequest = self.processSensorActuator.createIote2eRequest()
                if iote2eRequest != None:
                    logger.info("Sending Request: " + str(iote2eRequest) )
                    self.requestQueue.put_nowait(iote2eRequest)

            except Empty:
                pass

        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        self.requestQueue.put_nowait(None)
        
        