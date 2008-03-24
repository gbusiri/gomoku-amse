package ru.amse.gomoku.logic.player;

import ru.amse.gomoku.UI.BoardView;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 27.02.2008
 * Time: 12:03:06
 * To change this template use File | Settings | File Templates.
 */
public class PersonPlayer extends Player {

    private int[] myTurn;
    private BoardView myBoard;

    public PersonPlayer(String name, int colour, BoardView board) {
        super(name, false, colour);
        myBoard = board;
    }

    public void makeNextTurn(int[][] board, int[] coordinates) {
        while (!myBoard.turnIsReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        myTurn = myBoard.getTurn();
    }

    public int[] giveNextTurn() {
        return myTurn;
    }
}
