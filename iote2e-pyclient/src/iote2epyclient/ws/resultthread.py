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
ResultThread 
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import threading
import logging
from Queue import Empty

logger = logging.getLogger(__name__)

class ResultThread( threading.Thread):
    """
    Thread to handle Iote2eResult's
    """
    
    def __init__(self,resultQueue, processSensorActuator):
        logger.info("Constructing")
        threading.Thread.__init__(self)
        self.resultQueue = resultQueue
        self.processSensorActuator = processSensorActuator
        self.isShutdown = False
    
    def run(self, *args):
        logger.info("ResultThread run start")
        while True:
            if self.isShutdown: 
                break
            try:
                iote2eResult = self.resultQueue.get(True, 2)
                if iote2eResult != None:
                    logger.info("Rcvd Result: " + str(iote2eResult))
                    self.processSensorActuator.handleIote2eResult( iote2eResult )

            except Empty:
                pass
        
        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        self.resultQueue.put_nowait(None)
