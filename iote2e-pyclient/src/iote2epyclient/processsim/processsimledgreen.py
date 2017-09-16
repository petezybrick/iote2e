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
ProcessSimLedGreen
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

logger = logging.getLogger(__name__)


class ProcessSimLedGreen(object):
    '''
    Simulate switch toggle for LED green on/off
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.ledGreenState = "0";
        self.lockLedGreen = threading.RLock()
        
        
    def createIote2eRequest(self ):
        time.sleep(2)
        ledGreenState = self.getLedGreenState()
        # TODO: read switch on/off from here
        logger.info( "ProcessSimLedGreen createIote2eRequest ledGreenState: {}".format(ledGreenState))

        pairs = { self.sensorName: str(ledGreenState)}
        iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='switch', 
                                       request_uuid=str(uuid.uuid4()), 
                                       request_timestamp=ClientUtils.nowIso8601(), 
                                       pairs=pairs, operation='SENSORS_VALUES')
        return iote2eRequest


    def handleIote2eResult(self, iote2eResult ):
        # TODO: turn on/off actuator (fan) here
        logger.info('ProcessSimHumidityToMister handleIote2eResult: ' + str(iote2eResult))
        actuatorValue = iote2eResult.pairs['actuatorValue'];
        logger.info('actuatorValue {}'.format(actuatorValue))
        if 'off' == actuatorValue:
            self.setLedGreenState( "1")
        elif 'green' == actuatorValue:
            self.setLedGreenState( "0")
        
        
    def setLedGreenState(self, ledGreenState):
        self.lockLedGreen.acquire()
        self.ledGreenState = ledGreenState
        self.lockLedGreen.release()
        
        
    def getLedGreenState(self):
        self.lockLedGreen.acquire()
        ledGreenState = self.ledGreenState
        self.lockLedGreen.release()
        return ledGreenState
        