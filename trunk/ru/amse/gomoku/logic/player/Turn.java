package ru.amse.gomoku.logic.player;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 28.02.2008
 * Time: 14:43:14
 * To change this template use File | Settings | File Templates.
 */
class Turn {

    private int myNumberOfPossibleTurns;
    private int myHeight;
    private int myWidth;

    Turn (int height, int width) {
        myHeight = height;
        myWidth = width;
    }

    int getNumberOfTurns() {
        return myNumberOfPossibleTurns;
    }

    int getHeight() {
        return myHeight;
    }

    int getWidth() {
        return myWidth;
    }
}
