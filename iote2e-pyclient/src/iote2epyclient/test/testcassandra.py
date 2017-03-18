'''
Created on Feb 18, 2017

@author: pete
'''
import sys
import datetime
from cassandra.cluster import Cluster

def main(conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    logger.info('Starting')

    cluster = Cluster('127.0.0.1')
    session = cluster.connect('iote2e')
    
    logger.info(session)
    
    cluster.close()

if __name__ == '__main__':
    if( len(sys.argv) < 2 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1])