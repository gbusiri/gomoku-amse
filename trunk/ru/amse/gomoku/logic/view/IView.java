package ru.amse.gomoku.logic.view;

import ru.amse.gomoku.logic.board.Board;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 24.02.2008
 * Time: 14:34:13
 * To change this template use File | Settings | File Templates.
 */
public interface IView {

    public void printBoard(Board currentPosition);

    public int[] tellPlayerToMakeMove(String player);

    public void tellPlayerTheMoveIsInvalid(String player);

    public void printWin(String player);

    public void printStart();

    public void printDraw();
}
