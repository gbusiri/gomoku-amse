package ru.amse.gomoku.players;

import javax.swing.*;

/**
 *
 */
public interface IPlayer {

    public String getName();

    public void setName(String name);

    public byte getColour();

    public void setColour(byte colour);

    public ImageIcon getImage();

    public void makeNextTurn(byte[][] board, byte[] coordinates);

    public byte[] giveNextTurn();
}