'''
Created on Jul 30, 2016

@author: pete
'''

from pyclient.process.processtemptofan import ProcessTempToFan
from pyclient.ws.requestthread import RequestThread
from pyclient.ws.resultthread import ResultThread
import sys
import time
from threading import Thread
from Queue import Queue


def main(endpoint_url, conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    
    requestQueue = Queue()
    resultQueue = Queue()
    
    threadRequest = RequestThread(requestQueue)
    threadResult = ResultThread(resultQueue)
    
    threadRequest.start()
    threadResult.start()
    
    for i in range(1,5):
        requestQueue.put("testRequest-" + str(i) )
        resultQueue.put("testResult-" + str(i) )
        time.sleep(.5)
    
    #cls = globals()["ProcessTempToFan"]
    #processTempToFan = cls()
    #processTempToFan.process()    

    threadRequest.shutdown()
    threadResult.shutdown()
    
    threadRequest.join()
    threadResult.join()
    
    logger.info('Done')


if __name__ == '__main__':
    if( len(sys.argv) < 3 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python endpoint_url <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1], sys.argv[2])