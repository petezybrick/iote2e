'''
Created on Mar 18, 2017

@author: pete
'''
import datetime

class ClientUtils(object):
    '''
    classdocs
    '''

    @staticmethod
    def nowIso8601():
        (dt, micro) = datetime.datetime.utcnow().strftime('%Y-%m-%dT%H:%M:%S.%f').split('.')
        dt = "%s.%03dZ" % (dt, int(micro) / 1000)
        return dt
