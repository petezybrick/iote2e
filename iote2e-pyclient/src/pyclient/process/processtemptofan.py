'''
Created on Aug 6, 2016

@author: pete
'''
import logging
import time
from pyclient.schema.iote2erequest import Iote2eRequest

logger = logging.getLogger(__name__)


class ProcessTempToFan(object):
    '''
    classdocs
    '''

    def __init__(self):
        self.cnt = -1
        
    def process(self):
        logger.info('process');
        
    def createIote2eRequest(self ):
        self.cnt += 1
        logger.info('ProcessTempToFan createIote2eRequest: ' + str(self.cnt))
        testPairs = { 'testPairNameA'+str(self.cnt):'testPairValueA'+str(self.cnt), 'testPairNameB'+str(self.cnt):'testPairValueB'+str(self.cnt) }
        testMetadata = { 'testMetadataNameA'+str(self.cnt):'testMetadataValueA'+str(self.cnt), 'testMetadataNameB'+str(self.cnt):'testMetadataValueB'+str(self.cnt) }

        iote2eRequest = Iote2eRequest( login_name='testLogin'+str(self.cnt),source_name='testSourceName'+str(self.cnt), source_type='testSourceType'+str(self.cnt), 
                                       request_uuid='testRequestUuid'+str(self.cnt), request_timestamp='testRequestTimestamp'+str(self.cnt), 
                                       pairs=testPairs, operation='SENSORS_VALUES', metadata=testMetadata)
        time.sleep(.25)
        if (self.cnt % 2) == 0:
            return None 
        return iote2eRequest
        
    def handleIote2eResult(self, iote2eResult ):
        logger.info('ProcessTempToFan handleIote2eResult: ' + str(iote2eResult))
        
        