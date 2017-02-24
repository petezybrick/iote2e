'''
Created on Aug 6, 2016

@author: pete
'''
import logging
import time
import uuid
import sys
import datetime
import threading
from iote2epyclient.schema.iote2erequest import Iote2eRequest

logger = logging.getLogger(__name__)


class ProcessSimLedGreen(object):
    '''
    classdocs
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
                                       request_timestamp=datetime.datetime.utcnow().isoformat(), 
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
        