'''
Created on Feb 18, 2017

@author: pete
'''
import avro.io
import io

class Iote2eRequestItem():
    '''
    classdocs
    '''

    def __init__(self, login_name, source_name, source_type, request_uuid, request_timestamp, pairs, operation, metadata={} ):
        self.login_name = login_name
        self.source_name = source_name
        self.source_type = source_type
        self.request_uuid = request_uuid
        self.request_timestamp = request_timestamp
        self.pairs = pairs
        self.operation = operation
        self.metadata = metadata

    def __str__(self):
        return 'login_name={}, source_name={}, source_type={}, request_uuid={}, request_timestamp={}, pairs={}, operation={}, metadata={}'.format(self.login_name, self.source_name, self.source_type, self.request_uuid, self.request_timestamp, self.pairs, self.operation, self.metadata)
    
    def toAvroBinarySchema(self, schemaRequest ):
        writer = avro.io.DatumWriter(schemaRequest)
        bytes_writer = io.BytesIO()
        encoder = avro.io.BinaryEncoder(bytes_writer)
        writer.write(self.__dict__,encoder)
        raw_bytes = bytes_writer.getvalue()
        b = bytearray()
        b.extend(raw_bytes)
        return b
    