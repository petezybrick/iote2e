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
ClientLauncher - main launch point
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""


import sys
import signal
from iote2epyclient.launch.clientrun import ClientRun

clientRun = None

def main( processClassName, sensorName, schemaSourceFolder, endpoint_url, loginName, sourceName, loggingConfig, optionalFilterSensorName):
    import logging.config
    logging.config.fileConfig( loggingConfig, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    
    clientRun = ClientRun(processClassName, sensorName, schemaSourceFolder, endpoint_url, loginName, sourceName, optionalFilterSensorName)
    def signal_handler(signal, frame):
        logger.info('ClientRun main shutdown - start')
        if clientRun.socketThread.is_alive():
            clientRun.socketThread.shutdown()
            clientRun.socketThread.join(5)
        if clientRun.threadRequest.is_alive():
            clientRun.threadRequest.shutdown()
            clientRun.threadRequest.join(5)
        if clientRun.threadResult.is_alive():
            clientRun.threadResult.shutdown()
            clientRun.threadResult.join(5)
        logger.info('ClientRun main shutdown - complete, exiting')
        sys.exit(0)
    signal.signal(signal.SIGINT, signal_handler)
    clientRun.process()
    logger.info('Done')


if __name__ == '__main__':
    if( len(sys.argv) < 8 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python endpoint_url loginName sourceName consoleConfigFile.conf optionalFilterSensorName')
        sys.exit(8)
    optionalFilterSensorName = ''
    if  len(sys.argv) > 8:
        optionalFilterSensorName = sys.argv[8]
    main(processClassName=sys.argv[1], sensorName=sys.argv[2], schemaSourceFolder=sys.argv[3], endpoint_url=sys.argv[4], 
         loginName=sys.argv[5], sourceName=sys.argv[6], 
         loggingConfig=sys.argv[7], optionalFilterSensorName=optionalFilterSensorName)

    
    