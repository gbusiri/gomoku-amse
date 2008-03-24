package ru.amse.gomoku.logic.player;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 03.03.2008
 * Time: 14:55:38
 * To change this template use File | Settings | File Templates.
 */
class Border {
    int myEast;
    int myWest;
    int myNorth;
    int mySouth;
    final int MY_MAX;

    Border(int max, int north, int south, int east, int west) {
        MY_MAX = max;
        myEast = east;
        myWest = west;
        myNorth = north;
        mySouth = south;
        makeZero();
        makeMax();
    }

    void setBorder(int north, int south, int east, int west) {
        if (east < myEast) {
            myEast = east;
        }
        if (west > myWest ) {
            myWest = west;
        }
        if (north < myNorth) {
            myNorth = north;
        }
        if (south > mySouth) {
            mySouth = south;
        }
        makeZero();
        makeMax();
    }

    void makeZero() {
        if (myEast < 0) {
            myEast = 0;
        }
        if (myNorth < 0) {
            myNorth = 0;
        }
    }

    void makeMax() {
        if (myWest > MY_MAX) {
            myWest = MY_MAX;
        }
        if (mySouth > MY_MAX) {
            mySouth = MY_MAX;
        }
    }
}
