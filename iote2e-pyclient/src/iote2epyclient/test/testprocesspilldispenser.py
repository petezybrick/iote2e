import time
from iote2epyclient.pilldispenser.handlepilldispenser import HandlePillDispenser
from iote2epyclient.pilldispenser.blinkledthread import BlinkLedThread
import base64
from io import BytesIO
from picamera import PiCamera
import piplates.DAQCplate as DAQC


def main():
    blinkLedThread = BlinkLedThread()
    blinkLedThread.start()
    time.sleep(5)
    blinkLedThread.shutdown()
    
    handlePillDispenser = HandlePillDispenser()
    imageStream = BytesIO()
    camera = PiCamera(resolution=(200,200))
    camera.contrast = 0
    camera.sharpness = 100
   
    dispensePills(handlePillDispenser)
    takePicture(camera, imageStream)
    leds()
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
    
    with open('/tmp/iote2e-test.png', 'wb') as output:
        output.write(decoded)
        output.close()

if __name__ == '__main__':
    main()
