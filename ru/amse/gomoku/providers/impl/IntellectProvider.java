package ru.amse.gomoku.providers.impl;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.players.impl.AIPlayer;
import ru.amse.gomoku.providers.IIntellectProvider;
import ru.amse.gomoku.*;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 *
 */
public class IntellectProvider extends URLClassLoader implements IIntellectProvider {

    private Hashtable<String, IPlayer> myPlayers = new Hashtable<String, IPlayer>();
    private LinkedList<File> myAddedPlayers = new LinkedList<File>();
    private int mySize = 0;

    public void registerPlayer(String name, IPlayer player) throws IllegalAccessException {
        if (myPlayers.containsKey(name)) {
            throw new IllegalAccessException("Trying to add a player with existing name!");
        }
        myPlayers.put(name, player);
        mySize++;
    }

    public IntellectProvider(Main m){
            super(new URL[0], m.getClass().getClassLoader());
        }

    public void registerPlayer(File playerFile) {
        String path = playerFile.getPath();
        String name = playerFile.getName();
        try {
            System.out.println(path);
            URL url = new URL(path);
            ClassLoader cl = new URLClassLoader(new URL[]{url});
            try {
                Class c = cl.loadClass(name);
                try {
                    this
                    .registerPlayer(playerFile.getName()
                    , (IPlayer) c.getConstructor(String.class, byte.class)
                               .newInstance(playerFile.getName()
                                           , IBoard.DEFAULT_DIB_COLOUR));
                    myAddedPlayers.add(playerFile);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void unRegisterPlayer(String name) {
        if (myPlayers.remove(name) != null) {
            mySize--;
        }
    }

    public IPlayer getPlayer(String name) {
        IPlayer toGet = null;
        try {
            toGet = myPlayers.get(name).getClass().newInstance();
        } catch (Exception ee) {
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
        }
        toGet.setName(name);
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

    public LinkedList<File> getAddedPlayers() {
        return myAddedPlayers;
    }

    public int size() {
        return mySize;
    }

    public static void main(String args[]){
            Main m = new Main();
            IntellectProvider ip = new IntellectProvider(m);

            try {             
                String path = "C:\\temp\\el\\ru\\amse\\gomoku\\bin\\my.jar";
                //remember vasya.jar
                   ip.addURL(new URL("file:" + path));
                Class c = ip.loadClass("ru.amse.gomoku.players.impl.AIPlayer");

                AIPlayer aip = (AIPlayer) c.newInstance();
                System.out.println(aip.getName());

            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (MalformedURLException e) {}
            catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

}
