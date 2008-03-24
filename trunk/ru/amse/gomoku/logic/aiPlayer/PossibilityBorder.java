package ru.amse.gomoku.logic.aiPlayer;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 06.03.2008
 * Time: 20:02:22
 * To change this template use File | Settings | File Templates.
 */
class PossibilityBorder {

    int[][] possibleTurns;
    int[][][] myLevelControl;
    final int MY_RANGE;
    final int MY_LIMIT;
    final int MY_MAX_LEVELS;
    int myPossibilities;
    boolean myBegin;
    boolean needsReseting;
    int[] myCurrentPossibility;
    int myCurrentLevel;


    PossibilityBorder(int range, int limit, int levels) {
        possibleTurns = new int[limit][limit];
        MY_RANGE = range;
        MY_LIMIT = limit;
        MY_MAX_LEVELS = levels;
        myBegin = false;
        myLevelControl = new int[levels + 1][limit][limit];
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
        if ((myBegin) && (possibleTurns[coordinates[0]][coordinates[1]] == 1)) {
            myPossibilities--;
            possibleTurns[coordinates[0]][coordinates[1]] = 2;
        }
        /*
        print(possibleTurns);
           */
        myCurrentPossibility = new int[] {0, -1};
        myBegin = true;
        needsReseting = true;
    }

    int[] getCoordinates(int level) {
        System.out.println("   current possibility is " + myCurrentPossibility[0] + ", " + myCurrentPossibility[1]);
        if ((myCurrentLevel > level) || needsReseting) {
            System.out.println(" i am reseting now cause new level is "+ level + " and old level is " + myCurrentLevel);
            reseting(level);
        } else if (myCurrentLevel < level) {
            resetCurrentPossibility(level);
        }
        myCurrentLevel = level;
        int[] needed = getNextPossibility(level);
        myLevelControl[level][needed[0]][needed[1]] = 1;
        return needed;
    }

    void resetCurrentPossibility(int level) {
        int height = 0;
        int width = -1;
        boolean check = true;
        do {
            if (width + 1 < MY_LIMIT) {
                width++;
            } else {
                height++;
                width = 0;
            }
            if (checkUpperLevels(height, width, level)) {
                myCurrentPossibility = makePreviousIndex(new int[] {height, width});
                check = false;
            }
        } while (check);
    }

    boolean checkUpperLevels(int height, int width, int level) {
        boolean needed = true;
        int k = level;
        do {
            k--;
            if ((myLevelControl[k][height][width]) != 0) {
                needed = false;
            }
        } while (k > 0);
        return needed;
    }

    int[] getNextPossibility(int level) {
        do {
           System.out.println("  inside       " +
                    " current possibility is " + myCurrentPossibility[0] + ", " + myCurrentPossibility[1] +
                    " and level is " + level);
            /*
            print(myLevelControl[level]);      */
            if ((myCurrentPossibility[1] + 1) < MY_LIMIT) {
               myCurrentPossibility[1]++;
            } else {
                myCurrentPossibility[0]++;
                myCurrentPossibility[1] = 0;
            }
            /**
             * add if here to break if increased..
             */
        } while ((myLevelControl[level][myCurrentPossibility[0]][myCurrentPossibility[1]] != 0)
                || (possibleTurns[myCurrentPossibility[0]][myCurrentPossibility[1]] != 1));
        return myCurrentPossibility;
    }

    void reseting(int level) {
        int levels = MY_MAX_LEVELS;
        while (level < levels) {
            reset(0, myLevelControl[levels]);
            levels--;
        }
        if ((level == 0) && needsReseting) {
            System.out.println("i am here in if now");
            reset(0, myLevelControl[level]);
            needsReseting = false;                                       
        }
    }

    void reset(int resetingValue, int[][] possibilities) {
        int count = 0;
        
        for (int k = 0; k < MY_LIMIT; k++) {
            for (int l = 0; l < MY_LIMIT; l++) {
                if (possibilities[k][l] != resetingValue) {
                    possibilities[k][l] = 0;
                    if (count == 0) {
                        myCurrentPossibility = makePreviousIndex(new int[] {k, l});
                        count++;
                    }
                }
            }
        }
    }

    int[] makePreviousIndex(int[] current) {
        int[] previous = new int[2];
        if (current[1] != 0) {
            previous = new int[] {current[0], current[1] - 1};
        } else if (current[0] != 0) {
            previous = new int[] {current[0] - 1, MY_LIMIT - 1};
        }
        return previous;
    }

    void print(int [][] possibilities) {
        for (int k = 0; k < MY_LIMIT; k++) {
            for (int l = 0; l < MY_LIMIT; l++) {
                System.out.print(possibilities[k][l] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}
