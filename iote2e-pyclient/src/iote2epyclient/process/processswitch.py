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
import piplates.DAQCplate as DAQC


logger = logging.getLogger(__name__)


class ProcessSwitch(object):
    '''
    classdocs
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

        