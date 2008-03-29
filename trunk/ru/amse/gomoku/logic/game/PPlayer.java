package ru.amse.gomoku.logic.game;

import ru.amse.gomoku.logic.view.View;
import ru.amse.gomoku.logic.player.Player;

/**
 *
 */
public class PPlayer extends Player {

    private int[] myTurn;

    public PPlayer(String name, int colour) {
        super(name, false, colour);
    }

    public void makeNextTurn(int[][] board, int[] coordinates) {
        View view = new View();
        myTurn = view.tellPlayerToMakeMove(myName);
    }

    public int[] giveNextTurn() {
        return myTurn;
    }


}
