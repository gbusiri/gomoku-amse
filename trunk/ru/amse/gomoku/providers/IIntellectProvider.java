package ru.amse.gomoku.providers;

import ru.amse.gomoku.players.IPlayer;

import javax.swing.*;

/**
 *
 */
public interface IIntellectProvider {

    public static final int MY_MAX_NAME_SIZE = 10;

    public void registerPlayer(String name, IPlayer player)
                throws IllegalAccessException;

    public void unRegisterPlayer(String name);

    public IPlayer getPlayer(String name);

    public ImageIcon getPlayerImage(String playerName);

    public String[] getAllNames();

    public int size();
}
