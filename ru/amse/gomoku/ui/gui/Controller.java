package ru.amse.gomoku.ui.gui;

import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.board.IBoard;

/**
 *
 */
public class Controller extends Thread {

    private IPlayer myFirstPlayer;
    private IPlayer mySecondPlayer;
    private IPlayer myCurrentPlayer;

    private IBoard myBoard;
    private byte[] myCoordinates;
    final GomokuFrame gomokuFrame;

    private boolean interrupted = false;

    public Controller(IBoard board, GomokuFrame frame) {
        myBoard = board;
        gomokuFrame = frame;
        start();
    }

    public void run() {
        myCoordinates = null;

        waitForStart();
        do {
            myCurrentPlayer = nextPlayer();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {}
            waitForContinue();

            makeTurn();
        } while ((!myBoard.isWin()) && (myBoard.isPossibleTurnPresent()));
        gomokuFrame.setGameFinished(myBoard.isPossibleTurnPresent()
                                   , myCurrentPlayer);
    }

    public void setPlayers(IPlayer first, IPlayer second) {
        myFirstPlayer = first;
        mySecondPlayer = second;
    }

    public IPlayer getCurrentPlayer() {
        return myCurrentPlayer;
    }
    
    public IPlayer getLastPlayer() {
        if (myCurrentPlayer == myFirstPlayer) {
            return mySecondPlayer;
        } else {
            return myFirstPlayer;
        }
    }

    private synchronized void waitForStart() {
        while (!gomokuFrame.isStarted()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }
    }

    private synchronized void waitForContinue() {
        while (gomokuFrame.isGamePaused()) {
            if (gomokuFrame.isUndoNeeded()) {
                undoTurn();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {}
        }
    }

    private synchronized void makeTurn() {
        byte[] lastCoordinates = myCoordinates;

        myCurrentPlayer.makeNextTurn(myBoard.getCurrentBoard()
                                    , lastCoordinates);
        if (gomokuFrame.isGamePaused()) {
            interrupted = true;
            return;                
        }
        myCoordinates = myCurrentPlayer.giveNextTurn();

        if (!myBoard.addDib(myCoordinates[0]
                      , myCoordinates[1]
                      , myCurrentPlayer.getColour())) {
            gomokuFrame.toMakeTurnIsImpossible(myCoordinates);
        }
    }

    private IPlayer nextPlayer() {
        previousPlayer();
        return myCurrentPlayer;
    }

    private void previousPlayer() {
        if (myCurrentPlayer == myFirstPlayer) {
            myCurrentPlayer = mySecondPlayer;
        } else {
            myCurrentPlayer = myFirstPlayer;
        }
    }

    private void undoTurn() {
        if (myBoard.isUndoPossible()) {
            if (!interrupted) {
                previousPlayer();
            }
            interrupted = false;
            myBoard.undoLastTurn();
        }
    }
}
