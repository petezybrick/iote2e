import time
import piplates.MOTORplate as MOTOR

class HandlePillDispenser(object):
   
    def __init__(self, plateAddr=0, plateMotor='a'):
        self.plateAddr = plateAddr
        self.plateMotor = plateMotor

    def dispensePill(self):
        numSteps = 102
        self.resetMotor()
        MOTOR.stepperCONFIG(self.plateAddr, self.plateMotor, 'ccw', 3, 500, .02)
        MOTOR.stepperMOVE(self.plateAddr, self.plateMotor, numSteps)
        flag = 1
        while(flag):
            time.sleep(0.1) 
            stat = MOTOR.getINTflag0(self.plateAddr)
            if (stat & 0x20): 
                flag = 0
        stat = 0xff
        while stat:
            time.sleep(.1)
            stat = MOTOR.getINTflag0(0)
        self.resetMotor()

    def resetMotor(self):
        MOTOR.RESET(0)
        stat = 0xff
        while stat:
            time.sleep(.1)
            stat = MOTOR.getINTflag0(0)
        time.sleep(.1)

