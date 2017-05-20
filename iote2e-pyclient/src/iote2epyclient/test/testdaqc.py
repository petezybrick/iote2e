'''
Created on Feb 18, 2017

@author: pete
'''
import sys
import datetime
import piplates.DAQCplate as DAQC
from time import sleep

def main():
    print('Starting')
    showTemperature()
    print('Done')

    
def showTemperature():
    for i in range(0,10):
        t = round(DAQC.getTEMP(0,0,'c'),2)
        print(t)
        sleep(1)


    
if __name__ == '__main__':
    main()

