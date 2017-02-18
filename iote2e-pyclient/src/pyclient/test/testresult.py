'''
Created on Feb 18, 2017

@author: pete
'''
import sys
import avro.schema
from pyclient.schema.iote2eresult import Iote2eResult


def main(conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    logger.info('Starting')
    
    schemaResult = avro.schema.parse(open('../../../../iote2e-schema/src/main/avro/iote2e-result.avsc', 'rb').read())
    
    testPairs = { 'testPairNameA':'testPairValueA', 'testPairNameB':'testPairValueB' }
    testMetadata = { 'testMetadataNameA':'testMetadataValueA', 'testMetadataNameB':'testMetadataValueB' }
    
    iote2eResultBefore = Iote2eResult( login_name='testLogin',source_name='testSourceName', source_type='testSourceType', result_uuid='testResultUuid', 
                                           result_timestamp='testResultTimestamp', pairs=testPairs, operation='ACTUATOR_VALUES', metadata=testMetadata,
                                           request_uuid='testRequestUuid', request_timestamp='testRequestTimestamp')
    logger.info(iote2eResultBefore.__dict__)
    b = Iote2eResult.commonToAvroBinarySchema(schema=schemaResult, dictContent=iote2eResultBefore.__dict__)
    logger.info(len(str(iote2eResultBefore)))
    logger.info(len(b))
    
    iote2eResultAfter = Iote2eResult.resultFromAvroBinarySchema( schema=schemaResult, rawBytes=b )
    logger.info( iote2eResultAfter )
    logger.info( iote2eResultAfter.pairs['testPairNameA'] )
    logger.info( iote2eResultAfter.result_code )

if __name__ == '__main__':
    if( len(sys.argv) < 2 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1])