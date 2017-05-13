import time
from iote2epyclient.pilldispenser.handlepilldispenser import HandlePillDispenser
import base64
from io import BytesIO
from picamera import PiCamera
import piplates.DAQCplate as DAQC


def main():
    handlePillDispenser = HandlePillDispenser()
    imageStream = BytesIO()
    camera = PiCamera(resolution=(100,100))
    camera.contrast = 0
    camera.sharpness = 100
    camera.capture( imageStream, 'png' )
    
    dispensePills(handlePillDispenser)
    takePicture(camera, imageStream)
    leds()
    print('done')
    
def leds():
    for i in range(0,6):
        DAQC.setLED(0,0)
        time.sleep(.25)
        DAQC.clrLED(0,0)
    for i in range(0,6):
        DAQC.setLED(0,1)
        time.sleep(.25)
        DAQC.clrLED(0,0)

def dispensePills(handlePillDispenser):
    print('dispensing 1 pills')
    handlePillDispenser.dispensePills(1)
    
def takePicture(camera, imageStream):
    print('Taking picture')
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

if __name__ == '__main__':
    main()
