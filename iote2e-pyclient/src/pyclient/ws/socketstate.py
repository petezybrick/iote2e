'''
Created on Feb 20, 2017

@author: pete
'''

from enum import Enum

class SocketState(Enum):
    PENDING = 1
    CONNECTED = 2
    ERROR = 3
    CLOSED = 4
        