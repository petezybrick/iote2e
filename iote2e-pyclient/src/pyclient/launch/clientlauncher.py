'''
Created on Jul 30, 2016

@author: pete
'''

from pyclient.ws.requestthread import RequestThread
from pyclient.ws.resultthread import ResultThread
from pyclient.ws.socketthread import SocketThread
import sys
import avro
import time
import uuid
import atexit
from Queue import Queue
from pyclient.ws.loginvo import LoginVo
from pyclient.ws.socketstate import SocketState
from pyclient.process.processtemptofan import ProcessTempToFan
from pyclient.processsim.processsimtemptofan import ProcessSimTempToFan
from pyclient.launch.clientrun import ClientRun

clientRun = None

def main( processClassName, schemaSourceFolder, endpoint_url, loginName, sourceName, loggingConfig, optionalFilterSensorName):
    import logging.config
    logging.config.fileConfig( loggingConfig, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    clientRun = ClientRun(processClassName, schemaSourceFolder, endpoint_url, loginName, sourceName, optionalFilterSensorName)
    clientRun.process()
    logger.info('Done')
    
    
def shutdownHook():
    print('>>>> shutdown hook <<<<')
    if clientRun.socketThread.is_alive():
        clientRun.socketThread.shutdown
        clientRun.socketThread.join(5)
    if clientRun.threadRequest.is_alive():
        clientRun.threadRequest.shutdown()
        clientRun.threadRequest.join(5)
    if clientRun.threadResult.is_alive():
        clientRun.threadResult.shutdown()
        clientRun.threadResult.join(5)


if __name__ == '__main__':
    if( len(sys.argv) < 7 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python endpoint_url loginName sourceName consoleConfigFile.conf optionalFilterSensorName')
        sys.exit(8)
    #atexit.register(shutdownHook)
    optionalFilterSensorName = ''
    if  len(sys.argv) > 7:
        optionalFilterSensorName = sys.argv[7]
    main(processClassName=sys.argv[1], schemaSourceFolder=sys.argv[2], endpoint_url=sys.argv[3], loginName=sys.argv[4], sourceName=sys.argv[5], 
         loggingConfig=sys.argv[6], optionalFilterSensorName=optionalFilterSensorName)

    
    