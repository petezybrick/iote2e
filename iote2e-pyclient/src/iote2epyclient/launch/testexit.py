'''
Created on Jul 30, 2016

@author: pete
'''

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

    
    