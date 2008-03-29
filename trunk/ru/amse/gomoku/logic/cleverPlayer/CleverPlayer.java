package ru.amse.gomoku.logic.cleverPlayer;

import ru.amse.gomoku.logic.player.Player;
import ru.amse.gomoku.logic.board.Board;

/**
 *
 */
public class CleverPlayer extends Player {

    final int myOpponentsColour;
    private Looker mySearcher;
    private int[] myTurn;

    public CleverPlayer(String name, int colour) {
        super(name, true, colour);
        myOpponentsColour = colour % 2 + 1;
        mySearcher = new Looker(myColour, myOpponentsColour);
    }

    public void makeNextTurn(int[][] board, int[] coordinates) {
        if (coordinates.length == 0) {
            int[] needed = new int[] {Board.MY_BOARD_SIZE / 2, Board.MY_BOARD_SIZE / 2};
            mySearcher.myBorder.addCoordinates(needed);
            myTurn = needed;
        } else {
            myTurn = mySearcher.look(board, coordinates);
        }
    }

    public int[] giveNextTurn() {
        return myTurn;
    }
}
