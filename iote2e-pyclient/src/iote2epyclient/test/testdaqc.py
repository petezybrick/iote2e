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
testdaqc - Test the Data Acquistion board
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import sys
import datetime
import piplates.DAQCplate as DAQC
from time import sleep

def main():
    print('Starting')
    showTemperature()
    print('Done')

    
def showTemperature():
    for i in range(0,10):
        t = round(DAQC.getTEMP(0,0,'c'),2)
        print(t)
        sleep(1)


    
if __name__ == '__main__':
    main()

