import base64
from io import BytesIO
from picamera import PiCamera

imageStream = BytesIO()
camera = PiCamera(resolution=(100,100))
camera.contrast = 0
camera.sharpness = 100
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
