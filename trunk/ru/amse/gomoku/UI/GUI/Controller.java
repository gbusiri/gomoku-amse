package ru.amse.gomoku.UI.GUI;

import ru.amse.gomoku.Players.IPlayer;
import ru.amse.gomoku.Board.IBoard;

/**
 *
 */
public class Controller extends Thread{

    private IPlayer myFirstPlayer;
    private IPlayer mySecondPlayer;
    private IPlayer myCurrentPlayer;

    private IBoard myBoard;
    private byte[] myCoordinates;
    final BoardView myBoardView;
    final GomokuFrame gomokuFrame;

    public Controller(BoardView boardView
                     , IBoard board
                     , GomokuFrame frame) {
        myBoardView = boardView;
        myBoard = board;
        gomokuFrame = frame;
        start();       
    }

    public void run() {

        myCoordinates = null;
        boolean check;

        waitForStart();
        while ((!myBoard.isWin())
              && (myBoard.isPossibleTurnPresent())) {
            myCurrentPlayer = nextPlayer();
            gomokuFrame.setStatus(myCurrentPlayer);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
            waitForContinue();

            makeTurn();
        }
        check = myBoard.isWin();
        gomokuFrame.setStatusWait();
        gomokuFrame.setGameFinished(check, myCurrentPlayer);
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
                gomokuFrame.setUndoNeeded(false);
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
            return;                
        }
        myCoordinates = myCurrentPlayer.giveNextTurn();

        if (!myBoard.isPossibleMove(myCoordinates[0], myCoordinates[1])) {
            gomokuFrame.toMakeTurnIsImpossible(myCoordinates);
        } else {
            myBoard.addDib(myCoordinates[0]
                          , myCoordinates[1]
                          , myCurrentPlayer.getColour());
            myBoardView.addDib(myCoordinates[0], myCoordinates[1]);
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

    public void setPlayers(IPlayer first, IPlayer second) {
        myFirstPlayer = first;
        mySecondPlayer = second;
    }

    private void undoTurn() {
        if (myBoard.isUndoPossible()) {
            myBoard.undoLastTurn();
            myBoardView.undoTurn();
            previousPlayer();
            gomokuFrame.setStatus(myCurrentPlayer);
        }
    }
}
