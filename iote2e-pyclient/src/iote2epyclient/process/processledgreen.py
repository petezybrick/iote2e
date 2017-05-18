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


class ProcessLedGreen(object):
    '''
    classdocs
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.btnPressed = '0'
        DAQC.clrLED(0,0)

        DAQC.setLED(0,0)
        if DAQC.getINTflags(0) == 256:
            break
        time.sleep(.25)
        if DAQC.getINTflags(0) == 256:
            break
        
        
        
    def createIote2eRequest(self ):
        swState = DAQC.getSWstate(0) 
        while True:
            if (self.btnPressed == '0' and swState == 1 ) or (self.btnPressed == '1' and swState == 0):
                self.btnPressed = str(swState)
                logger.info( "ProcessLedGreen createIote2eRequest {}: {}".format(self.sensorName,self.btnPressed))
        
                pairs = { self.sensorName: str(self.btnPressed)}
                iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='switch', 
                                               request_uuid=str(uuid.uuid4()), 
                                               request_timestamp=ClientUtils.nowIso8601(), 
                                               pairs=pairs, operation='SENSORS_VALUES')
                break
            
        return iote2eRequest


    def handleIote2eResult(self, iote2eResult ):
        logger.info('ProcessSimHumidityToMister handleIote2eResult: ' + str(iote2eResult))
        actuatorValue = iote2eResult.pairs['actuatorValue'];
        logger.info('actuatorValue {}'.format(actuatorValue))
        if 'off' == actuatorValue:
            DAQC.clrLED(0,0)
        elif 'green' == actuatorValue:
            DAQC.setLED(0,1)

        