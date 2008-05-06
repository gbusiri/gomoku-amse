package ru.amse.gomoku.players.impl.cleverPlayer;

/**
 * Needed to find out all the possible turns one by one on the given board.
 */

class Possibilities {

    /**
     * board's sideLength.
     */
    final int MY_LIMIT;

    /**
     * reference to the board.
     */
    private int[][] myPosition;

    /**
     * current position of the mark.
     */
    private int[] myCoordinates;

    Possibilities(Border border) {
        myPosition = border.possibleTurns;
        MY_LIMIT = myPosition.length;
        resetCoordinates();
    }

    /**
     * moves mark until the first available move is found.
     *
     * @return next available possibility.
     */
    int[] getNextPossibility() {
        do {
            if ((myCoordinates[1] + 1) < MY_LIMIT) {
               myCoordinates[1]++;
            } else {
                myCoordinates[0]++;
                myCoordinates[1] = 0;
            }
        } while (myPosition[myCoordinates[0]][myCoordinates[1]] != 1);
        return myCoordinates;
    }

    /**
     * sets mark to the beginning.
     */
    void resetCoordinates() {
        myCoordinates = new int[] {0, -1};
    }

    //to be deleted................
    void print() {
        for (int k = 0; k < MY_LIMIT; k++) {
            for (int l = 0; l < MY_LIMIT; l++) {
                System.out.print(myPosition[k][l] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
        System.out.println(myCoordinates[0] + "  " + myCoordinates[1]);
        System.out.println(" ");
    }
}
