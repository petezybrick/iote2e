'''
Created on Feb 18, 2017

@author: pete
'''
from iote2epyclient.schema.iote2ebase import Iote2eBase


class Iote2eRequest(Iote2eBase):
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
    
    @staticmethod
    def requestFromAvroBinarySchema( schema, rawBytes ):
        obj = Iote2eBase.commonFromAvroBinarySchema( schema, rawBytes)
        iote2eRequest = Iote2eRequest( login_name=obj['login_name'], source_name=obj['source_name'], source_type =obj['source_type'],
                                               request_uuid=obj['request_uuid'],request_timestamp=obj['request_timestamp'],pairs=obj['pairs'],
                                               operation=obj['operation'],metadata=obj['metadata'])
        return iote2eRequest
        
        
