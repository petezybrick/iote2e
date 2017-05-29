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
    
    def __init__(self, daqcBoard=0, led=1):
        logger.info("Constructing")
        threading.Thread.__init__(self)
        self.daqcBoard = daqcBoard
        self.led = led
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
        
        