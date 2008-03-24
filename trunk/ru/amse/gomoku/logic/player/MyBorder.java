package ru.amse.gomoku.logic.player;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 14.03.2008
 * Time: 18:23:19
 * To change this template use File | Settings | File Templates.
 */
public class MyBorder {

    int[][] possibleTurns;
    final int MY_RANGE;
    final int MY_LIMIT;
    int myPossibilities;
    boolean myBegin = false;
    boolean needsReseting;

    int myCurrentLevel;
    int[] myCurrentPossibility;

    MyBorder(int range, int limit) {
        possibleTurns = new int[limit][limit];
        MY_RANGE = range;
        MY_LIMIT = limit;
        addCoordinates(new int[] {MY_LIMIT / 2, MY_LIMIT / 2});
    }

    void addCoordinates(int[] coordinates) {
        int i = coordinates[0] - MY_RANGE;
        int j = coordinates[1] - MY_RANGE;
        int maxI = coordinates[0] + MY_RANGE;
        int maxJ = coordinates[1] + MY_RANGE;

        if (coordinates[0] - MY_RANGE < 0) {
            i = 0;
        } else if (coordinates[0] + MY_RANGE > MY_LIMIT) {
            maxI = MY_LIMIT;
        }
        if (coordinates[1] - MY_RANGE < 0) {
            j = 0;
        } else if (coordinates[1] + MY_RANGE > MY_LIMIT) {
            maxJ = MY_LIMIT;
        }
        for ( ; i < maxI; i++) {
            for (int l = j ; l < maxJ; l++) {
                if (possibleTurns[i][l] == 0) {
                    possibleTurns[i][l] = 1;
                    myPossibilities++;
                }
            }
        }
        if (myBegin) {
            myPossibilities--;
            possibleTurns[coordinates[0]][coordinates[1]] = 2;
        }
        myBegin = true;
        needsReseting = true;
    }

    void setCoordinates(int height, int width, int level) {
                    
    }
}
