'''
Created on Jul 30, 2016

@author: pete
'''

from pyclient.process.processtemptofan import ProcessTempToFan
import sys
# import clientthread
from threading import Thread

def main(endpoint_url, conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    
    
    
    
    # end_point = "ws://hp-lt-ubuntu-1:8090/e2e/" #"ws://echo.websocket.org/"
    # "/home/pete/development/workspace/e2e/e2eclientpython/config/e2e_client_consoleonly.conf"
    #threads = []
    #for i in range(1,2):
    #    #t = Thread(target=client.main, args=(endpoint_url,"test000"+str(i),conf_file,))
    #    t = clientthread.ClientThread( endpoint_url, "test000"+str(i))
    #    t.start()
    #    threads.append(t)
        
        
    cls = globals()["ProcessTempToFan"]
    processTempToFan = cls()
    processTempToFan.process()
    
    # join all threads
    #for t in threads:
    #    t.join()
    logger.info('Done')



if __name__ == '__main__':
    if( len(sys.argv) < 3 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python endpoint_url <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1], sys.argv[2])