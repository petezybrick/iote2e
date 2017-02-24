'''
Created on Aug 6, 2016

@author: pete
'''
import logging
import time
import uuid
import datetime
from iote2epyclient.schema.iote2erequest import Iote2eRequest

logger = logging.getLogger(__name__)


class ProcessTempToFan(object):
    '''
    classdocs
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        
    def process(self):
        logger.info('process');
        
    def createIote2eRequest(self ):
        time.sleep(2)
        logger.info('ProcessTempToFan createIote2eRequest:')
        # TODO: read temp from sensor here
        pairs = { self.sensorName:'78.5' }

        iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='temp', 
                                       request_uuid=str(uuid.uuid4()), 
                                       request_timestamp=datetime.datetime.utcnow().isoformat(), 
                                       pairs=pairs, operation='SENSORS_VALUES')
        return iote2eRequest
        
    def handleIote2eResult(self, iote2eResult ):
        # TODO: turn on/off actuator (fan) here
        logger.info('ProcessTempToFan handleIote2eResult: ' + str(iote2eResult))
        
        