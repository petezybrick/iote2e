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
ButtonPushedThread
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import threading
import time
import logging
import piplates.DAQCplate as DAQC


logger = logging.getLogger(__name__)

class ButtonPushedThread( threading.Thread):
    '''
    Thread to detect when the button is pushed to confirm acceptance of dispensed pills 
    '''
    def __init__(self, pillDispenser, daqcBoard=0 ):
        logger.info("Constructing")
        threading.Thread.__init__(self)
        self.daqcBoard = daqcBoard
        self.pillDispenser = pillDispenser
        self.isShutdown = False
    
    def run(self, *args):
        logger.info("ButtonPushedThread run start")
        
        DAQC.enableSWint(self.daqcBoard)
        DAQC.intEnable(self.daqcBoard)
        while True:
            if self.isShutdown: 
                break
            if DAQC.getINTflags(self.daqcBoard) == 256:
                self.pillDispenser.setDispenseState('CONFIRMING')
                break
            time.sleep(.25)     
        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        
        