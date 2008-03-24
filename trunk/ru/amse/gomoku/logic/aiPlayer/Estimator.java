package ru.amse.gomoku.logic.aiPlayer;

import ru.amse.gomoku.logic.board.Board;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 10.03.2008
 * Time: 21:29:43
 * To change this template use File | Settings | File Templates.
 */
class Estimator {

    private int[][] myBoard;

    Estimator(int[][] board) {
        myBoard = board;
    }

    int estimate(int height, int width, int colour, int[][] board) {
        myBoard = board;
        int valueEstimated;
        int oppositeColour = colour % 2 + 1;
        valueEstimated = estimationForGivenColour(height, width, colour);
        if (valueEstimated != Looker.MY_MAX) {
            valueEstimated
             += estimationForGivenColour(height, width, oppositeColour);
        }
        return valueEstimated;
    }

    int estimationForGivenColour(int height, int width, int colour) {
        boolean estimationNeeded = false;
        int valueEstimated = 0;

        int[] first = directionCheck(height, width, 1, 1, colour) ;
        if (maxReached(first)) {
            return (Looker.MY_MAX / 2) * 3;
        }
        int[] second = directionCheck(height, width, 1, 0, colour);
        if (maxReached(second)) {
            return (Looker.MY_MAX / 2) * 3;
        }
        int[] third = directionCheck(height, width, 0, 1, colour);
        if (maxReached(third)) {
            return (Looker.MY_MAX / 2) * 3;
        }
        int[] forth = directionCheck(height, width, 1, -1, colour);
        if (maxReached(forth)) {
            return (Looker.MY_MAX / 2) * 3;
        }
        if ((first[0] > 0) || (second[0] > 0) || (third[0] > 0) || (forth[0] > 0)) {
            estimationNeeded = true;
        }        
        if (estimationNeeded) {
            valueEstimated += estimatingHelper(new int[][] {first, second, third, forth});
        }
        return valueEstimated;
    }

    private boolean maxReached(int[] checking) {
        return checking[1] >= Board.MY_WINNING_SIZE;
    }

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
                    return Looker.MY_MAX;
                } else if ((data[i][1] == Board.MY_WINNING_SIZE - 1)
                          && (!exists)) {
                    estimated += Looker.MY_MAX / 6;
                    exists = true;
                } else if (data[i][1] == Board.MY_WINNING_SIZE - 1) {
                    return Looker.MY_MAX;
                } else if ((data[i][1] == Board.MY_WINNING_SIZE - 2)
                          && (data[i][2] > 0)
                          && (data[i][3] > 0)
                          && (!cross)) {
                    estimated += Looker.MY_MAX / 12;
                    cross = true;
                } else if ((data[i][1] == Board.MY_WINNING_SIZE - 2)
                          && (data[i][2] > 0)
                          && (data[i][3] > 0)) {
                    return Looker.MY_MAX;
                } else if (((data[i][1] == Board.MY_WINNING_SIZE - 3)
                          && (!twins))) {
                    estimated += Looker.MY_MAX / 100;
                    twins = true;
                } else if (data[i][1] == Board.MY_WINNING_SIZE - 3) {
                    estimated += Looker.MY_MAX / 30;
                }
                if (data[i][0] > Board.MY_WINNING_SIZE) {
                    estimated += 5000 * data[i][0];

                }
            }
        }
        /*
        give bonus for 6 empty
         */
        return estimated;
    }

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
                  && coordinateAcceptance(height + heightChange, width + widthChange)
                  && (checkColour(0, height + heightChange, width + widthChange)
                     || (checkColour(colour, height + heightChange, width + widthChange))))) {
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
        return !((height < 0) || (height >= myBoard.length)
                || (width < 0) || (width >= myBoard.length));
    }

    private boolean checkColour(int colour
                                , int height2
                                , int width2) {
        return colour == myBoard[height2][width2];
    }

    private void print() {
        for (int[] aMyBoard : myBoard) {
            for (int l = 0; l < myBoard.length; l++) {
                System.out.print(aMyBoard[l] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}
