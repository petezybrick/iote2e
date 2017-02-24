'''
Created on Feb 18, 2017

@author: pete
'''
import avro.io
import io
import logging

logger = logging.getLogger(__name__)

class Iote2eBase():
    '''
    classdocs
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