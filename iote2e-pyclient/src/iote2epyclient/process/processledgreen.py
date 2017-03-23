'''
Created on Aug 6, 2016

@author: pete
'''
import logging
import time
import uuid
import threading
from iote2epyclient.launch.clientutils import ClientUtils
from iote2epyclient.schema.iote2erequest import Iote2eRequest
from sense_hat import SenseHat

logger = logging.getLogger(__name__)


class ProcessLedGreen(object):
    '''
    classdocs
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.sense = SenseHat()
        self.sense.clear()
        
        
    def createIote2eRequest(self ):
        while True:
            event = self.sense.stick.wait_for_event()
            if 'middle' == event.direction:
                if 'pressed' == event.action:
                    ledGreenState = '1'
                elif 'released' == event.action:
                    ledGreenState = '0'
                else: 
                    continue
                logger.info( "ProcessSimLedGreen createIote2eRequest ledGreenState: {}".format(ledGreenState))
        
                pairs = { self.sensorName: str(ledGreenState)}
                iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='switch', 
                                               request_uuid=str(uuid.uuid4()), 
                                               request_timestamp=ClientUtils.nowIso8601(), 
                                               pairs=pairs, operation='SENSORS_VALUES')
                break
            
        return iote2eRequest


    def handleIote2eResult(self, iote2eResult ):
        # TODO: turn on/off actuator (fan) here
        logger.info('ProcessSimHumidityToMister handleIote2eResult: ' + str(iote2eResult))
        actuatorValue = iote2eResult.pairs['actuatorValue'];
        logger.info('actuatorValue {}'.format(actuatorValue))
        if 'off' == actuatorValue:
            self.sense.clear()
        elif 'green' == actuatorValue:
            self.sense.clear(0,255,0)

        