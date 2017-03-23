'''
Created on Aug 6, 2016

@author: pete
'''
import logging
import time
import uuid
from iote2epyclient.launch.clientutils import ClientUtils
from iote2epyclient.schema.iote2erequest import Iote2eRequest
from sense_hat import SenseHat


logger = logging.getLogger(__name__)


class ProcessTempToFan(object):
    '''
    classdocs
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.sense = SenseHat()

        
    def process(self):
        logger.info('process');
        
        
    def createIote2eRequest(self ):
        time.sleep(1)
        logger.info('ProcessTempToFan createIote2eRequest:')
        tempC = str(round(self.sense.get_temperature(),2))
        pairs = { self.sensorName: tempC }

        iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='temperature', 
                                       request_uuid=str(uuid.uuid4()), 
                                       request_timestamp=ClientUtils.nowIso8601(), 
                                       pairs=pairs, operation='SENSORS_VALUES')
        return iote2eRequest
        
    def handleIote2eResult(self, iote2eResult ):
        # TODO: turn on/off actuator (fan) here
        logger.info('ProcessTempToFan handleIote2eResult: ' + str(iote2eResult))
        
        