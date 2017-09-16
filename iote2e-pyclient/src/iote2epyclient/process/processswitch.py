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
ProcessSwitch
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import logging
import time
import uuid
import threading
from iote2epyclient.launch.clientutils import ClientUtils
from iote2epyclient.schema.iote2erequest import Iote2eRequest
import piplates.DAQCplate as DAQC


logger = logging.getLogger(__name__)


class ProcessSwitch(object):
    '''
    Basic test to toggle the LED green/red based on switch press
    '''

    def __init__(self, loginVo, sensorName):
        logger.info( 'ProcessLedGreen init')
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.cnt = 1
        
        
    def createIote2eRequest(self ):
        iote2eRequest = None
        if self.cnt < 1001:
            pairs = { self.sensorName : str(self.cnt) }
            iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='switch', 
                                           request_uuid=str(uuid.uuid4()), 
                                           request_timestamp=ClientUtils.nowIso8601(), 
                                           pairs=pairs, operation='SENSORS_VALUES')
            logger.info( "ProcessSwitch iote2eRequest {}".format(iote2eRequest))    
            self.cnt = self.cnt + 1
            #time.sleep(1)   
        return iote2eRequest



    def handleIote2eResult(self, iote2eResult ):
        logger.info('ProcessLedGreen handleIote2eResult: ' + str(iote2eResult))
        actuatorValue = iote2eResult.pairs['actuatorValue'];
        logger.info('actuatorValue {}'.format(actuatorValue))
        if self.ledColor == 'green':
            logger.info('set led green')
            for i in range(0,100):
                DAQC.clrLED(0,0)
                DAQC.clrLED(0,1)
            #time.sleep(.25)
            for i in range(0,100):
                DAQC.setLED(0,1)
            self.ledColor = 'red'
        elif self.ledColor == 'red':
            logger.info('set led red')
            for i in range(0,100):
                DAQC.clrLED(0,0)
                DAQC.clrLED(0,1)
            #time.sleep(.25)
            for i in range(0,100):
                DAQC.setLED(0,0)
            self.ledColor = 'green'               

        