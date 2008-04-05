package ru.amse.gomoku.Players;

import ru.amse.gomoku.Board.IBoard;

/**
 * 
 */
public abstract class Player implements IPlayer {

    protected String myName = "";
    protected byte myColour;

    protected boolean myNameSet;
    protected boolean myColourSet;

    public Player(String name, byte colour) {
        myName = name;
        myNameSet = true;
        setColour(colour);
    }
    
    public abstract void makeNextTurn(byte[][] board, byte[] coordinates);

    public abstract byte[] giveNextTurn();

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        myName = name;
        myNameSet = true;
    }

    public byte getColour() {
        return myColour;
    }

    public void setColour(byte colour) {
        if (colour % 2 == 0) {
            myColour = IBoard.DIB_COLOUR_SECOND;
        } else {
            myColour = IBoard.DIB_COLOUR_FIRST;
        }
        myColourSet = true;
    }

    public boolean isNameSet() {
        return myNameSet;
    }

    public boolean isColourSet() {
        return myColourSet;
    }
}