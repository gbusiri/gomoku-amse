package ru.amse.gomoku.logic.player;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 24.02.2008
 * Time: 15:19:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class Player implements IPlayer {

    protected final String myName;
    protected final boolean myPlayerIsComputer;
    protected final int myColour;

    public Player(String name, boolean isComp, int colour) {
        myName = name;
        myPlayerIsComputer = isComp;
        if (colour % 2 == 0) {
            myColour = 2;
        } else {
            myColour = 1;
        }
    }

    public String getName() {
        return myName;
    }

    public int getColour() {
        return myColour;
    }

    public boolean isComputer() {
        return myPlayerIsComputer;
    }
}