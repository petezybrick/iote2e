# Copyright 2016, 2017 Peter Zybrick and others.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""
HandlePillDispenser
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import time
import piplates.MOTORplate as MOTOR
import base64
from io import BytesIO
from picamera import PiCamera

class HandlePillDispenser(object):
    '''
    Methods to dispense pills (stepper motor) and take a picture of the dispensed pills via the Pi's camera
    '''
    def __init__(self, plateAddr=0, plateMotor='a'):
        self.plateAddr = plateAddr
        self.plateMotor = plateMotor

    def dispensePills(self, numPills=1):
        for i in range(0,numPills):
            self.dispensePill()
            time.sleep(.1)

    def dispensePill(self):
        numSteps = 103
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

    def captureImageBase64(self):
        imageStream = BytesIO()
        camera = PiCamera(resolution=(100,100))
        camera.contrast = 0
        camera.sharpness = 100
        camera.capture( imageStream, 'png' )
        encoded = base64.b64encode(imageStream.getvalue() )
        imageStream.close()
        return encoded
