'''
Created on Feb 18, 2017

@author: pete
'''
import threading
import time
import logging
import piplates.DAQCplate as DAQC


logger = logging.getLogger(__name__)

class BlinkLedThread( threading.Thread):
    # Red=0, Green=1
    def __init__(self, daqcBoard=0, ledColor='green'):
        logger.info("Constructing")
        threading.Thread.__init__(self)
        self.daqcBoard = daqcBoard
        if ledColor == 'green':
            self.led = 1
        else:
            self.led = 0
        self.isShutdown = False

    
    def run(self, *args):
        logger.info("BlinkLedThread run start")

        while True:
            if self.isShutdown: 
                break
            for i in range(0,100):
                DAQC.clrLED(self.daqcBoard, 0)
                DAQC.clrLED(self.daqcBoard, 1)
            time.sleep(.25)           
            for i in range(0,100):
                DAQC.setLED(self.daqcBoard, self.led)
            time.sleep(.25)
        for i in range(0,100):
            DAQC.clrLED(self.daqcBoard, 0)
            DAQC.clrLED(self.daqcBoard, 1)            

        
    def shutdown(self):
        logger.info("Shutting down")
        self.isShutdown = True
        
        