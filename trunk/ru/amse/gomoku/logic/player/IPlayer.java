package ru.amse.gomoku.logic.player;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 24.02.2008
 * Time: 15:19:20
 * To change this template use File | Settings | File Templates.
 */
public interface IPlayer {

    public String getName();

    public int getColour();

    public boolean isComputer();

    public void makeNextTurn(int[][] board, int[] coordinates);

    public int[] giveNextTurn();
}
