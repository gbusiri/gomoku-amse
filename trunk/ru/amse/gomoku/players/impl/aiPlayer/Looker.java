package ru.amse.gomoku.players.impl.aiPlayer;

import ru.amse.gomoku.board.IBoard;
import java.util.Random;

/**
 *
 */
class Looker {

    static final boolean ALLOW_RANDOMIZING = true;
    static final int MY_MAX = 1000000;
    private byte myColour;
    Estimator estimator;

    //to be deleted
    private int[][] myEstimations = new int[IBoard.MY_BOARD_SIZE][IBoard.MY_BOARD_SIZE];

    Looker(byte colour1) {

        estimator = new Estimator();
        myColour = colour1;
    }

    byte[] search(byte[][] board) {
        Random r = new Random();
        int size = board.length;
        byte heightNeeded = -1;
        byte widthNeeded = -1;

        int maxEstimated = -1;
        for (byte i = 0; i < size; i++ ) {
            for (byte j = 0; j < size; j++) {
                if (board[i][j] == IBoard.EMPTY_TURN) {
                    int estimated = estimator.estimate(i, j, myColour, board);
                    //myEstimations[i][j] = estimated;
                    if ((estimated > maxEstimated)
                       || ((estimated == maxEstimated)
                          && (ALLOW_RANDOMIZING && r.nextBoolean()))) {
                        maxEstimated = estimated;
                        heightNeeded = i;
                        widthNeeded = j;
                    }
                }
            }
        }
        //print();
        //clear();
        return new byte[]{heightNeeded, widthNeeded};
    }

    private void clear() {
         for (int[] estim : myEstimations) {
            for (int l = 0; l < myEstimations.length; l++) {
                estim[l] = 0;
            }                             
         }
    }

    private void print() {
        for (int[] estim : myEstimations) {
            for (int l = 0; l < myEstimations.length; l++) {
                System.out.printf("%7d ", estim[l]);
            }
            System.out.println(" ");
        }
        System.out.println(" ");

    }
}
