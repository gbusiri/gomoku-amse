package ru.amse.gomoku.PlayerProvider.Impl;

import ru.amse.gomoku.Board.IBoard;
import ru.amse.gomoku.PlayerProvider.IPlayerProvider;
import ru.amse.gomoku.Players.IPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 */
public class PlayerProvider implements IPlayerProvider {

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
            if (myPlayers.get(name).getClass().getName().equals("PersonPlayer")) {

            }
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
