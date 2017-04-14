import logging
import time
import threading
from cassandra.cqltypes import EMPTY

logger = logging.getLogger(__name__)


class ProcessPillDispenser(object):
    '''
    classdocs
    '''

    def __init__(self, loginVo, sensorName):
        self.loginVo = loginVo
        self.sensorName = sensorName
        self.dispenseState = None
        self.numPillsToDispense = -1
        self.pillsDispensedUuid = None
        self.pillsDispensedDelta = 9999
        self.lockDispenser = threading.RLock()
        
        
    def createIote2eRequest(self ):
        iote2eRequest = None
        self.lockDispenser.acquire()
        if 'PILLDISP_DISPENSE' == self.dispenseState:
            # Tell the pill dispenser to dispense the number of pills
            # Sleep for half a second, then take a picture
            # Byte64 encode the picture
            imageByte64 = 'test b64 image'
            # Create Iote2eRequest that contains the confirmation image
            pairs = { self.sensorName: imageByte64}
            metadata = { 'PILLS_DISPENSED_UUID': self.pillsDispensedUuid}
            iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='pill_dispenser', 
                               request_uuid=str(uuid.uuid4()), 
                               request_timestamp=ClientUtils.nowIso8601(), 
                               pairs=pairs, metadata=metadata, operation='SENSORS_VALUES')
        elif 'PILLDISP_CONFIRM' == self.dispenseState:
            if self.pillsDispensedDelta == 0:
                pass
            else:
                pass             
            
        self.lockDispenser.release()
        
        while True:
            event = self.sense.stick.wait_for_event()
            if 'middle' == event.direction:
                if 'pressed' == event.action:
                    btnPressed = '1'
                elif 'released' == event.action:
                    btnPressed = '0'
                else: 
                    continue
                logger.info( "ProcessSimLedGreen createIote2eRequest {}: {}".format(self.sensorName,btnPressed))
        
                pairs = { self.sensorName: str(btnPressed)}
                iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='switch', 
                                               request_uuid=str(uuid.uuid4()), 
                                               request_timestamp=ClientUtils.nowIso8601(), 
                                               pairs=pairs, operation='SENSORS_VALUES')
                break
            
        return iote2eRequest


    def handleIote2eResult(self, iote2eResult ):
        logger.info('ProcessPillDispenser handleIote2eResult: ' + str(iote2eResult))
        self.lockDispenser.acquire()
        if 'ACTUATOR_VALUES' == iote2eResult.operation:
            self.numPillsToDispense = iote2eResult.pairs['actuatorValue']
            self.pillsDispensedUuid = iote2eResult.metadata['PILLS_DISPENSED_UUID']
            self.dispenseState = "PILLDISP_DISPENSE"
        elif 'ACTUATOR_CONFIRM' == iote2eResult.operation:
            self.pillsDispensedDelta = iote2eResult.pairs['actuatorValue']
            self.dispenseState = "PILLDISP_CONFIRM"
        self.lockDispenser.release()

