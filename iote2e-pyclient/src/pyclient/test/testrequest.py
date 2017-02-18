'''
Created on Feb 18, 2017

@author: pete
'''
import sys
import avro.schema
from pyclient.schema.iote2erequestitem import Iote2eRequestItem


def main(conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    logger.info('Starting')
    
    schemaRequest = avro.schema.parse(open('../../../../iote2e-schema/src/main/avro/iote2e-request.avsc', 'rb').read())
    
    testPairs = { 'testPairNameA':'testPairValueA', 'testPairNameB':'testPairValueB' }
    testMetadata = { 'testMetadataNameA':'testMetadataValueA', 'testMetadataNameB':'testMetadataValueB' }
    
    iote2eRequestItem = Iote2eRequestItem( login_name='testLogin',source_name='testSourceName', source_type='testSourceType', request_uuid='testRequestUuid', 
                                           request_timestamp='testRequestTimestamp', pairs=testPairs, operation='SENSORS_VALUES', metadata=testMetadata)
    logger.info(iote2eRequestItem)
    b = iote2eRequestItem.toAvroBinarySchema(schemaRequest)
    logger.info(len(str(iote2eRequestItem)))
    logger.info(len(b))


if __name__ == '__main__':
    if( len(sys.argv) < 2 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1])