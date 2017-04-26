import logging
import time
import base64
import uuid
from iote2epyclient.launch.clientutils import ClientUtils
from iote2epyclient.schema.iote2erequest import Iote2eRequest

logger = logging.getLogger(__name__)


class ProcessSimPillDispenser(object):
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
        self.sense.clear()
        
        
    def createIote2eRequest(self ):
        iote2eRequest = None
        if 'DISPENSING' == self.dispenseState:
            # Tell the pill dispenser to dispense the number of pills
            # self.handlePillDispenser.dispensePills(self.numPillsToDispense)
            # Sleep for half a second, then take a picture
            time.sleep(.5)
            # Byte64 encode the picture
            with open("yourfile.ext", "rb") as image_file:
                imageByte64 = base64.b64encode(image_file.read())
            # Create Iote2eRequest that contains the confirmation image
            pairs = { self.sensorName: imageByte64}
            metadata = { 'PILLS_DISPENSED_UUID': self.pillsDispensedUuid}
            iote2eRequest = Iote2eRequest( login_name=self.loginVo.loginName,source_name=self.loginVo.sourceName, source_type='pill_dispenser', 
                               request_uuid=str(uuid.uuid4()), 
                               request_timestamp=ClientUtils.nowIso8601(), 
                               pairs=pairs, metadata=metadata, operation='SENSORS_VALUES')
        elif 'DISPENSED' == self.dispenseState:
            if self.pillsDispensedDelta == 0:
                for i in range(0,3):
                    if 'CONFIRMED' == self.dispenseState:
                        break
                    self.sense.clear(0,255,0)
                    time.sleep(.5)
                    self.sense.clear()
                    time.sleep(.5)
                    if 'CONFIRMED' == self.dispenseState:
                        break
            else:
                if self.pillsDispensedDelta < 0:
                    msg = "Not enough pills dispensed"
                else:
                    msg = "Too many pills dispensed"
                for i in range(0,3):
                    if 'CONFIRMED' == self.dispenseState:
                        break                    
                    self.sense.show_message(msg, scroll_speed=.025);
                    self.sense.clear(255,0,0)
                    time.sleep(.5)
                    self.sense.clear()
                    time.sleep(.5)
                    if 'CONFIRMED' == self.dispenseState:
                        break
        elif 'CONFIRMED' == self.dispenseState:
                    self.sense.clear()
                    time.sleep(.25)
        return iote2eRequest


    def handleIote2eResult(self, iote2eResult ):
        logger.info('ProcessPillDispenser handleIote2eResult: ' + str(iote2eResult))
        pills_dispensed_state = iote2eResult.metadata['PILLS_DISPENSED_STATE']
        if 'DISPENSING' == pills_dispensed_state:
            self.numPillsToDispense = iote2eResult.pairs['actuatorValue']
            self.pillsDispensedUuid = iote2eResult.metadata['PILLS_DISPENSED_UUID']
            self.dispenseState = 'DISPENSING'
        elif 'DISPENSED' == pills_dispensed_state:
            self.pillsDispensedDelta = iote2eResult.pairs['actuatorValue']
            self.dispenseState = 'DISPENSED'
        elif 'CONFIRMED' == pills_dispensed_state:
            self.pillsDispensedDelta = iote2eResult.pairs['actuatorValue']
            self.dispenseState = 'CONFIRMED'

