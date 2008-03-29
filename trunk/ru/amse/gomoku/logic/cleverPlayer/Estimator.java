package ru.amse.gomoku.logic.cleverPlayer;

import ru.amse.gomoku.logic.board.Board;

/**
 * gives estimation for the given coordinates. 
 */
class Estimator {

    private int[][] myBoard;
    private int myColour;
    private int myOpponentsColur;
    private int myValueEstimated;
    private int[] myFirstDirection;
    private int[] mySecondDirection;
    private int[] myThirdDirection;
    private int[] myForthDirection;

    boolean printNeeded = false;

    int estimate(int height, int width, int colour, int[][] board) {
        myBoard = board;

        myColour = colour;
        myOpponentsColur = myColour % 2 + 1;
        
        myValueEstimated = estimationForGivenColour(height, width, myColour);

        // to be deleted...................
        if (printNeeded) {
            System.out.println(myValueEstimated + " for my colour");
        }

        if (myValueEstimated < Looker.MY_MAX) {
            myValueEstimated
             += (estimationForGivenColour(height, width, myOpponentsColur) / 5) * 4;
        }

        // to be deleted...................
        if (printNeeded) {
            System.out.println(myValueEstimated + " for both colours");
        }

        return myValueEstimated;
    }

    int estimationForGivenColour(int height, int width, int colour) {
        int valueEstimated = 0;

        if (isWinPresent(height, width, colour)) {
            if (colour == myColour) {
                return (Looker.MY_MAX / 2) * 3;
            } else {
                return Looker.MY_MAX / 2;
            }
        }
        if ((myFirstDirection[0] > 0)
           || (mySecondDirection[0] > 0)
           || (myThirdDirection[0] > 0)
           || (myForthDirection[0] > 0)) {
            valueEstimated += estimatingHelper(new int[][] {myFirstDirection
                                                           , mySecondDirection
                                                           , myThirdDirection
                                                           , myForthDirection});
        }
        return valueEstimated;
    }

    /**
     * checks if win is present.
     *
     * @param height - vertical coordinate of the starting position.
     * @param width - horizontal coordinate of the starting position.
     * @param colour - colour of the player.
     * @return true if win is present.
     */
    private boolean isWinPresent(int height, int width, int colour) {
        myFirstDirection = directionCheck(height, width, 1, 1, colour);
        if (maxReached(myFirstDirection[1])) {
            return true;
        }
        mySecondDirection = directionCheck(height, width, 1, 0, colour);
        if (maxReached(mySecondDirection[1])) {
            return true;
        }
        myThirdDirection = directionCheck(height, width, 0, 1, colour);
        if (maxReached(myThirdDirection[1])) {
            return true;
        }
        myForthDirection = directionCheck(height, width, 1, -1, colour);
        return maxReached(myForthDirection[1]);
    }

    private boolean maxReached(int checking) {
        return checking >= Board.MY_WINNING_SIZE;
    }

    /**
     * if the win is not present then we estimate other cases.
     *
     * @param data - is the result from direction check.
     * @return estimation using given data.
     */
    int estimatingHelper(int[][] data) {
        int estimated = 0;
        boolean exists = false;
        boolean cross = false;
        boolean twins = false;

        for (int i = 0; i < 4; i++) {

            if (data[i][0] >= 0) {
                if ((data[i][1] == Board.MY_WINNING_SIZE - 1)
                   && (data[i][2] > 0)
                   && (data[i][3] > 0)) {
                    return Looker.MY_MAX / 5;
                } else if ((data[i][1] == Board.MY_WINNING_SIZE - 1)
                          && (!exists)) {
                    estimated += Looker.MY_MAX / 10;
                    exists = true;
                } else if (data[i][1] == Board.MY_WINNING_SIZE - 1) {
                    return Looker.MY_MAX / 5;
                } else if ((data[i][1] == Board.MY_WINNING_SIZE - 2)
                          && (data[i][2] > 0)
                          && (data[i][3] > 0)
                          && (!cross)) {
                    estimated += Looker.MY_MAX / 25;
                    cross = true;
                } else if ((data[i][1] == Board.MY_WINNING_SIZE - 2)
                          && (data[i][2] > 0)
                          && (data[i][3] > 0)) {
                    return Looker.MY_MAX / 6;
                } else if ((data[i][1] == Board.MY_WINNING_SIZE - 2)) {
                    estimated += Looker.MY_MAX / 100;
                } else if (((data[i][1] == Board.MY_WINNING_SIZE - 3)
                          && (!twins))) {
                    estimated += Looker.MY_MAX / 100;
                    twins = true;
                } else if (data[i][1] == Board.MY_WINNING_SIZE - 3) {
                    estimated += Looker.MY_MAX / 30;
                }
                
                // giving bonus to center.
                if (data[i][4] > Board.MY_WINNING_SIZE) {
                    estimated += (Looker.MY_MAX / 300) * data[i][0];
                }
            }
        }
        return estimated;
    }

