package ru.amse.gomoku.logic.board;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 23.02.2008
 * Time: 2:30:18
 * To change this template use File | Settings | File Templates.
 */
public interface IBoard {

    public void addDib(int height, int width, int colour);

    public int getDibColour(int height, int width);

    public int getNumberOfDibs();

    public boolean isWin();

    public int[][] getCurrentBoard();

    public boolean possibleTurnsPresent();

    public boolean isPossibleMove(int height, int width);

    public void refreshBoard();
}
