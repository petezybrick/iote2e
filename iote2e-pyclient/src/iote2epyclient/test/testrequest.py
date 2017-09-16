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
testrequest - test schema handling for Iote2eRequest
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import sys
import avro.schema
from iote2epyclient.schema.iote2erequest import Iote2eRequest


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