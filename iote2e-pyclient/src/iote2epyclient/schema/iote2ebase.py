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
Iote2eBase
:author: Pete Zybrick
:contact: pzybrick@gmail.com
:version: 1.0.0
"""

import avro.io
import io
import logging

logger = logging.getLogger(__name__)

class Iote2eBase():
    '''
    Helper methods to convert Avro to/from Binary
    '''
    @staticmethod
    def commonToAvroBinarySchema( schema, dictContent ):
        writer = avro.io.DatumWriter(schema)
        bytes_writer = io.BytesIO()
        encoder = avro.io.BinaryEncoder(bytes_writer)
        writer.write(dictContent,encoder)
        raw_bytes = bytes_writer.getvalue()
        b = bytearray()
        b.extend(raw_bytes)
        return b
    
    @staticmethod
    def commonFromAvroBinarySchema( schema, rawBytes ):
        bytesReader = io.BytesIO(rawBytes)
        decoder = avro.io.BinaryDecoder(bytesReader)
        reader = avro.io.DatumReader(schema)
        while True:
            try:
                obj = reader.read(decoder)
                return obj
            except TypeError as ex:
                if str(ex) == 'ord() expected a character, but string of length 0 found':
                    break
                raise ex                        
            except Exception as ex:
                logger.error( str(type(ex)) )
                logger.error( 'exception ' + str(ex) )
                break