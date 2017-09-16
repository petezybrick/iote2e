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
Iote2eResult
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

from iote2epyclient.schema.iote2ebase import Iote2eBase

class Iote2eResult(Iote2eBase):
    '''
    Avro schemea for result
    '''

    def __init__(self, login_name, source_name, source_type, request_uuid, request_timestamp, pairs, operation, result_uuid, result_timestamp, result_code=0, result_error_message='', metadata={} ):
        self.login_name = login_name
        self.source_name = source_name
        self.source_type = source_type
        self.request_uuid = request_uuid
        self.request_timestamp = request_timestamp
        self.result_code = result_code
        self.result_error_message = result_error_message
        self.result_uuid = result_uuid
        self.result_timestamp = result_timestamp
        self.pairs = pairs
        self.operation = operation
        self.metadata = metadata
                

    def __str__(self):
        return 'login_name={},source_name={},source_type={},request_uuid={},request_timestamp={},result_code={},result_error_message={},result_uuid={},result_timestamp={},pairs={},operation={}, metadata={}' \
            .format(self.login_name,self.source_name,self.source_type,self.request_uuid,self.request_timestamp,self.result_code,self.result_error_message,self.result_uuid,self.result_timestamp,self.pairs,self.operation,self.metadata)
    
    @staticmethod
    def resultFromAvroBinarySchema( schema, rawBytes ):
        obj = Iote2eBase.commonFromAvroBinarySchema( schema, rawBytes)
        iote2eResult = Iote2eResult( login_name=obj['login_name'], source_name=obj['source_name'], source_type =obj['source_type'],
                                               request_uuid=obj['request_uuid'], request_timestamp=obj['request_timestamp'],
                                               result_code=obj['result_code'], result_error_message=obj['result_error_message'],
                                               result_uuid=obj['result_uuid'], result_timestamp=obj['result_timestamp'],
                                               pairs=obj['pairs'], operation=obj['operation'], metadata=obj['metadata'])
        return iote2eResult
        