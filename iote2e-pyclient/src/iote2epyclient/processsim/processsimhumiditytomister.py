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
ProcessSimHumidityToMister
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


class ProcessSimHumidityToMister(object):
    '''
    Simulate Humidity Sensor and Mister
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.humidityDirectionIncrease = True
        self.HUMIDITY_MIN = 82.0
        self.HUMIDITY_MAX = 93.0
        self.HUMIDITY_INCR = .5
        self.humidityNow = 90.0
        
        
        
    def createIote2eRequest(self ):
        time.sleep(2)
        logger.info('ProcessSimHumidityToMister createIote2eRequest:')
        if self.humidityDirectionIncrease and self.humidityNow < self.HUMIDITY_MAX:
            self.humidityNow += self.HUMIDITY_INCR
        elif (not self.humidityDirectionIncrease) and self.humidityNow > self.HUMIDITY_MIN:
            self.humidityNow -= self.HUMIDITY_INCR;
        logger.info( "humidityNow: {}".format(self.humidityNow))
        
        if self.humidityNow <= self.HUMIDITY_MIN or self.humidityNow >= self.HUMIDITY_MAX:
            logger.error("Humidity exceeded: {}".format(self.humidityNow))
            # TODO: need to throw an exception or something so the calling thread exits
            sys.exit(8)

        # TODO: read humidity from sensor here
        pairs = { self.sensorName: str(self.humidityNow)}
        iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='humidity', 
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
            self.humidityDirectionIncrease = False;
        elif 'on' == actuatorValue:
            self.humidityDirectionIncrease = True;
        
        