import time
from iote2epyclient.pilldispenser.handlepilldispenser import HandlePillDispenser

def main():
    handlePillDispenser = HandlePillDispenser()

    for i in range(1,16):
        print('pill ' + str(i))
        handlePillDispenser.dispensePills(i)
        time.sleep(.25)


if __name__ == '__main__':
    main()
