'''
Created on Feb 18, 2017

@author: pete
'''
import avro.io
import io

class Iote2eResultItem():
    '''
    classdocs
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
    
    def toAvroBinarySchema(self, schema ):
        writer = avro.io.DatumWriter(schema)
        bytes_writer = io.BytesIO()
        encoder = avro.io.BinaryEncoder(bytes_writer)
        writer.write(self.__dict__,encoder)
        raw_bytes = bytes_writer.getvalue()
        b = bytearray()
        b.extend(raw_bytes)
        return b
