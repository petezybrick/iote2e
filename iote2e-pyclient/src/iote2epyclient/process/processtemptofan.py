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
ProcessTempToFan
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import logging
import time
import uuid
from iote2epyclient.launch.clientutils import ClientUtils
from iote2epyclient.schema.iote2erequest import Iote2eRequest
import piplates.MOTORplate as MOTOR
import piplates.DAQCplate as DAQC


logger = logging.getLogger(__name__)


class ProcessTempToFan(object):
    '''
    Send Iote2eRequest with temperature, hanlde Iote2eResult to turn fan on or off
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        MOTOR.dcCONFIG(0,3,'cw',100, 1)
        
        
    def process(self):
        logger.info('process');
        
        
    def createIote2eRequest(self ):
        time.sleep(1)
        logger.info('ProcessTempToFan createIote2eRequest:')
        #TODO: get temp from DAQC
        tempC = str(round(DAQC.getTEMP(0,0,'c'),2))
        pairs = { self.sensorName: tempC }

        iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='temperature', 
                                       request_uuid=str(uuid.uuid4()), 
                                       request_timestamp=ClientUtils.nowIso8601(), 
                                       pairs=pairs, operation='SENSORS_VALUES')
        return iote2eRequest
        
    def handleIote2eResult(self, iote2eResult ):
        actuatorValue = iote2eResult.pairs['actuatorValue'];
        logger.info('actuatorValue {}'.format(actuatorValue))
        if 'off' == actuatorValue:
            MOTOR.dcSTOP(0,3)
        elif 'on' == actuatorValue:
            MOTOR.dcSTART(0,3)
        
        
        
