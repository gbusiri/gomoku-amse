package ru.amse.gomoku.ui.gui;

import ru.amse.gomoku.board.impl.Board;
import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.providers.IIntellectProvider;

import java.util.LinkedList;

/**
 * class is package private cause provider can return null if the name wanted is not
 * present.this access guarantees that the value returned by provider isn't null.
 */
class Tournament extends Thread {

    private IPlayer[] myPlayers;
    private int myNumber;
    private int myGamesFinished = 0;
    private int myLength;

    private GomokuFrame myFrame;
    private TournamentView myResult;


    private boolean isReadyToStart = false;

    public Tournament(String[] playersChosen
                         , int number
                         , IIntellectProvider provider
                         , GomokuFrame frame) {
        myLength = playersChosen.length;
        if (myLength == 1) {
            myLength++;
        }
        myPlayers = new IPlayer[myLength];
        myNumber = number;
        myFrame = frame;

        for (int i = 0; i < playersChosen.length; i++) {
            myPlayers[i] = provider.getPlayer(playersChosen[i]);
        }
        if (playersChosen.length == 1) {
            myPlayers[1] = provider.getPlayer(playersChosen[0]);            
        }
        start();
    }

    public void run() {
        waitForStart();

        for (int i = 0; i < myNumber; i++) {
            LinkedList<Controller> threads = new LinkedList<Controller>();

            for (int j = 0; j < myLength; j++) {

                for (int k = j + 1; k < myLength; k++) {
                    int first = j;
                    int second = k;
                    if (k < myLength / 2) {
                        first = k;
                        second = j;
                    }
                    Controller tournamentGame = new Controller(new Board(), myFrame, true);
                    threads.add(tournamentGame);

                    String name;
                    if (myPlayers[first].getName().equals(myPlayers[second].getName())) {
                        name = myPlayers[first].getName();
                        myPlayers[first].setName(name + " first");
                        myPlayers[second].setName(name + " second");
                    }
                    tournamentGame.setTournamentPlayers(myPlayers[first]
                                                       , myPlayers[second]
                                                       , myResult);
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

    private void waitForStart() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        } while (!isReadyToStart);
    }

    public int getGame() {
        return myGamesFinished;        
    }

    public int totalGameNumber() {
        return myNumber * myLength * (myLength - 1) / 2;
    }

    public void setReady(TournamentView result) {
        myResult = result;
        isReadyToStart = true;
    }
}
