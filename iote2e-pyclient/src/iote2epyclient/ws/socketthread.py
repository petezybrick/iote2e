'''
Created on Jul 30, 2016

@author: pete
'''

import websocket
import threading
import time
import json
from websocket import ABNF
from Queue import Empty
import logging
from threading import Thread
from iote2epyclient.ws.socketstate import SocketState
from iote2epyclient.schema.iote2erequest import Iote2eRequest
from iote2epyclient.schema.iote2eresult import Iote2eResult


logger = logging.getLogger(__name__)

class SocketThread( threading.Thread):
    
    def __init__(self, endpoint_url, loginVo, processSensorActuator, schemaRequest, schemaResult, requestQueue, resultQueue):
        threading.Thread.__init__(self)
        self.endpoint_url = endpoint_url
        self.loginVo = loginVo
        self.processSensorActuator = processSensorActuator
        self.schemaRequest = schemaRequest
        self.schemaResult = schemaResult        
        self.requestQueue = requestQueue
        self.resultQueue = resultQueue
        self.data_type = ABNF.OPCODE_TEXT
        self.errno = None
        self.strError = None
        self.socketState = SocketState.PENDING
        self.shutdown = False
 
    
    def on_data(self, ws, data, data_type, bcontinue ):
        self.socketState = SocketState.CONNECTED
        #logger.info( self.loginVo.loginName + ' on_data : ' + message)
        if logger.isEnabledFor("DEBUG"):
            logger.debug( self.loginVo.loginName + ' on_data data_type : ' + str(data_type) + ', type() ' + str(type(data)) + ', continue ' + str(bcontinue) )
        # TODO: error recovery/retry loop 
        #self.response_queue.put_nowait(message)
        self.data_type = data_type

    
    def on_message(self, ws, message):
        self.socketState = SocketState.CONNECTED
        if self.data_type == ABNF.OPCODE_TEXT:
            if logger.isEnabledFor("DEBUG"):
                logger.debug( self.loginVo.loginName + ' Rcvd Text: ' + message )
        elif self.data_type == ABNF.OPCODE_BINARY:
            if logger.isEnabledFor("DEBUG"):
                logger.debug( self.loginVo.loginName + ' Rcvd Binary')
            
        logger.info( self.loginVo.loginName + ' len=' + str(len(message)))
        # TODO: error recovery/retry loop
        iote2eResult = Iote2eResult.resultFromAvroBinarySchema( self.schemaResult, message )
        self.resultQueue.put_nowait(iote2eResult)
 
    
    def on_error(self, ws, error):
        self.socketState = SocketState.ERROR
        self.shutdown = True
        self.strError = str(error)
        logger.error( 'Socket connect failure, endpointUrl: {}, loginName: {}, error: {}, '.format(self.endpoint_url, self.loginVo.loginName,self.strError ) )

    
    def on_close(self, ws):
        self.socketState = SocketState.CLOSED
        self.shutdown = True
        logger.info( self.loginVo.loginName + ' client socket closed')

    
    def on_open(self, ws):
        def run(*args):
            loginVoJson = json.dumps(self.loginVo.__dict__)
            ws.send(loginVoJson)
            while True:
                if self.shutdown:
                    break;
                if self.socketState == SocketState.ERROR or self.socketState == SocketState.CLOSED:
                    break
                try :
                    iote2eRequest = self.requestQueue.get(True, 2)
                    if iote2eRequest != None:
                        byteArray = Iote2eRequest.commonToAvroBinarySchema( schema=self.schemaRequest, dictContent=iote2eRequest.__dict__)
                        ws.send(byteArray,opcode=ABNF.OPCODE_BINARY)
                except Empty:
                    pass
            time.sleep(1)
            ws.close()
            logger.info( self.loginVo.loginName + " Thread terminating " )
        t = Thread(target=run, args=())
        t.start()
    
    def shutdown(self):
        logger.info('Shutting down')
        self.shutdown = True
    
    def run(self, *args):
        websocket.enableTrace(False)
        logger.info( 'Client connecting to server, loginName=' + self.loginVo.loginName + ', sourceName=' + self.loginVo.sourceName )
        ws = websocket.WebSocketApp(self.endpoint_url,
                                    on_message = self.on_message,
                                    on_error = self.on_error,
                                    on_data = self.on_data,
                                    on_close = self.on_close,
                                    on_open = self.on_open )
        ws.run_forever()
        if self.strError != None:
            logger.error('WebSockets error: {}'.format(self.strError))
        time.sleep(2) 

    