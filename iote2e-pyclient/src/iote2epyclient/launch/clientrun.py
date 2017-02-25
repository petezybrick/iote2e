'''
Created on Jul 30, 2016

@author: pete
'''

from iote2epyclient.ws.requestthread import RequestThread
from iote2epyclient.ws.resultthread import ResultThread
from iote2epyclient.ws.socketthread import SocketThread
import avro
import time
import logging
from Queue import Queue
from iote2epyclient.ws.loginvo import LoginVo
from iote2epyclient.ws.socketstate import SocketState
from iote2epyclient.process.processtemptofan import ProcessTempToFan
from iote2epyclient.processsim.processsimtemptofan import ProcessSimTempToFan
from iote2epyclient.processsim.processsimhumiditytomister import ProcessSimHumidityToMister
from iote2epyclient.processsim.processsimledgreen import ProcessSimLedGreen

logger = logging.getLogger(__name__)


class ClientRun():
    '''
    classdocs
    '''

    def __init__(self, processClassName, sensorName, schemaSourceFolder, endpoint_url, loginName, sourceName, optionalFilterSensorName ):
        logger.info('ctor')
        self.processClassName = processClassName
        self.sensorName = sensorName
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
        processSensorActuator = cls(loginVo=loginVo,sensorName=self.sensorName)
        
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
        
        if self.socketThread.socketState == SocketState.ERROR or self.socketThread.socketState == SocketState.CLOSED:
            self.socketThread.shutdown
            self.socketThread.join(5)
            if self.threadRequest.is_alive():
                self.threadRequest.shutdown()
                self.threadRequest.join(5)
            if self.threadResult.is_alive():
                self.threadResult.shutdown()
                self.threadResult.join(5)
        else:
            self.threadRequest.start()
            self.threadResult.start()
            # need to do this in short join loop for SIGINT to be able to interrupt
            while True:
                time.sleep(60*60*24)
                #self.socketThread.join(1)    
    
        logger.info('Done')


    
    