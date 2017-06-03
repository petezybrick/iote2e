'''
Created on Feb 18, 2017

@author: pete
'''
import threading
import time
import logging
import piplates.DAQCplate as DAQC


logger = logging.getLogger(__name__)

class ButtonPushedThread( threading.Thread):
    def __init__(self, daqcBoard=0, pillDispenser ):
        logger.info("Constructing")
        threading.Thread.__init__(self)
        self.daqcBoard = daqcBoard
        self.pillDispenser = pillDispenser
        self.isShutdown = False
    
    def run(self, *args):
        logger.info("ButtonPushedThread run start")
        
        DAQC.enableSWint(self.daqcBoard)
        DAQC.intEnable(self.daqcBoard)
        while True:
            if self.isShutdown: 
                break
            if DAQC.getINTflags(self.daqcBoard) == 256:
                self.pillDispenser.setDispenseState('CONFIRMING')
                break
            time.sleep(.25)     
        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        
        