package ru.amse.gomoku.UI.CUI;

import ru.amse.gomoku.Board.IBoard;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 24.02.2008
 * Time: 14:34:13
 * To change this template use File | Settings | File Templates.
 */
public interface IView {

    public void printBoard(IBoard currentPosition);

    public byte[] tellPlayerToMakeMove(String player);

    public void tellPlayerTheMoveIsInvalid(String player);

    public void printWin(String player);

    public void printStart();

    public void printDraw();
}
