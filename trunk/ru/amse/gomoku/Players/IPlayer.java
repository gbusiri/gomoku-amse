package ru.amse.gomoku.Players;

/**
 *
 */
public interface IPlayer {

    public String getName();

    public void setName(String name);

    public byte getColour();

    public void setColour(byte colour);

    public boolean isNameSet();

    public boolean isColourSet();

    public void makeNextTurn(byte[][] board, byte[] coordinates);

    public byte[] giveNextTurn();
}