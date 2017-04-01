import time
from processpilldispenser import ProcessPillDispenser

def main():
    processPillDispenser = ProcessPillDispenser()

    for i in range(0,16):
        print('pill ' + str(i))
        processPillDispenser.dispensePill()
        time.sleep(.25)


if __name__ == '__main__':
    main()
