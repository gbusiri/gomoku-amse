package ru.amse.gomoku.Players.Impl;

import ru.amse.gomoku.Players.Player;
import ru.amse.gomoku.Players.IPlayer;
import ru.amse.gomoku.UI.CUI.*;
import ru.amse.gomoku.UI.CUI.impl.View;

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
