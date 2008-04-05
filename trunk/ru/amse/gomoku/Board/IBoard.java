package ru.amse.gomoku.Board;

/**
 * 
 */
public interface IBoard {

    public static final int MY_WINNING_SIZE = 5;
    public static final int MY_BOARD_SIZE = 15;

    public static final byte DIB_COLOUR_FIRST = 1;
    public static final byte DIB_COLOUR_SECOND = 2;
    public static final byte DEFAULT_DIB_COLOUR = DIB_COLOUR_FIRST;

    public boolean addDib(byte height, byte width, byte colour);

    public byte getDibColour(byte height, byte width);

    public int getNumberOfDibs();

    public boolean isWin();

    public byte[][] getCurrentBoard();

    public boolean isPossibleTurnPresent();

    public boolean isPossibleMove(byte height, byte width);

    public void refreshBoard();

    public boolean undoLastTurn();

    public boolean isUndoPossible(); 
}
