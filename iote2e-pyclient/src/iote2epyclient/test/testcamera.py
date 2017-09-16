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
testcamera - Test the RPi camera
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

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
