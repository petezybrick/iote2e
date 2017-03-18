'''
Created on Aug 6, 2016

@author: pete
'''
import logging
import time
import uuid
import sys
from iote2epyclient.launch.clientutils import ClientUtils
from iote2epyclient.schema.iote2erequest import Iote2eRequest

logger = logging.getLogger(__name__)


class ProcessSimHumidityToMister(object):
    '''
    classdocs
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
        
        