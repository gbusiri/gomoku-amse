package ru.amse.gomoku.logic.cleverPlayer;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 13.03.2008
 * Time: 14:18:03
 * To change this template use File | Settings | File Templates.
 */
class Possibilities {

    final int MY_LIMIT;
    private int[][] myPosition;
    private int[] myCoordinates;

    Possibilities(int[][] currentBoard) {
        myPosition = currentBoard;
        myCoordinates = new int[] {0, -1};
        MY_LIMIT = myPosition.length;
    }

    int[] getNextPossibility() {
        do {
            if ((myCoordinates[1] + 1) < MY_LIMIT) {
               myCoordinates[1]++;
            } else {
                myCoordinates[0]++;
                myCoordinates[1] = 0;
            }
        } while (myPosition[myCoordinates[0]][myCoordinates[1]] != 0);
        return myCoordinates;
    }
}
