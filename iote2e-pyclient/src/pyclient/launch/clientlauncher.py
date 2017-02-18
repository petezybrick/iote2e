'''
Created on Jul 30, 2016

@author: pete
'''

from pyclient.process.processtemptofan import ProcessTempToFan
from pyclient.ws.requestthread import RequestThread
from pyclient.ws.resultthread import ResultThread
import sys
import time
from threading import Thread
from Queue import Queue
from pyclient.schema.iote2erequest import Iote2eRequest
from pyclient.schema.iote2eresult import Iote2eResult


def main(endpoint_url, conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    
    requestQueue = Queue()
    resultQueue = Queue()
        
    cls = globals()["ProcessTempToFan"]
    processTempToFan = cls()
    
    threadRequest = RequestThread(requestQueue=requestQueue,processSensorActuator=processTempToFan)
    threadResult = ResultThread(resultQueue=resultQueue, processSensorActuator=processTempToFan)
    
    threadRequest.start()
    threadResult.start()

    #processTempToFan.process()  
    
    for i in range(1,5):
        testPairs = { 'testPairNameA'+str(i):'testPairValueA'+str(i), 'testPairNameB'+str(i):'testPairValueB'+str(i) }
        testMetadata = { 'testMetadataNameA'+str(i):'testMetadataValueA'+str(i), 'testMetadataNameB'+str(i):'testMetadataValueB'+str(i) }

        #iote2eRequest = Iote2eRequest( login_name='testLogin'+str(i),source_name='testSourceName'+str(i), source_type='testSourceType'+str(i), 
        #                               request_uuid='testRequestUuid'+str(i), request_timestamp='testRequestTimestamp'+str(i), 
        #                               pairs=testPairs, operation='SENSORS_VALUES', metadata=testMetadata)

        #requestQueue.put(iote2eRequest)
        
        iote2eResult = Iote2eResult( login_name='testLogin'+str(i),source_name='testSourceName'+str(i), source_type='testSourceType'+str(i), 
                                     result_uuid='testResultUuid'+str(i), result_timestamp='testResultTimestamp'+str(i), 
                                     pairs=testPairs, operation='ACTUATOR_VALUES', metadata=testMetadata,
                                     request_uuid='testRequestUuid'+str(i), request_timestamp='testRequestTimestamp'+str(i))
        resultQueue.put(iote2eResult)
        time.sleep(.5)
    
  

    threadRequest.shutdown()
    threadResult.shutdown()
    
    threadRequest.join()
    threadResult.join()
    
    logger.info('Done')


if __name__ == '__main__':
    if( len(sys.argv) < 3 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python endpoint_url <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1], sys.argv[2])