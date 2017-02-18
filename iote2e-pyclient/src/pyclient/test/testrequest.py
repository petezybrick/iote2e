'''
Created on Feb 18, 2017

@author: pete
'''
import sys
import avro.schema
from pyclient.schema.iote2erequest import Iote2eRequest


def main(conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    logger.info('Starting')
    
    schemaRequest = avro.schema.parse(open('../../../../iote2e-schema/src/main/avro/iote2e-request.avsc', 'rb').read())
    
    testPairs = { 'testPairNameA':'testPairValueA', 'testPairNameB':'testPairValueB' }
    testMetadata = { 'testMetadataNameA':'testMetadataValueA', 'testMetadataNameB':'testMetadataValueB' }
    
    iote2eRequestBefore = Iote2eRequest( login_name='testLogin',source_name='testSourceName', source_type='testSourceType', request_uuid='testRequestUuid', 
                                           request_timestamp='testRequestTimestamp', pairs=testPairs, operation='SENSORS_VALUES', metadata=testMetadata)
    logger.info(iote2eRequestBefore)
    b = Iote2eRequest.commonToAvroBinarySchema( schema=schemaRequest, dictContent=iote2eRequestBefore.__dict__)
    logger.info(len(str(iote2eRequestBefore)))
    logger.info(len(b))
    
    iote2eRequestAfter = Iote2eRequest.requestFromAvroBinarySchema( schema=schemaRequest, rawBytes=b )
    logger.info( iote2eRequestAfter )
    logger.info( iote2eRequestAfter.pairs['testPairNameB'] )


if __name__ == '__main__':
    if( len(sys.argv) < 2 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1])