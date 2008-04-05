package ru.amse.gomoku.Players.Impl;

import ru.amse.gomoku.Players.Player;
import ru.amse.gomoku.UI.GUI.BoardView;
import ru.amse.gomoku.UI.GUI.GomokuFrame;


/**
 * 
 */
public class PersonPlayer extends Player {

    private byte[] myTurn;
    private BoardView myBoard;
    private GomokuFrame myFrame;

    public PersonPlayer(String name, byte colour, BoardView board, GomokuFrame gameFrame) {
        super(name, colour);
        myBoard = board;
        myFrame = gameFrame;
    }

    public void makeNextTurn(byte[][] board, byte[] coordinates) {

        myBoard.setTurnAllowed(true);
        while (!myBoard.turnIsReady() && !myFrame.isUndoNeeded()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        myTurn = myBoard.getTurn();
        myBoard.setTurnAllowed(false);
    }

    public byte[] giveNextTurn() {
        return myTurn;
    }
}
