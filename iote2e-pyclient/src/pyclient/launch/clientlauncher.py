'''
Created on Jul 30, 2016

@author: pete
'''

import sys
from pyclient.launch.clientrun import ClientRun

clientRun = None

def main( processClassName, sensorName, schemaSourceFolder, endpoint_url, loginName, sourceName, loggingConfig, optionalFilterSensorName):
    import logging.config
    logging.config.fileConfig( loggingConfig, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    clientRun = ClientRun(processClassName, sensorName, schemaSourceFolder, endpoint_url, loginName, sourceName, optionalFilterSensorName)
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
    if( len(sys.argv) < 8 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python endpoint_url loginName sourceName consoleConfigFile.conf optionalFilterSensorName')
        sys.exit(8)
    #atexit.register(shutdownHook)
    optionalFilterSensorName = ''
    if  len(sys.argv) > 8:
        optionalFilterSensorName = sys.argv[8]
    main(processClassName=sys.argv[1], sensorName=sys.argv[2], schemaSourceFolder=sys.argv[3], endpoint_url=sys.argv[4], 
         loginName=sys.argv[5], sourceName=sys.argv[6], 
         loggingConfig=sys.argv[7], optionalFilterSensorName=optionalFilterSensorName)

    
    