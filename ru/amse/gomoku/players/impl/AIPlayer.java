package ru.amse.gomoku.players.impl;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.players.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class AIPlayer extends Player {

    public static final int MY_MAX = 500000;
    public static final boolean ALLOW_RANDOMIZING = true;

    private byte[][] myBoard;
    private byte[] myTurn;

    public AIPlayer(String name, byte colour, ImageIcon icon) {
        super(name, colour, icon);
    }

    public AIPlayer(String name, byte colour) {
        this(name, colour, null);
    }

    public AIPlayer() {
        super();
    }

    public void makeNextTurn(byte[][] board, byte[] coordinates) {
        myBoard = board;
        if (coordinates == null) {
            myTurn = new byte[]{IBoard.MY_BOARD_SIZE / 2, IBoard.MY_BOARD_SIZE / 2};
        } else {
            myTurn = searchTurns();
        }
    }

    public byte[] giveNextTurn() {
        return myTurn;
    }

    private byte[] searchTurns() {
        ArrayList<byte[]> list = getPossibleTurns();
        int size = list.size();

        Random r = new Random();
        byte height;
        byte width;
        byte heightNeeded = -1;
        byte widthNeeded = -1;

        int maxEstimated = -1;
        for (int i = 0; i < size; i++ ) {
            height = list.get(i)[0];
            width = list.get(i)[1];
            int estimated = estimatingFunction(height, width, myColour);
            if ((estimated > maxEstimated)
               || ((estimated == maxEstimated)
                  && (ALLOW_RANDOMIZING && r.nextBoolean()))) {
                maxEstimated = estimated;
                heightNeeded = height;
                widthNeeded = width;
            }
        }
        return new byte[]{heightNeeded, widthNeeded};
    }

    private int estimatingFunction(int height, int width, int colour) {
        int needed = checkDirection(1, 1, height, width, colour);
        needed += checkDirection(1, 0, height, width, colour);
        needed += checkDirection(0, 1, height, width, colour);
        needed += checkDirection(-1, 1, height, width, colour);
        return needed;
    }

    private int checkDirection(int vDirection
                              , int hDirection
                              , int height
                              , int width
                              , int colour) {
        int[] count= search(height, width, -vDirection, -hDirection, colour, 1, 1, true);
        int[] empty = search(height, width, vDirection, hDirection, colour, count[0], count[1], true);
        count = search(height, width, -vDirection, -hDirection, colour % 2 + 1, 0, 1, true);
        count = search(height, width, vDirection, hDirection, colour % 2 + 1, count[0], count[1], true);
        if ((empty[1] + count[1] - 1) < IBoard.MY_WINNING_SIZE) {
            return 0;
        }
        int additional = 0;
        if ((colour == myColour) && (empty[1] + count[1] >= IBoard.MY_WINNING_SIZE)) {
            additional = (empty[1] + count[1]) * MY_MAX / 1000;
        }
        return getValue(empty[0], true) + getValue(count[0], false) + additional;
    }

    private int getValue(int count, boolean check) {
        int needed;

        switch(count) {
            case(IBoard.MY_WINNING_SIZE - 5) :
                needed = 2;
                break;
            case(IBoard.MY_WINNING_SIZE - 4) :
                needed = MY_MAX / 20;
                break;
            case(IBoard.MY_WINNING_SIZE - 3) :
                needed = MY_MAX / 12;
                break;
            case(IBoard.MY_WINNING_SIZE - 2) :
                if (check) {
                    needed = MY_MAX / 5;
                } else {
                    needed = MY_MAX / 2 - MY_MAX / 10;
                }
                break;
            case(IBoard.MY_WINNING_SIZE - 1):
                if (check) {
                    needed = MY_MAX / 2;
                } else {
                    needed = getValue(5, true) - MY_MAX / 1000;
                }
                break;
            default :
                needed = MY_MAX;
                break;
        }
        return needed;
    }

    private int[] search(int height
                      , int width
                      , int heightChange
                      , int widthChange
                      , int colour
                      , int count
                      , int empty
                      , boolean checking) {
        if (count < IBoard.MY_WINNING_SIZE
           && checking
           && coordinateAcceptance(height + heightChange, width + widthChange)
           && checkColour(colour, height + heightChange, width + widthChange)) {
            return search(height + heightChange
                             , width + widthChange
                             , heightChange
                             , widthChange
                             , colour
                             , ++count, ++empty, checking);
        } else if ((count < IBoard.MY_WINNING_SIZE)
                  && coordinateAcceptance(height + heightChange, width + widthChange)
                  && checkColour(0, height + heightChange, width + widthChange)) {
            return search(height + heightChange
                             , width + widthChange
                             , heightChange
                             , widthChange
                             , colour
                             , count, ++empty, false);
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

    private ArrayList<byte[]> getPossibleTurns() {
        ArrayList<byte[]> turns = new ArrayList<byte[]>();
        int length = myBoard.length;

        for (byte i = 0; i < length; i++) {
            for (byte j = 0; j < length; j++) {
                if (myBoard[i][j] == 0) {
                    turns.add(new byte[]{i, j});
                }
            }
        }
        return turns;
    }
}
