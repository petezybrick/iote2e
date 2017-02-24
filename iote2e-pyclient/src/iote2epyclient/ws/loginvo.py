'''
Created on Feb 20, 2017

@author: pete
'''

class LoginVo(object):
    '''
    classdocs
    '''


    def __init__(self, loginName, passwordEncrypted, sourceName, optionalFilterSensorName=None ):
        self.loginName = loginName
        self.passwordEncrypted = passwordEncrypted
        self.sourceName = sourceName
        self.optionalFilterSensorName = optionalFilterSensorName
