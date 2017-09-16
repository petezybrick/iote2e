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
ProcessSimTempToFan
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import logging
import time
import uuid
import sys
from iote2epyclient.launch.clientutils import ClientUtils
from iote2epyclient.schema.iote2erequest import Iote2eRequest

logger = logging.getLogger(__name__)


class ProcessSimTempToFan(object):
    '''
    Simulate temperature sensor and fan
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.tempNow = 25
        self.tempDirectionIncrease = True
        self.TEMP_MIN = 25
        self.TEMP_MAX = 60
        self.TEMP_INCR = 3
        
        
    def createIote2eRequest(self ):
        time.sleep(2)
        logger.info('ProcessTempToFan createIote2eRequest:')
        if self.tempDirectionIncrease and self.tempNow < self.TEMP_MAX:
            self.tempNow += self.TEMP_INCR
        elif (not self.tempDirectionIncrease) and self.tempNow > self.TEMP_MIN:
            self.tempNow -= self.TEMP_INCR;
        logger.info( "tempNow: {}".format(self.tempNow))
        
        if self.tempNow <= self.TEMP_MIN or self.tempNow >= self.TEMP_MAX:
            logger.error("Temp exceeded: {}".format(self.tempNow))
            # TODO: need to throw an exception or something so the calling thread exits
            sys.exit(8)

        # TODO: read temp from sensor here
        pairs = { self.sensorName: str(self.tempNow)}
        iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='temperature', 
                                       request_uuid=str(uuid.uuid4()), 
                                       request_timestamp=ClientUtils.nowIso8601(), 
                                       pairs=pairs, operation='SENSORS_VALUES')
        return iote2eRequest

        
    def handleIote2eResult(self, iote2eResult ):
        # TODO: turn on/off actuator (fan) here
        logger.info('ProcessTempToFan handleIote2eResult: ' + str(iote2eResult))
        actuatorValue = iote2eResult.pairs['actuatorValue'];
        logger.info('actuatorValue {}'.format(actuatorValue))
        if 'off' == actuatorValue:
            self.tempDirectionIncrease = True;
        elif 'on' == actuatorValue:
            self.tempDirectionIncrease = False;
        
        