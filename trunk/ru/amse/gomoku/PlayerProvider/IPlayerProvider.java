package ru.amse.gomoku.PlayerProvider;

import ru.amse.gomoku.Players.IPlayer;

/**
 *
 */
public interface IPlayerProvider {

    public void registerPlayer(String name, IPlayer player)
                throws IllegalAccessException;

    public void unRegisterPlayer(String name);

    public IPlayer getPlayer(String name);

    public String[] getAllNames();

    public int size();
}
