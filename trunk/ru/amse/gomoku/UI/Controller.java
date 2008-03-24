package ru.amse.gomoku.UI;

import ru.amse.gomoku.logic.player.IPlayer;
import ru.amse.gomoku.logic.board.Board;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 18.03.2008
 * Time: 1:22:01
 * To change this template use File | Settings | File Templates.
 */
public class Controller extends Thread{

    private IPlayer myFirstPlayer;
    private IPlayer mySecondPlayer;
    private IPlayer myCurrentPlayer;

    private Board myBoard;
    private int[] myCoordinates;
    final BoardView myBoardView;
    final MyFrame myFrame;

    public Controller(BoardView boardView
                     , Board board
                     , MyFrame frame) {
        myBoardView = boardView;
        myBoard = board;
        start();
        myFrame = frame;
    }

    public void run() {
        
        myCoordinates = new int[0];
        while (!myFrame.isStarted()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {}
        }
        while ((!myBoard.isWin())
              && (myBoard.possibleTurnsPresent())) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
            myCurrentPlayer = nextPlayer();
            if (!myCurrentPlayer.isComputer()) {
                myBoardView.setFlag(false);
            }
            makeTurn();            
            myBoardView.addDib(myCoordinates[0], myCoordinates[1]);
        }
        myFrame.setGameFinished(true, myCurrentPlayer);
    }

    private synchronized void makeTurn() {
        int[] lastCoordinates = myCoordinates;
        do {
            myCurrentPlayer.makeNextTurn(myBoard.getCurrentBoard()
                                        , lastCoordinates);
            myCoordinates = myCurrentPlayer.giveNextTurn();
        } while (!myBoard.isPossibleMove(myCoordinates[0]
                                        , myCoordinates[1]));
        myBoard.addDib(myCoordinates[0]
                      , myCoordinates[1]
                      , myCurrentPlayer.getColour());
    }

    private IPlayer nextPlayer() {
        if (myCurrentPlayer == myFirstPlayer) {
            myCurrentPlayer = mySecondPlayer;
        } else if (myCurrentPlayer == null) {
            myCurrentPlayer = myFirstPlayer;
        } else {
            myCurrentPlayer = myFirstPlayer;
        }
        return myCurrentPlayer;
    }

    public void setPlayers(IPlayer first, IPlayer second) {
        myFirstPlayer = first;
        mySecondPlayer = second;
    }
}
