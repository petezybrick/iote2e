import time
from iote2epyclient.pilldispenser.handlepilldispenser import HandlePillDispenser

def main():
    handlePillDispenser = HandlePillDispenser()
    testDispensePills(handlePillDispenser)
    print('done')

def testDispensePills(handlePillDispenser):
    print('dispensing 4 pills')
    handlePillDispenser.dispensePills(4)

if __name__ == '__main__':
    main()
