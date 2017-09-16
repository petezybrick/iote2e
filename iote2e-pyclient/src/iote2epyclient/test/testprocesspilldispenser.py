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
testprocesspilldispenser - test camera and stepper motor
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import time
from iote2epyclient.pilldispenser.handlepilldispenser import HandlePillDispenser
from iote2epyclient.pilldispenser.blinkledthread import BlinkLedThread
import base64
from io import BytesIO
from picamera import PiCamera
import piplates.DAQCplate as DAQC


def main():
    #blinkLedThread = BlinkLedThread()
    #blinkLedThread.start()
    #time.sleep(5)
    #blinkLedThread.shutdown()
    
    handlePillDispenser = HandlePillDispenser()
    imageStream = BytesIO()
    camera = PiCamera(resolution=(200,200))
    camera.contrast = 0
    camera.sharpness = 100
   
    #dispensePills(handlePillDispenser)
    takePicture(camera, imageStream)
   # leds()
    print('done')
    
def leds():
    DAQC.enableSWint(0)
    DAQC.intEnable(0)
    while True:
        DAQC.setLED(0,0)
        if DAQC.getINTflags(0) == 256:
            break
        time.sleep(.25)
        if DAQC.getINTflags(0) == 256:
            break
        DAQC.clrLED(0,0)
        if DAQC.getINTflags(0) == 256:
            break
        time.sleep(.25)
        if DAQC.getINTflags(0) == 256:
            break
    for i in range(0,100):
        DAQC.clrLED(0,0)
        DAQC.clrLED(0,1)


def dispensePills(handlePillDispenser):
    print('dispensing 3 pills')
    handlePillDispenser.dispensePills(3)
    
def takePicture(camera, imageStream):
    print('Taking picture')
    camera.capture( imageStream, 'png' )
    print('before encode')
    print( 'len=' + str( len(imageStream.getvalue() ) ))
    
    encoded = base64.b64encode(imageStream.getvalue() )
    #print("Encoded=" + encoded )
    print("Encoded len=" + str( len(encoded)))
    imageStream.close()
    
    print('after encode')
    
    decoded = base64.b64decode(encoded)
    print("Decoded=" + decoded )
    print("Decoded=" +  str( len(decoded)))
    
    with open('/tmp/iote2e-test3.png', 'wb') as output:
        output.write(decoded)
        output.close()

if __name__ == '__main__':
    main()
