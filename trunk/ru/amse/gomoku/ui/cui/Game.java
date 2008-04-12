package ru.amse.gomoku.ui.cui;

import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.board.impl.Board;
import ru.amse.gomoku.ui.cui.View;

/**
 * 
 */
public class Game {

    private final IPlayer myFirstPlayer;
    private final IPlayer mySecondPlayer;
    private IPlayer myCurrentPlayer;
    private byte[] myCoordinates;
    private Board myBoard;
    private IView view;

    public Game(IPlayer p1, IPlayer p2) {
        myFirstPlayer = p1;
        mySecondPlayer = p2;
    }

    public void runGame() {
        view = new View();
        myBoard = new Board();

        myCoordinates = null;
        while ((!myBoard.isWin())
              && (myBoard.isPossibleTurnPresent())) {
            myCurrentPlayer = nextPlayer();
            makeTurn();
            view.printBoard(myBoard);
        }
        view.printWin(myCurrentPlayer.getName());
    }

    private void makeTurn() {
        byte[] lastCoordinates = myCoordinates;
        boolean check = false;
        do {
            myCurrentPlayer.makeNextTurn(myBoard.getCurrentBoard()
                                        , lastCoordinates);
            myCoordinates = myCurrentPlayer.giveNextTurn();
            if (check) {
                view.tellPlayerTheMoveIsInvalid(myCurrentPlayer.getName());
            }
            check = true;
        } while (!myBoard.isPossibleMove(myCoordinates[0]
                                        , myCoordinates[1]));
        myBoard.addDib(myCoordinates[0]
                      , myCoordinates[1]
                      , myCurrentPlayer.getColour());
    }


    private IPlayer nextPlayer() {
        if (myCurrentPlayer == myFirstPlayer) {
            myCurrentPlayer = mySecondPlayer;
            return mySecondPlayer;
        } else if (myCurrentPlayer == null) {
            myCurrentPlayer = myFirstPlayer;
            return myFirstPlayer;
        } else {
            myCurrentPlayer = myFirstPlayer;
            return myFirstPlayer;
        }
    }
}
