import time
from iote2epyclient.pilldispenser.handlepilldispenser import HandlePillDispenser
import base64
from io import BytesIO
from picamera import PiCamera


def main():
    handlePillDispenser = HandlePillDispenser()
    imageStream = BytesIO()
    camera = PiCamera(resolution=(100,100))
    camera.contrast = 0
    camera.sharpness = 100
    camera.capture( imageStream, 'png' )
    
    dispensePills(handlePillDispenser)
    takePicture(camera, imageStream)
    print('done')

def dispensePills(handlePillDispenser):
    print('dispensing 4 pills')
    handlePillDispenser.dispensePills(4)
    
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
