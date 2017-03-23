'''
Created on Feb 18, 2017

@author: pete
'''
import sys
import datetime
from sense_hat import SenseHat
from time import sleep

def main(conf_file):
    import logging.config
    logging.config.fileConfig( conf_file, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    logger.info('Starting')

    sense = SenseHat()
    #showMessages(sense)
    #showLetters(sense)
    #showPixels(sense)
    #showTemperature(sense)
    #showJoystickPoll(sense)
    showJoystickWait(sense)
    sense.clear()

    logger.info('Done')


def showJoystickPoll(sense):
    while True:
        for event in sense.stick.get_events():
            print("The joystick was {} {}".format(event.action,event.direction))
        sleep(.25)
        print('poll')


def showJoystickWait(sense):
    while True:
        event = sense.stick.wait_for_event()
        if "middle" == event.direction:
            if "pressed" == event.action:
                print("1");
            elif "released" == event.action:
                print("0");
        #print("The joystick was {} {}".format(event.action,event.direction))

    
def showTemperature(sense):
    for i in range(0,20):
        t = round(sense.get_temperature(),2)
        print(t)
        sense.show_message("temp (c)={}".format(t), scroll_speed=.025)
        sleep(1.5)


def showMessages(sense):
    sense.show_message("Watson, come here. I need you.", scroll_speed=.025);

 
def showLetters(sense):
    sense.show_letter("R", text_colour=[255,0,0],back_colour=[0,0,0]);
    sleep(1.5)
    sense.show_letter("G", text_colour=[0,255,0],back_colour=[0,0,0]);
    sleep(1.5)
    sense.show_letter("B", text_colour=[0,0,255],back_colour=[0,0,0]);
    sleep(1.5)    

def showPixels(sense):
    b = [0,0,255]
    y = [255,255,0]
    e = [0,0,0]
    image = [
        b,b,e,b,b,e,y,y,
        b,b,e,b,b,e,y,y,
        e,e,e,e,e,e,e,e,
        b,b,e,b,b,e,b,b,
        b,b,e,b,b,e,b,b,
        e,e,e,e,e,e,e,e,
        b,b,e,b,b,e,b,b,
        b,b,e,b,b,e,b,b
        ]
    sense.set_pixels(image)
    angles = [0,90,180,270,0,90,180,270]
    for angle in angles:
        sense.set_rotation(angle)
        sleep(2)

    
if __name__ == '__main__':
    sys.argv = ['testhatsensors.py', '/home/pete/iote2epyclient/log-configs/client_consoleonly.conf']
    if( len(sys.argv) < 2 ):
        print('Invalid format, execution cancelled')
        print('Correct format: python <consoleConfigFile.conf>')
        sys.exit(8)
    main(sys.argv[1])

