'''
Created on Feb 18, 2017

@author: pete
'''
import threading
import time
import logging
import avro
from Queue import Queue,Empty
from pyclient.schema.iote2erequest import Iote2eRequest


logger = logging.getLogger(__name__)

class RequestThread( threading.Thread):
    
    def __init__(self, requestQueue,processSensorActuator):
        threading.Thread.__init__(self)
        self.requestQueue = requestQueue
        self.processSensorActuator = processSensorActuator
        self.isShutdown = False

    
    def run(self, *args):
        logger.info("RequestThread run start")
        schemaRequest = avro.schema.parse(open('../../../../iote2e-schema/src/main/avro/iote2e-request.avsc', 'rb').read())

        while True:
            try:
                if self.isShutdown: 
                    break
                #iote2eRequest = self.requestQueue.get(True, 2)
                iote2eRequest = self.processSensorActuator.createIote2eRequest()
                if iote2eRequest != None:
                    logger.info("Sending Request: " + str(iote2eRequest) )
                    rawBytes = Iote2eRequest.commonToAvroBinarySchema( schema=schemaRequest, dictContent=iote2eRequest.__dict__)
                    logger.info("Sending Request, raw bytes length: " + str(len(rawBytes) ) )

            except Empty:
                pass

        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        self.requestQueue.put_nowait(None)
        
        