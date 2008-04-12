package ru.amse.gomoku.players.impl.aiPlayer;


/**
 *
 */
class Border {

    byte[][] possibleTurns;
    final byte MY_RANGE;
    final byte MY_LIMIT;
    int myPossibilities;
    boolean myBegin = false;
    boolean needsReseting;
    byte myCurrentLevel;
    byte[] myCurrentPossibility;

    Border(byte range, byte limit) {
        possibleTurns = new byte[limit][limit];
        MY_RANGE = range;
        MY_LIMIT = limit;
        resetCurrentPossibility();
        addCoordinates(new byte[] {(byte)(MY_LIMIT / 2),(byte)(MY_LIMIT / 2)});
    }

    void addCoordinates(byte[] coordinates) {
        int i = coordinates[0] - MY_RANGE;
        int j = coordinates[1] - MY_RANGE;
        int maxI = coordinates[0] + MY_RANGE;
        int maxJ = coordinates[1] + MY_RANGE;

        if (coordinates[0] - MY_RANGE < 0) {
            i = 0;
        } else if (coordinates[0] + MY_RANGE > MY_LIMIT) {
            maxI = MY_LIMIT;
        }
        if (coordinates[1] - MY_RANGE < 0) {
            j = 0;
        } else if (coordinates[1] + MY_RANGE > MY_LIMIT) {
            maxJ = MY_LIMIT;
        }
        for ( ; i < maxI; i++) {
            for (int l = j ; l < maxJ; l++) {
                if (possibleTurns[i][l] == 0) {
                    possibleTurns[i][l] = 1;
                    myPossibilities++;
                }
            }
        }
        if (myBegin) {
            myPossibilities--;
            possibleTurns[coordinates[0]][coordinates[1]] = 2;
        }
        myBegin = true;
        needsReseting = true;
    }

    byte[] getCoordinates(byte level) {
        if (needsReseting) {
            reseting(level);
            resetCurrentPossibility();
        } else if (myCurrentLevel < level) {
            resetCurrentPossibility();
        } else if (myCurrentLevel > level) {
            reseting(level);
        }
        myCurrentLevel = level;
        byte[] needed = getNextPossibility();
        possibleTurns[needed[0]][needed[1]] = (byte)(-1 - myCurrentLevel);
        /*
        print(possibleTurns);
        */
        return needed;
    }

    void resetCurrentPossibility() {
        myCurrentPossibility = new byte[] {0, -1};
    }

    void reseting(int level) {
        int i = -1 - level;

        for (byte k = 0; k < MY_LIMIT; k++) {
            for (byte l = 0; l < MY_LIMIT; l++) {
                if (possibleTurns[k][l] < i) {
                    possibleTurns[k][l] = 1;
                } else if (possibleTurns[k][l] == i) {
                    myCurrentPossibility[0] = k;
                    myCurrentPossibility[1] = l;
                    possibleTurns[k][l] = 1;
                }
            }
        }
        needsReseting = false;
    }

    byte[] getNextPossibility() {
        do {
            if ((myCurrentPossibility[1] + 1) < MY_LIMIT) {
               myCurrentPossibility[1]++;
            } else {
                myCurrentPossibility[0]++;
                myCurrentPossibility[1] = 0;
            }
            /**
             * add if here to break if increased..
             */
        } while (possibleTurns[myCurrentPossibility[0]][myCurrentPossibility[1]] != 1);
        return myCurrentPossibility;
    }

    void print(int [][] possibilities) {
        for (int k = 0; k < MY_LIMIT; k++) {
            for (int l = 0; l < MY_LIMIT; l++) {
                System.out.print(possibilities[k][l] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}