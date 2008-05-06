package ru.amse.gomoku.board.impl;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.board.IListener;

import java.util.LinkedList;
import java.util.Stack;

/**
 * model of the game.
 * it is a board where the dibs are placed and presence of a win is analized. 
 */
public class Board implements IBoard {

    private LinkedList <IListener> myListeners;

    private final byte[][] myBoard;
    private final Stack <byte[]> myTurns;

    private int myNumberOfDibs;
    private byte myHeightOfLastAdded;
    private byte myWidthOfLastAdded;

    public Board() {
		myBoard = new byte[MY_BOARD_SIZE][MY_BOARD_SIZE];
        myTurns = new Stack <byte[]>();
        myListeners = new LinkedList <IListener>();
        myNumberOfDibs = 0;
        refreshBoard();
    }

    public void registerListener(IListener listener) {
        myListeners.add(listener);
    }

    public void unRegisterListener(IListener listener) {
        myListeners.remove(listener);
    }

    public void fireEvent(int event, Object description) {
        for (IListener listener : myListeners) {
            listener.actionPerformed(event, description);
        }
    }

    /**
     * adds dib with checking dibPresent.
     *
     * @param height - vertical coordinate of the dib being added.
     * @param width - horizontal coordinate of the dib being added.
     * @param colour - colour of the dib to add.
     */
    public boolean addDib(byte height, byte width, byte colour) {
        byte[] toAdd = new byte[] {height, width};

        if ((isPossibleMove(height, width))
           && ((colour == DIB_COLOUR_FIRST) || (colour == DIB_COLOUR_SECOND))) {
			myBoard[height][width] = colour;
			myNumberOfDibs++;
            myHeightOfLastAdded = height;
            myWidthOfLastAdded = width;
            myTurns.push(toAdd);
            fireEvent(IBoard.ADD_PERFORMED, toAdd);
            return true;
        }
        fireEvent(IBoard.INVALID_MOVE, toAdd);
        return false;
    }

    public boolean isWin() {
        if (checkDirection((byte) 1, (byte) 1)
           || checkDirection((byte) 1, (byte) 0)
           || checkDirection((byte) 0, (byte) 1)
           || checkDirection((byte) 1, (byte) -1)) {
            fireEvent(IBoard.WIN_GOT, null);

            /**System.out.println("started");
            for (int i = 0; i < MY_BOARD_SIZE; i++) {
            for (int j = 0; j < MY_BOARD_SIZE; j++) {
                System.out.printf("%2d", myBoard[i][j] );
            }
            System.out.println();
            }
            System.out.println("finished");
             */
            return true;
        }
        return false;
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
        myNumberOfDibs = 0;
        fireEvent(IBoard.REFRESH_PERFORMED, null);
    }

    public boolean undoLastTurn() {
        if (isUndoPossible()) {
            byte[] lastTurn = myTurns.pop();
            myBoard[lastTurn[0]][lastTurn[1]] = EMPTY_TURN;
            myNumberOfDibs--;
            fireEvent(IBoard.UNDO_PERFORMED, null);
            return true;
        }
        return false;
    }

    public boolean isUndoPossible() {
        return myNumberOfDibs > 0;
    }

    public int getDibColour(byte i, byte j) {
        return myBoard[i][j];
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