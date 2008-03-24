package ru.amse.gomoku.logic.view;

import ru.amse.gomoku.logic.board.Board;

import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 24.02.2008
 * Time: 14:27:12
 * To change this template use File | Settings | File Templates.
 */
public class View implements IView{

    public void printBoard(Board currentPosition) {
        for (int i = 0; i < Board.MY_BOARD_SIZE; i++) {
            for (int j = 0; j < Board.MY_BOARD_SIZE; j++) {
                System.out.print(" " + currentPosition.getDibColour(i, j));
            }
            System.out.println();
        }
        System.out.println();
    }

    public int[] tellPlayerToMakeMove(String player) {
        System.out.println(" Player " + player + " please make the next move");
        Scanner scan = new Scanner(System.in);
        int[] coordinates = new int[2];
        try {
            coordinates[0] = scan.nextInt();
            if (coordinates[0] == -1) {
                System.exit(0);
            }
            coordinates[1] = scan.nextInt();
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
