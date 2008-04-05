package ru.amse.gomoku.Players.Impl.cleverPlayer;

/**
 * keeps and modifies a range from which we get possible moves.
 * it is possible to set coordinates and according to them
 * get possible turns worth to make
 */
class Border {

    int[][] possibleTurns;
    final int MY_RANGE;
    final int MY_LIMIT;

    /**
     * total number of possible turns which are worth to explore.
     */
    int myPossibilities;
    boolean myBegin = false;
    boolean needsReseting;
    int myCurrentLevel;

    /**
     * mark set to position from where to start giving possible turns.
     */
    int[] myCurrentPossibility;

    //to be deleted................
    private boolean allowPrinting = false;

    Border(int range, int limit) {
        possibleTurns = new int[limit][limit];
        MY_RANGE = range;
        MY_LIMIT = limit;
        resetCurrentPossibility();
        addCoordinates(new int[] {MY_LIMIT / 2, MY_LIMIT / 2});
    }

    /**
     * adds to the given position a dib by setting value of possible turns
     * in that position -2.
     * spreads possible turns which are worth to explore according to new dib.
     *
     * @param coordinates- height and width of added dib.
     */
    void addCoordinates(int[] coordinates) {
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

        // spreads turns possible to make.
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
        reseting(0);
        resetCurrentPossibility();

        //to be deleted................
        if (allowPrinting) {
            print(possibleTurns);
        }
    }

    void setCoordinates(int height, int width, int level) {

        reseting(level);
        resetCurrentPossibility();
        possibleTurns[height][width] = -1 - level;
        myCurrentLevel = level;

        //to be deleted................
        if (allowPrinting) {
            print(possibleTurns);
        }
    }

    int[] getCoordinates(int level) {
        int[] needed;

        if (myCurrentLevel < level) {
            resetCurrentPossibility();
        } else if (myCurrentLevel > level) {
            reseting(level);
        }
        myCurrentLevel = level;

        needed = getNextPossibility();
        possibleTurns[needed[0]][needed[1]] = -1 - myCurrentLevel;

        //to be deleted................
        if (allowPrinting) {
            print(possibleTurns);
        }
        return needed;
    }

    void resetCurrentPossibility() {
        myCurrentPossibility = new int[] {0, -1};
    }

    void reseting(int level) {
        int i = -1 - level;

        for (int k = 0; k < MY_LIMIT; k++) {
            for (int l = 0; l < MY_LIMIT; l++) {
                if (possibleTurns[k][l] < i) {
                    possibleTurns[k][l] = 1;
                } else if (possibleTurns[k][l] == i) {
                    possibleTurns[k][l] = 1;
                    myCurrentPossibility[0] = k;
                    myCurrentPossibility[1] = l;
                }
            }
        }
        needsReseting = false;
    }

    /**
     *
     * @return the next possible move which is not yet touched.
     */
    int[] getNextPossibility() {
        do {
            if ((myCurrentPossibility[1] + 1) < MY_LIMIT) {
               myCurrentPossibility[1]++;
            } else {
                myCurrentPossibility[0]++;
                myCurrentPossibility[1] = 0;
            }
        } while (possibleTurns[myCurrentPossibility[0]][myCurrentPossibility[1]] != 1);
        return myCurrentPossibility;
    }

    // to be deleted....
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
