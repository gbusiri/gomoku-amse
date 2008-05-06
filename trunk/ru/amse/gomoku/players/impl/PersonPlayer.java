package ru.amse.gomoku.players.impl;

import ru.amse.gomoku.players.Player;
import ru.amse.gomoku.ui.gui.view.BoardView;
import ru.amse.gomoku.ui.gui.view.GomokuFrame;

import javax.swing.*;

/**
 * 
 */
public class PersonPlayer extends Player {

    private byte[] myTurn;
    private BoardView myBoard;
    private GomokuFrame myFrame;

    public PersonPlayer(String name
                       , byte colour
                       , ImageIcon image
                       , BoardView board
                       , GomokuFrame gameFrame) {
        super(name, colour, image);
        myBoard = board;
        myFrame = gameFrame;
    }

    public PersonPlayer(String name
                       , byte colour
                       , BoardView board
                       , GomokuFrame gameFrame) {
        this(name, colour, null, board, gameFrame);        
    }

    public void makeNextTurn(byte[][] board, byte[] coordinates) {

        myBoard.setTurnAllowed(true);
        while (!myBoard.turnIsReady()
              && !myFrame.isUndoNeeded()
              && !myFrame.isInterrupted()) {
            try {
                Thread.sleep(111);
            } catch (InterruptedException e) {}
        }
        myTurn = myBoard.getTurn();
        myBoard.setTurnAllowed(false);
    }

    public byte[] giveNextTurn() {
        return myTurn;
    }
}
