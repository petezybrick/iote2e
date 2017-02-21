'''
Created on Aug 6, 2016

@author: pete
'''
import logging
import time
import uuid
import sys
import datetime
from pyclient.schema.iote2erequest import Iote2eRequest

logger = logging.getLogger(__name__)


class ProcessSimTempToFan(object):
    '''
    classdocs
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.tempNow = 74
        self.tempDirectionIncrease = True
        self.TEMP_MIN = 74
        self.TEMP_MAX = 84
        self.TEMP_INCR = .5
        
        
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
        iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='temp', 
                                       request_uuid=str(uuid.uuid4()), 
                                       request_timestamp=datetime.datetime.utcnow().isoformat(), 
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
        
        