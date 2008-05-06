package ru.amse.gomoku.ui.gui.game;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.ui.gui.view.TournamentView;
import ru.amse.gomoku.ui.gui.view.GomokuFrame;

/**
 *
 */
public class Controller extends Thread {

    private IPlayer myFirstPlayer;
    private IPlayer mySecondPlayer;
    private IPlayer myCurrentPlayer;

    private IBoard myBoard;
    private byte[] myCoordinates;
    private TournamentView myResultTable;
    final GomokuFrame gomokuFrame;

    private volatile boolean interrupted = false;
    private volatile boolean isReadyToStart = false;
    private final boolean isTournament;
    private boolean isIllegalOperation = false;

    public Controller(IBoard board, GomokuFrame frame, boolean isTournament) {
        myBoard = board;
        gomokuFrame = frame;
        this.isTournament = isTournament;
        start();
    }

    public void run() {
        myCoordinates = null;

        waitForStart();
        do {
            myCurrentPlayer = nextPlayer();
            if (!isTournament) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
                waitForContinue();
            } else if (isIllegalOperation) {
                System.err.println("illegal turn occurred");
                return;
            }

            if (gomokuFrame.isInterrupted()) {
                return;
            }

            makeTurn();
        } while ((!myBoard.isWin()) && (myBoard.isPossibleTurnPresent()));
        
        if (!isTournament) {
            gomokuFrame.setGameFinished(myBoard.isPossibleTurnPresent()
                                       , myCurrentPlayer);
        } else {
            checkLock();            
        }
    }

    private void checkLock() {
        while (myResultTable.tryLock()) {
            try {
                Thread.sleep(111);
            } catch (InterruptedException e) {}
        }
        if (!myResultTable.tryLock()) {
            myResultTable.setWinner(myBoard.isPossibleTurnPresent()
                                   , myFirstPlayer
                                   , mySecondPlayer
                                   , myCurrentPlayer);
        } else {
            checkLock();
        }
    }

    public void setPlayers(IPlayer first, IPlayer second) {
        myFirstPlayer = first;
        mySecondPlayer = second;
        isReadyToStart = true;
    }

    public void setTournamentPlayers(IPlayer first
                                    , IPlayer second
                                    , TournamentView result) {
        myResultTable = result;
        setPlayers(first, second);
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
        while (!isReadyToStart) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }
    }

    private synchronized void waitForContinue() {
        while (gomokuFrame.isGamePaused() && !gomokuFrame.isInterrupted()) {
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
        if ((gomokuFrame.isGamePaused()) || gomokuFrame.isInterrupted()) {
            interrupted = true;
            return;                
        }
        myCoordinates = myCurrentPlayer.giveNextTurn();
        if (!myBoard.addDib(myCoordinates[0]
                      , myCoordinates[1]
                      , myCurrentPlayer.getColour())) {
            if (!isTournament) {
                gomokuFrame.toMakeTurnIsImpossible(myCoordinates);
            } else {
                isIllegalOperation = true;
            }
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
        if (!myBoard.isUndoPossible()) {
            gomokuFrame.undo(false);            
        }
    }
}
