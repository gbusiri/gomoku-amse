package ru.amse.gomoku.players;

import ru.amse.gomoku.board.IBoard;

import javax.swing.*;

/**
 * 
 */
public abstract class Player implements IPlayer {

    protected String myName;
    protected byte myColour;
    protected ImageIcon myImage;

    public Player(String name, byte colour, ImageIcon image) {
        myName = name;
        myImage = image;
        setColour(colour);
    }

    public Player(String name, byte colour) {
        this(name, colour, null);
    }
    
    public abstract void makeNextTurn(byte[][] board, byte[] coordinates);

    public abstract byte[] giveNextTurn();

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        myName = name;
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
    }

    public ImageIcon getImage() {
        return myImage;  
    }
}