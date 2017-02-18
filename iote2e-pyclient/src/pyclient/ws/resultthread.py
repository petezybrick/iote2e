'''
Created on Feb 18, 2017

@author: pete
'''
import threading
import time
import logging
from Queue import Queue,Empty

logger = logging.getLogger(__name__)

class ResultThread( threading.Thread):
    
    def __init__(self,resultQueue, processSensorActuator):
        logger.info("Shutting down")
        threading.Thread.__init__(self)
        self.resultQueue = resultQueue
        self.processSensorActuator = processSensorActuator
        self.isShutdown = False
    
    def run(self, *args):
        logger.info("ResultThread run start")
        while True:
            try:
                if self.isShutdown: 
                    break
                iote2eResult = self.resultQueue.get(True, 2)
                if iote2eResult != None:
                    logger.info("Rcvd Result: " + str(iote2eResult))
                    self.processSensorActuator.handleIote2eResult( iote2eResult )

            except Empty:
                pass
        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        self.resultQueue.put_nowait(None)
