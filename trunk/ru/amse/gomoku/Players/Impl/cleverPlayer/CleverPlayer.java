package ru.amse.gomoku.Players.Impl.cleverPlayer;

import ru.amse.gomoku.Players.Player;
import ru.amse.gomoku.Players.IPlayer;
import ru.amse.gomoku.Board.IBoard;

/**
 *
 */
public class CleverPlayer extends Player {

    byte myOpponentsColour;
    private Looker mySearcher;
    private byte[] myTurn = new byte[]{1, 1};

    public CleverPlayer(String name, byte  colour) {
        super(name, colour);
        myOpponentsColour = (byte)(colour % 2 + 1);
        mySearcher = new Looker(myColour, myOpponentsColour);
    }
    
    public void makeNextTurn(byte[][] board, byte[] coordinates) {
        if (coordinates == null) {
            byte[] needed = new byte[] {IBoard.MY_BOARD_SIZE / 2, IBoard.MY_BOARD_SIZE / 2};
            //mySearcher.myBorder.addCoordinates(needed);
            myTurn = needed;
        } else {
            //myTurn = mySearcher.look(board, coordinates);
        }
    }

    public byte[] giveNextTurn() {
        return myTurn;
    }
}
