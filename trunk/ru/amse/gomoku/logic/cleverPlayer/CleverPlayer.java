package ru.amse.gomoku.logic.cleverPlayer;

import ru.amse.gomoku.logic.player.Player;
import ru.amse.gomoku.logic.board.Board;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 05.03.2008
 * Time: 12:03:41
 * To change this template use File | Settings | File Templates.
 */
public class CleverPlayer /*extends Player */{  /*

    protected final int myOpponentsColour;
    private Looker mySearcher;

    public CleverPlayer(String name, int colour) {
        super(name, true, colour);
        myOpponentsColour = colour % 2 + 1;
        mySearcher = new Looker(myColour, myOpponentsColour);
    }

    public int[] makeNextTurn(int[][] board, int[] coordinates) {
        if (coordinates.length == 0) {
            int[] needed = new int[] {Board.MY_BOARD_SIZE / 2, Board.MY_BOARD_SIZE / 2};
            mySearcher.myBorder.addCoordinates(needed);
            return needed;
        }
        return mySearcher.look(board, coordinates);
    }                                   */
}
