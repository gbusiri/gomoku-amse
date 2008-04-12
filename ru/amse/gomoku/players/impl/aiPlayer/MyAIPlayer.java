package ru.amse.gomoku.players.impl.aiPlayer;

import ru.amse.gomoku.players.Player;
import ru.amse.gomoku.board.IBoard;

/**
 * 
 */
public class MyAIPlayer extends Player {

    private byte myOpponentsColour;
    private Looker mySearcher;
    private byte[] myTurn;

    public MyAIPlayer(String name, byte colour) {
        super(name, colour);
        myOpponentsColour = (byte)(colour % 2 + 1);
        mySearcher = new Looker(myColour, myOpponentsColour);
    }

    public void makeNextTurn(byte[][] board, byte[] coordinates) {
        if (coordinates == null) {
            byte[] needed = new byte[] {IBoard.MY_BOARD_SIZE / 2, IBoard.MY_BOARD_SIZE / 2};
            mySearcher.myBorder.addCoordinates(needed);
            myTurn = needed;
        } else {
            myTurn = mySearcher.search(board, coordinates);
        }
    }

    public byte[] giveNextTurn() {
        return myTurn;
    }
}
