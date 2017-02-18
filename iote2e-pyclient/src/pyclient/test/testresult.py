'''
Created on Feb 18, 2017

@author: pete
'''
import sys
import avro.schema
from pyclient.schema.iote2eresultitem import Iote2eResultItem


def main(conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    logger.info('Starting')
    
    schemaResult = avro.schema.parse(open('../../../../iote2e-schema/src/main/avro/iote2e-result.avsc', 'rb').read())
    
    testPairs = { 'testPairNameA':'testPairValueA', 'testPairNameB':'testPairValueB' }
    testMetadata = { 'testMetadataNameA':'testMetadataValueA', 'testMetadataNameB':'testMetadataValueB' }
    
    iote2eResultItem = Iote2eResultItem( login_name='testLogin',source_name='testSourceName', source_type='testSourceType', result_uuid='testResultUuid', 
                                           result_timestamp='testResultTimestamp', pairs=testPairs, operation='ACTUATOR_VALUES', metadata=testMetadata,
                                           request_uuid='testRequestUuid', request_timestamp='testRequestTimestamp')
    logger.info(iote2eResultItem.__dict__)
    b = iote2eResultItem.toAvroBinarySchema(schemaResult)
    logger.info(len(str(iote2eResultItem)))
    logger.info(len(b))


if __name__ == '__main__':
    if( len(sys.argv) < 2 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1])