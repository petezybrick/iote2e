'''
Created on Feb 18, 2017

@author: pete
'''
import threading
import time
import logging
import avro
from Queue import Queue,Empty
from iote2epyclient.schema.iote2erequest import Iote2eRequest


logger = logging.getLogger(__name__)

class RequestThread( threading.Thread):
    
    def __init__(self, requestQueue,processSensorActuator):
        logger.info("Constructing")
        threading.Thread.__init__(self)
        self.requestQueue = requestQueue
        self.processSensorActuator = processSensorActuator
        self.isShutdown = False

    
    def run(self, *args):
        logger.info("RequestThread run start")

        while True:
            try:
                if self.isShutdown: 
                    break
                #iote2eRequest = self.requestQueue.get(True, 2)
                iote2eRequest = self.processSensorActuator.createIote2eRequest()
                if iote2eRequest != None:
                    logger.info("Sending Request: " + str(iote2eRequest) )
                    self.requestQueue.put_nowait(iote2eRequest)

            except Empty:
                pass

        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        self.requestQueue.put_nowait(None)
        
        