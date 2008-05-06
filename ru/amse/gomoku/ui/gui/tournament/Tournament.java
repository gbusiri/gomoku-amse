package ru.amse.gomoku.ui.gui.tournament;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.board.impl.Board;
import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.providers.IIntellectProvider;
import ru.amse.gomoku.ui.gui.view.GomokuFrame;
import ru.amse.gomoku.ui.gui.view.TournamentView;
import ru.amse.gomoku.ui.gui.game.Controller;

import java.util.LinkedList;

/**
 * 
 */
public class Tournament extends Thread {

    private IPlayer[] myPlayers;
    private String[] myPlayersNames;
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
        myPlayersNames = playersChosen;
        myLength = myPlayersNames.length;
        if (myLength == 1) {
            myLength++;
        }
        myPlayers = new IPlayer[myLength];
        myNumber = number;
        myFrame = frame;

        for (int i = 0; i < myPlayersNames.length; i++) {
            myPlayers[i] = provider.getPlayer(myPlayersNames[i]);
            //System.out.println(myPlayers[i].getClass().getName());
        }
        if (playersChosen.length == 1) {
            myPlayers[1] = provider.getPlayer(myPlayersNames[0]);
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
                    if (i < myNumber / 2) {
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
                    IPlayer firstPlayer = clonePlayer(myPlayers[first]);
                    IPlayer secondPlayer = clonePlayer(myPlayers[second]);

                    //System.out.println(myPlayers[first].getName() + "  vs  " + myPlayers[second].getName() );
                    if (myPlayers[first].equals(myPlayers[second])) {
                        System.exit(-1);
                    }
                    if (firstPlayer == null || secondPlayer == null) {
                        firstPlayer = myPlayers[first];
                        secondPlayer = myPlayers[second];
                    }
                    firstPlayer.setColour(IBoard.DIB_COLOUR_FIRST);
                    secondPlayer.setColour(IBoard.DIB_COLOUR_SECOND);
                    tournamentGame.setTournamentPlayers(firstPlayer
                                                       , secondPlayer
                                                       , myResult);

                    //mistake is that same objects are playing
                    //several games at the same time...

                }
            }
            for (Controller thread: threads) {
                try {
                    if (thread.isAlive()) {
                        thread.join();
                    }
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

    public int getPlayersNumber() {
        return myLength;
    }

    public String[] getAllPlayers() {
        return myPlayersNames;
    }

    public int totalGameNumber() {
        return myNumber * myLength * (myLength - 1) / 2;
    }

    public void setReady(TournamentView result) {
        myResult = result;
        isReadyToStart = true;
    }

    private IPlayer clonePlayer(IPlayer player) {
        IPlayer cloned;
        try {
            cloned = player.getClass().newInstance();
            cloned.setName(player.getName());
            return cloned;
        } catch (Exception e) {}
        return null;
    }
}
