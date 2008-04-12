package ru.amse.gomoku.players.impl;

import ru.amse.gomoku.players.Player;
import ru.amse.gomoku.ui.cui.*;
import ru.amse.gomoku.ui.cui.View;

/**
 *
 */
public class PPlayer extends Player {

    private byte[] myTurn;

    public PPlayer(String name, byte colour) {
        super(name, colour);
    }

    public void makeNextTurn(byte[][] board, byte[] coordinates) {
        IView view = new View();
        myTurn = view.tellPlayerToMakeMove(myName);
    }

    public byte[] giveNextTurn() {
        return myTurn;
    }
}
