package ru.amse.gomoku.Players.Impl.MyaiPlayer;

/**
 *
 */
public class Main {
    public static final int SIZE = 10;

    static void print(int height, int width, int colour, int estimated) {
        System.out.println(" height = " + height + "   width = " + width + "   colour = "
                + colour + "   estimated = " + estimated);
    }

    public static void main(String[] args) {
        byte[][] board = new byte[SIZE][SIZE];
        Estimator estim = new Estimator(board);

        board[2][4] = 1;
        board[2][5] = 1;
        board[3][2] = 2;
        board[3][3] = 2;
        board[3][4] = 2;
        board[3][5] = 2;
        board[4][2] = 1;
        board[4][4] = 2;
        board[4][5] = 1;
        board[5][3] = 1;
        board[5][4] = 2;
        board[5][5] = 1;
        board[6][5] = 2;

        print(board);
        for (byte i = 0; i < board.length; i++) {
            for (byte j = 0; j < board.length; j++) {
                print(i, j, 1, estim.estimate(i, j, (byte)1, board));
            }
        }


    }

    static void print(byte [][] possibilities) {
        for (int k = 0; k < possibilities.length; k++) {
            for (int l = 0; l < possibilities.length; l++) {
                System.out.print(possibilities[k][l] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

}
