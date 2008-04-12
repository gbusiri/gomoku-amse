package ru.amse.gomoku.board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 */
public interface IBoard {

    public static final int MY_WINNING_SIZE = 5;
    public static final int MY_BOARD_SIZE = 10;

    public static final int ADD_PERFORMED = 0;
    public static final int UNDO_PERFORMED = 1;
    public static final int REFRESH_PERFORMED = 2;
    public static final int INVALID_MOVE = 5;
    public static final int WIN_GOT = 3;

    public static final byte DIB_COLOUR_FIRST = 1;
    public static final byte DIB_COLOUR_SECOND = 2;
    public static final byte DEFAULT_DIB_COLOUR = DIB_COLOUR_FIRST;

    public void registerListener(IListener listener);

    public void unRegisterListener(IListener listener);

    public void fireEvent(int Action, Object description);

    public boolean addDib(byte height, byte width, byte colour);

    public boolean isWin();

    public byte[][] getCurrentBoard();

    public boolean isPossibleTurnPresent();

    public boolean isPossibleMove(byte height, byte width);

    public void refreshBoard();

    public boolean undoLastTurn();

    public boolean isUndoPossible();

    int getDibColour(byte i, byte j);
}
