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
BlinkLedThread
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import threading
import time
import logging
import piplates.DAQCplate as DAQC


logger = logging.getLogger(__name__)

class BlinkLedThread( threading.Thread):
    '''
    When dispensed pills image has been analyzed, blink green if correct number dispensed, else blink red
    '''
    # Red=0, Green=1
    def __init__(self, daqcBoard=0, ledColor='green'):
        logger.info("Constructing")
        threading.Thread.__init__(self)
        self.daqcBoard = daqcBoard
        if ledColor == 'green':
            self.led = 1
        else:
            self.led = 0
        self.isShutdown = False

    
    def run(self, *args):
        logger.info("BlinkLedThread run start")

        while True:
            if self.isShutdown: 
                break
            for i in range(0,100):
                DAQC.clrLED(self.daqcBoard, 0)
                DAQC.clrLED(self.daqcBoard, 1)
            time.sleep(.25)           
            for i in range(0,100):
                DAQC.setLED(self.daqcBoard, self.led)
            time.sleep(.25)
        for i in range(0,100):
            DAQC.clrLED(self.daqcBoard, 0)
            DAQC.clrLED(self.daqcBoard, 1)            

        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        
        