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
TestExit - learn how to handle a shutdown hook
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import sys
import time
import atexit
import signal
from Queue import Queue
from iote2epyclient.ws.resultthread import ResultThread

threadResult = None
    
def shutdownHook():
    print('>>>> shutdown hook <<<<')
    if threadResult.is_alive():
        threadResult.shutdown()
        threadResult.join(5)
        


# atexit.register(shutdownHook)

def main( ):
    import logging.config
    logging.config.fileConfig( '/home/pete/development/gitrepo/iote2e/iote2e-pyclient/config/client_consoleonly.conf', disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    resultQueue = Queue()
    threadResult = ResultThread(resultQueue=resultQueue, processSensorActuator='test')
    def signal_handler(signal, frame):
        print('main shutdown')
        if threadResult.is_alive():
            threadResult.shutdown()
            threadResult.join(5)
        sys.exit(0)
    signal.signal(signal.SIGINT, signal_handler)
    threadResult.start()
    signal.signal(signal.SIGINT, signal_handler)
    time.sleep(60)
    threadResult.join()

    logger.info("before join")
    logger.info('Done')
    


if __name__ == '__main__':


    main()

    
    