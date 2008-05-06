package ru.amse.gomoku.players.impl.aiPlayer;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.players.Player;

/**
 * 
 */
public class MyAIPlayer extends Player {

    private Looker mySearcher;
    private byte[] myTurn;

    public MyAIPlayer(String name, byte colour) {
        super(name, colour);
        mySearcher = new Looker(myColour);
    }

    public MyAIPlayer() {
        super();
        mySearcher = new Looker(myColour);
    }

    public void makeNextTurn(byte[][] board, byte[] coordinates) {
        if (coordinates == null) {
            myTurn = new byte[] {IBoard.MY_BOARD_SIZE / 2, IBoard.MY_BOARD_SIZE / 2};
        } else {
            myTurn = mySearcher.search(board);
        }
    }

    public byte[] giveNextTurn() {
        return myTurn;
    }
}
