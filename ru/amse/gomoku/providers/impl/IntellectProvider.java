package ru.amse.gomoku.providers.impl;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.providers.IIntellectProvider;
import ru.amse.gomoku.players.IPlayer;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 */
public class IntellectProvider implements IIntellectProvider {

    private Hashtable<String, IPlayer> myPlayers = new Hashtable<String, IPlayer>();
    private int mySize = 0;

    public void registerPlayer(String name, IPlayer player) throws IllegalAccessException {
        if (myPlayers.containsKey(name)) {
            throw new IllegalAccessException("Trying to add a player with existing name!");
        }
        myPlayers.put(name, player);
        mySize++;
    }

    public void unRegisterPlayer(String name) {
        if (myPlayers.remove(name) != null) {
            mySize--;
        }
    }

    public IPlayer getPlayer(String name) {
        IPlayer toGet = null;
        try {
            toGet = myPlayers.get(name).getClass()
                    .getConstructor(String.class, byte.class)
                    .newInstance(name, IBoard.DEFAULT_DIB_COLOUR);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return toGet;
    }

    public ImageIcon getPlayerImage(String playerName) {
        IPlayer player = myPlayers.get(playerName);
        return player.getImage();
    }

    public String[] getAllNames() {        
        Enumeration<String>  enu = myPlayers.keys();
        String[] names = new String[myPlayers.size()];

        for (int i = 0; i < names.length; i++) {
            names[i] = enu.nextElement();
        }
        return names;
    }

    public int size() {
        return mySize;
    }
}
