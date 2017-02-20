'''
Created on Jul 30, 2016

@author: pete
'''

from pyclient.ws.requestthread import RequestThread
from pyclient.ws.resultthread import ResultThread
from pyclient.ws.socketthread import SocketThread
import sys
import avro
from Queue import Queue
from pyclient.ws.loginvo import LoginVo


def main( schemaSourceFolder, endpoint_url, loginName, sourceName, loggingConfig, optionalFilterSensorName):
    import logging.config
    logging.config.fileConfig( loggingConfig, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    
    if not schemaSourceFolder.endswith('/'):
        schemaSourceFolder += '/'
    schemaRequest = avro.schema.parse(open(schemaSourceFolder+'iote2e-request.avsc', 'rb').read())
    schemaResult = avro.schema.parse(open(schemaSourceFolder+'iote2e-result.avsc', 'rb').read())

    loginVo = LoginVo(loginName=loginName, passwordEncrypted='anything', sourceName=sourceName, optionalFilterSensorName=optionalFilterSensorName)
    
    socketThread = SocketThread( endpoint_url=endpoint_url, login=loginName)
    socketThread.start()
    
    requestQueue = Queue()
    resultQueue = Queue()
        
    cls = globals()['ProcessTempToFan']
    processSensorActuator = cls()
    
    threadRequest = RequestThread(requestQueue=requestQueue,processSensorActuator=processSensorActuator)
    threadResult = ResultThread(resultQueue=resultQueue, processSensorActuator=processSensorActuator)
    
    threadRequest.start()
    threadResult.start()
    
    socketThread = SocketThread(endpoint_url=endpoint_url, loginVo=loginVo, processSensorActuator=processSensorActuator, 
                                schemaRequest=schemaRequest, schemaResult=schemaResult, 
                                requestQueue=requestQueue, resultQueue=resultQueue)

    socketThread.start()
    socketThread.join()
    
    threadRequest.shutdown()
    threadResult.shutdown()
    
    threadRequest.join(5)
    threadResult.join(5)
    
    logger.info('Done')


if __name__ == '__main__':
    if( len(sys.argv) < 6 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python endpoint_url loginName sourceName consoleConfigFile.conf optionalFilterSensorName')
        sys.exit(8)
    optionalFilterSensorName = ''
    if  len(sys.argv) > 6:
        optionalFilterSensorName = sys.argv[6]
    main(schemaSourceFolder=sys.argv[1], endpoint_url=sys.argv[2], loginName=sys.argv[3], sourceName=sys.argv[4], loggingConfig=sys.argv[5], optionalFilterSensorName=optionalFilterSensorName)

    
    