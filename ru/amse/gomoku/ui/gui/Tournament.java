package ru.amse.gomoku.ui.gui;

import ru.amse.gomoku.board.impl.Board;
import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.providers.IIntellectProvider;

import javax.swing.*;
import java.util.LinkedList;

/**
 * class is package private cause provider can return null if the name wanted is not
 * present.this access guarantees that the value returned by provider isn't null.
 */
class Tournament extends Thread {

    private IPlayer[] myPlayers;
    private int myNumber;
    private int myGamesFinished = 0;
    private GomokuFrame myFrame;

    public Tournament(String[] playersChosen
                         , int number
                         , IIntellectProvider provider
                         , GomokuFrame frame) {
        myPlayers = new IPlayer[playersChosen.length];
        myNumber = number;
        myFrame = frame;
        int i = 0;

        for (String name : playersChosen) {
            myPlayers[i] = provider.getPlayer(name);
            i++;
        }
        start();
    }

    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        int length = myPlayers.length;
        for (int i = 0; i < myNumber; i++) {
            LinkedList<Controller> threads = new LinkedList<Controller>();
            for (int j = 0; j < length; j++) {
                int k = j + 1;
                if (length == 1) {
                    k = j;
                }
                for ( ; k < length; k++) {
                    int first = j;
                    int second = k;
                    if (k < length / 2) {
                        first = k;
                        second = j;
                    }
                    Controller tournamentGame = new Controller(new Board(), myFrame, true);
                    threads.add(tournamentGame);
                    tournamentGame.setPlayers(myPlayers[first], myPlayers[second]);
                }
            }
            for (Controller thread: threads) {
                try {
                    thread.join();
                    myGamesFinished++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }           
        }
    }

    public int getGame() {
        return myGamesFinished;        
    }

    public int totalGameNumber() {
        return myNumber;
    }
}
