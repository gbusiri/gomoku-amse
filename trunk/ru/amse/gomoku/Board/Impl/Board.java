package ru.amse.gomoku.Board.Impl;

import ru.amse.gomoku.Board.IBoard;
import java.util.Stack;

/**
 * model of the game.
 * it is a board where the dibs are placed and presence of a win is analized. 
 */
public class Board implements IBoard {

    public static final byte EMPTY_TURN = 0;

    private final byte[][] myBoard;
    private final Stack<byte[]> myTurns;
    private int myNumberOfDibs;
    private byte myHeightOfLastAdded;
    private byte myWidthOfLastAdded;

    public Board() {
		myBoard = new byte[MY_BOARD_SIZE][MY_BOARD_SIZE];
        myTurns = new Stack<byte[]>();
        myNumberOfDibs = 0;
        refreshBoard();
    }

    /**
     * adds dib with checking dibPresent.
     *
     * @param height - vertical coordinate of the dib being added.
     * @param width - horizontal coordinate of the dib being added.
     * @param colour - colour of the dib to add.
     */
    public boolean addDib(byte height, byte width, byte colour) {

        if ((isPossibleMove(height, width))
           && ((colour == DIB_COLOUR_FIRST) || (colour == DIB_COLOUR_SECOND))) {
			myBoard[height][width] = colour;
			myNumberOfDibs++;
            myHeightOfLastAdded = height;
            myWidthOfLastAdded = width;
            myTurns.push(new byte[] {myHeightOfLastAdded, myWidthOfLastAdded});
            return true;
        }
        return false;
    }

    public byte getDibColour(byte height, byte width) {
		return myBoard[height][width];
    }

    public int getNumberOfDibs() {
        return myNumberOfDibs;
    }

    public boolean isWin() {
        return (checkDirection((byte) 1, (byte) 1)
               || checkDirection((byte) 1, (byte) 0)
               || checkDirection((byte) 0, (byte) 1)
               || checkDirection((byte) 1, (byte) -1));
    }

    public byte[][] getCurrentBoard() {
        return myBoard.clone();
    }

    public boolean isPossibleTurnPresent() {
        for (int i = 0; i < MY_BOARD_SIZE; i++) {
            for (int j = 0; j < MY_BOARD_SIZE; j++) {
                if (myBoard[i][j] == EMPTY_TURN) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPossibleMove(byte height, byte width) {
        return (isCoordinateAcceptance(height, width)
                && !(isDibPresent(height, width)));
    }

    public void refreshBoard() {
        for (int i = 0; i < MY_BOARD_SIZE; i++) {
            for (int j = 0; j < MY_BOARD_SIZE; j++) {
                myBoard[i][j] = EMPTY_TURN;
            }
        }
    }

    public boolean undoLastTurn() {
        if (isUndoPossible()) {
            byte[] lastTurn = myTurns.pop();
            myBoard[lastTurn[0]][lastTurn[1]] = EMPTY_TURN;
            myNumberOfDibs--;
            return true;
        }
        return false;
    }

    public boolean isUndoPossible() {
        return myNumberOfDibs > 0;
    }

    private boolean checkDirection(byte vDirection, byte hDirection) {
        return (search(myHeightOfLastAdded
                     , myWidthOfLastAdded
                     , vDirection
                     , hDirection
                     , search(myHeightOfLastAdded
                             , myWidthOfLastAdded
                             , (byte)(-vDirection)
                             , (byte)(-hDirection)
                             , (byte) 1))
               == MY_WINNING_SIZE);
    }

    private byte search(byte height
                      , byte width
                      , byte heightChange
                      , byte widthChange
                      , byte count) {
        if ((count < MY_WINNING_SIZE)
           && isDibPresent((byte)(height + heightChange), (byte)(width + widthChange))
           && (checkColours(height
                           , width
                           , (byte)(height + heightChange)
                           , (byte)(width + widthChange)))) {
            return search((byte)(height + heightChange)
                         , (byte)(width + widthChange)
                         , heightChange
                         , widthChange
                         , ++count);
        } else {
            return count;
        }
    }

    private boolean checkColours(byte height1, byte width1, byte height2, byte width2) {
        return (myBoard[height1][width1] == myBoard[height2][width2]);
    }

    private boolean isDibPresent(byte height, byte width) {
        return (isCoordinateAcceptance(height, width)
                && (myBoard[height][width] != EMPTY_TURN));
    }

    private boolean isCoordinateAcceptance(byte height, byte width) {
        return !((height < 0) || (height >= MY_BOARD_SIZE)
                || (width < 0) || (width >= MY_BOARD_SIZE));
    }
}