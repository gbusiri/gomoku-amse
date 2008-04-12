package ru.amse.gomoku.ui.cui;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.ui.cui.IView;

import java.util.Scanner;
import java.util.InputMismatchException;

/**
 *
 */
public class View implements IView {

    public void printBoard(IBoard currentPosition) {
        for (byte i = 0; i < IBoard.MY_BOARD_SIZE; i++) {
            for (byte j = 0; j < IBoard.MY_BOARD_SIZE; j++) {
                System.out.print(" " + currentPosition.getDibColour(i, j));
            }
            System.out.println();
        }
        System.out.println();
    }

    public byte[] tellPlayerToMakeMove(String player) {
        System.out.println(" Player " + player + " please make the next move");
        Scanner scan = new Scanner(System.in);
        byte[] coordinates = new byte[2];
        try {
            coordinates[0] = scan.nextByte();
            if (coordinates[0] == -1) {
                System.exit(0);
            }
            coordinates[1] = scan.nextByte();
        } catch (InputMismatchException e) {
            tellPlayerTheMoveIsInvalid(player);
            return tellPlayerToMakeMove(player);
        }
        return coordinates;
    }

    public void tellPlayerTheMoveIsInvalid(String player) {
        System.out.println(" Player " + player + " the current move is invalid \n ");
    }

    public void printWin(String player) {
        System.out.println("Player " + player + " congratulations! You have just won the game!");
    }

    public void printStart() {
        System.out.println("Welcome! The game has started!\n" + "type -1 if you want to exit");
    }

    public void printDraw() {
        System.out.println("It is a draw!");
    }
}