    /**
     * checks the given direction for the given colour from a given position.
     *
     * @param height - vertical coordinate of the starting position.
     * @param width - horizontal coordinate of the starting position.
     * @param heightDirection - vertical direction to look.
     * @param widthDirection - horizontal direction to look.
     * @param colour - colour to look for.
     * @return - array of size 5.
     *          at index 0 - may be 0 if no 5 in succession are not possible,
     *                       1 - otherwise.
     *          at index 1 - total number of dibs of the given colour being in
     *                       succession including starting.
     *          at index 2 - number of empty cells after the last dib of the
     *                       given colour in the given direction.
     *          at index 3 - number of empty cells after the last dib of the
     *                       given colour in the opposite of the given direction.
     *          at index 4 - total number of cells without opponents dibs in the
     *                       given direction.
     */
    int[] directionCheck(int height
                        , int width
                        , int heightDirection
                        , int widthDirection
                        , int colour) {
        int[] estimating = new int[5];
        int[] neededFirst = search(height
                                  , width
                                  , heightDirection
                                  , widthDirection
                                  , colour
                                  , 1
                                  , 1
                                  , true);
        int[] neededSecond = search(height
                                   , width
                                   , - heightDirection
                                   , - widthDirection
                                   , colour
                                   , 0
                                   , 0
                                   , true);
        if (neededFirst[1] + neededSecond[1] >= 5) {
            estimating[0] = 1;
        } else {
            estimating[0] = 0;
        }
        estimating[1] = neededFirst[0] + neededSecond[0];
        estimating[2] = neededFirst[1] - neededFirst[0];
        estimating[3] = neededSecond[1] - neededSecond[0];
        estimating[4] = neededFirst[1] + neededSecond[1];
        return estimating;
    }

    int[] search(int height
                , int width
                , int heightChange
                , int widthChange
                , int colour
                , int count
                , int empty
                , boolean checking) {
        if (count < Board.MY_WINNING_SIZE
           && checking
           && coordinateAcceptance(height + heightChange, width + widthChange)
           && checkColour(colour, height + heightChange, width + widthChange)) {
            return search(height + heightChange
                         , width + widthChange
                         , heightChange
                         , widthChange
                         , colour
                         , ++count, ++empty, checking);
        } else if (((count < Board.MY_WINNING_SIZE)
                  && coordinateAcceptance(height + heightChange
                                         , width + widthChange)
                  && (checkColour(0, height + heightChange, width + widthChange)
                     || (checkColour(colour
                                    , height + heightChange
                                    , width + widthChange))))) {
            return search(height + heightChange
                         , width + widthChange
                         , heightChange
                         , widthChange
                         , colour
                         , count
                         , ++empty
                         , false);
        } else {
            return new int[] {count, empty};
        }
    }


    private boolean coordinateAcceptance(int height, int width) {
        
        return !((height < 0)
                || (height >= myBoard.length)
                || (width < 0)
                || (width >= myBoard.length));
    }

    private boolean checkColour(int colour
                                , int height2
                                , int width2) {
        return colour == myBoard[height2][width2];
    }

    //to be deleted................
    void print() {
        for (int[] aMyBoard : myBoard) {
            for (int l = 0; l < myBoard.length; l++) {
                System.out.print(aMyBoard[l] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}