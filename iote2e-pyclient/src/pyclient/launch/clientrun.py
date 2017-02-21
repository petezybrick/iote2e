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
import logging
from Queue import Queue
from pyclient.ws.loginvo import LoginVo
from pyclient.ws.socketstate import SocketState
from pyclient.process.processtemptofan import ProcessTempToFan
from pyclient.processsim.processsimtemptofan import ProcessSimTempToFan

logger = logging.getLogger(__name__)


class ClientRun():
    '''
    classdocs
    '''

    def __init__(self, processClassName, schemaSourceFolder, endpoint_url, loginName, sourceName, optionalFilterSensorName ):
        logger.info('ctor')
        self.processClassName = processClassName
        self.schemaSourceFolder = schemaSourceFolder
        self.endpoint_url = endpoint_url
        self.loginName = loginName
        self.sourceName = sourceName
        self.optionalFilterSensorName = optionalFilterSensorName
        if not schemaSourceFolder.endswith('/'):
            schemaSourceFolder += '/'

    def process(self):
    
        schemaRequest = avro.schema.parse(open(self.schemaSourceFolder+'iote2e-request.avsc', 'rb').read())
        schemaResult = avro.schema.parse(open(self.schemaSourceFolder+'iote2e-result.avsc', 'rb').read())
    
        loginVo = LoginVo(loginName=self.loginName, passwordEncrypted='anything', sourceName=self.sourceName, optionalFilterSensorName=self.optionalFilterSensorName)
        
        requestQueue = Queue()
        resultQueue = Queue()
            
        cls = globals()[self.processClassName]
        processSensorActuator = cls(loginVo=loginVo,sensorName='temp1')
        
        self.threadRequest = RequestThread(requestQueue=requestQueue,processSensorActuator=processSensorActuator)
        self.threadResult = ResultThread(resultQueue=resultQueue, processSensorActuator=processSensorActuator)
        
        self.socketThread = SocketThread(endpoint_url=self.endpoint_url, loginVo=loginVo, processSensorActuator=processSensorActuator, 
                                    schemaRequest=schemaRequest, schemaResult=schemaResult, 
                                    requestQueue=requestQueue, resultQueue=resultQueue)
    
        self.socketThread.start()
        #TODO: verify connection
        for i in range(0,4):
            if self.socketThread.socketState == SocketState.ERROR or self.socketThread.socketState == SocketState.CLOSED:
                logger.error("Socket failed to connect: " +self. endpoint_url)
                break;
            time.sleep(1)
        
        socketThreadTimeout = None
        if self.socketThread.socketState == SocketState.ERROR or self.socketThread.socketState == SocketState.CLOSED:
            self.socketThread.shutdown
            self.socketThreadTimeout = 5
        else:   
            self.threadRequest.start()
            self.threadResult.start()
            
        self.socketThread.join(socketThreadTimeout)
    
        if self.threadRequest.is_alive():
            self.threadRequest.shutdown()
            self.threadRequest.join(5)
        if self.threadResult.is_alive():
            self.threadResult.shutdown()
            self.threadResult.join(5)
        
        logger.info('Done')


    
    