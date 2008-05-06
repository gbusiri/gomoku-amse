package ru.amse.gomoku.players.impl.aiPlayer;

import ru.amse.gomoku.board.IBoard;

/**
 *
 */
class Estimator {

    private byte[][] myBoard;

    int estimate(byte height, byte width, byte colour, byte[][] board) {
        myBoard = board;
        int valueEstimated;
        byte oppositeColour = (byte)(colour % 2 + 1);

        valueEstimated = estimationForGivenColour(height, width, colour);

        // < is good for starting is worse for other cases.
        if (valueEstimated < (Looker.MY_MAX / 2) * 3) {
            valueEstimated
             += (estimationForGivenColour(height, width, oppositeColour) / 9) * 8;
        }
        return valueEstimated;
    }

    int estimationForGivenColour(byte height, byte width, byte colour) {
        int valueEstimated = 0;

        byte[] first = directionCheck(height, width, (byte)1,(byte)1, colour) ;
        if (maxReached(first)) {
            return (Looker.MY_MAX / 2) * 3;
        }
        byte[] second = directionCheck(height, width, (byte)1,(byte) 0, colour);
        if (maxReached(second)) {
            return (Looker.MY_MAX / 2) * 3;
        }
        byte[] third = directionCheck(height, width, (byte)0,(byte) 1, colour);
        if (maxReached(third)) {
            return (Looker.MY_MAX / 2) * 3;
        }
        byte[] forth = directionCheck(height, width, (byte)1,(byte) -1, colour);
        if (maxReached(forth)) {
            return (Looker.MY_MAX / 2) * 3;
        }
        if ((first[0] > 0) || (second[0] > 0) || (third[0] > 0) || (forth[0] > 0)) {
            valueEstimated += estimatingHelper(new byte[][] {first, second, third, forth});
        }
        return valueEstimated;
    }

    private boolean maxReached(byte[] checking) {
        return checking[1] >= IBoard.MY_WINNING_SIZE;
    }

    int estimatingHelper(byte[][] data) {
        int estimated = 0;
        boolean exists = false;
        boolean cross = false;
        boolean twins = false;

        // 4 - possible directions for search.
        for (int i = 0; i < 4; i++) {
            if (data[i][0] > 0) {
                switch(data[i][1]) {
                    case(IBoard.MY_WINNING_SIZE - 1):
                        if ((data[i][2] > 0)
                           && (data[i][3] > 0)) {
                            return (Looker.MY_MAX / 3) * 4;
                        } else if (!exists) {
                            estimated += Looker.MY_MAX / 6;
                            exists = true;
                        } else {
                            return Looker.MY_MAX;
                        }
                        break;
                    case(IBoard.MY_WINNING_SIZE - 2):
                        if ((data[i][2] > 0)
                           && (data[i][3] > 0)
                           && (!cross)) {
                            estimated += Looker.MY_MAX / 12;
                            cross = true;
                        } else if ((data[i][2] > 0)
                                  && (data[i][3] > 0)
                                  && (data[i][4] > IBoard.MY_WINNING_SIZE)) {
                            return (Looker.MY_MAX / 4) * 3;
                        } else {
                            estimated += Looker.MY_MAX / 25;
                        }
                        break;
                    case(IBoard.MY_WINNING_SIZE - 3):
                        if (!twins) {
                            estimated += Looker.MY_MAX / 100;
                            twins = true;
                        } else {
                            estimated += Looker.MY_MAX / 30;
                        }
                        break;
                    default:
                        estimated += Looker.MY_MAX / 2000;
                        break;
                }
            }
            if (data[i][4] > IBoard.MY_WINNING_SIZE) {
                estimated += Looker.MY_MAX / 1000 * data[i][0];
            }
        }
        return estimated;
    }

    byte[] directionCheck(byte height
                        , byte width
                        , byte heightDirection
                        , byte widthDirection
                        , byte colour) {
        byte[] estimating = new byte[5];
        byte[] neededFirst = search(height
                                  , width
                                  , heightDirection
                                  , widthDirection
                                  , colour
                                  , (byte)1
                                  , (byte)1
                                  , true);
        byte[] neededSecond = search(height
                                   , width
                                   , (byte)- heightDirection
                                   , (byte)- widthDirection
                                   , colour
                                   , (byte)0
                                   , (byte) 0
                                   , true);
        if (neededFirst[1] + neededSecond[1] >= 5) {
            estimating[0] = 1;
        } else {
            estimating[0] = 0;
        }
        estimating[1] = (byte)(neededFirst[0] + neededSecond[0]);
        estimating[2] = (byte)(neededFirst[1] - neededFirst[0]);
        estimating[3] = (byte)(neededSecond[1] - neededSecond[0]);
        estimating[4] = (byte)(neededFirst[1] + neededSecond[1]);
        return estimating;
    }

    byte[] search(byte height
                , byte width
                , byte heightChange
                , byte widthChange
                , byte colour
                , byte count
                , byte empty
                , boolean checking) {
        if (count < IBoard.MY_WINNING_SIZE
           && checking
           && coordinateAcceptance((byte)(height + heightChange)
                                   , (byte)(width + widthChange))
           && checkColour(colour, height + heightChange, width + widthChange)) {
            return search((byte)(height + heightChange)
                         ,(byte)(width + widthChange)
                         , heightChange
                         , widthChange
                         , colour
                         , ++count, ++empty, checking);
        } else if (((count < IBoard.MY_WINNING_SIZE)
                  && coordinateAcceptance((byte)(height + heightChange)
                                        , (byte)(width + widthChange))
                  && (checkColour(0, height + heightChange, width + widthChange)
                     || (checkColour(colour, height + heightChange, width + widthChange))))) {
            return search((byte)(height + heightChange)
                         , (byte)(width + widthChange)
                         , heightChange
                         , widthChange
                         , colour
                         , count
                         , ++empty
                         , false);
        } else {
            return new byte[] {count, empty};
        }
    }


    private boolean coordinateAcceptance(byte height, byte width) {
        return !((height < 0) || (height >= myBoard.length)
                || (width < 0) || (width >= myBoard.length));
    }

    private boolean checkColour(int colour
                                , int height2
                                , int width2) {
        return colour == myBoard[height2][width2];
    }

    private void print() {
        for (byte[] aMyBoard : myBoard) {
            for (int l = 0; l < myBoard.length; l++) {
                System.out.print(aMyBoard[l] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }
}
