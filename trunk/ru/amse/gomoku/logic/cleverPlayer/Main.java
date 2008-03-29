package ru.amse.gomoku.logic.cleverPlayer;

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
            int[][] board = new int[SIZE][SIZE];
            Estimator estim = new Estimator();

            board[2][4] = 1;
            board[2][5] = 1;
            //board[3][2] = 2;
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

            estim.printNeeded = true;
            print(board);

            print(3, 2, 1, estim.estimate(3, 2, 1, board));
            print(3, 6, 1, estim.estimate(3, 6, 1, board));
            print(3, 1, 1, estim.estimate(3, 1, 1, board));
            print(6, 4, 1, estim.estimate(6, 4, 1, board));
            print(4, 3, 1, estim.estimate(4, 3, 1, board));
            print(7, 6, 1, estim.estimate(7, 6, 1, board));
            print(2, 3, 1, estim.estimate(2, 3, 1, board));
            print(2, 6, 1, estim.estimate(2, 6, 1, board));

            /*
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 0) {
                        print(i, j, 1, estim.estimate(i, j, 1, board));
                    }
                }
            } */
            board[3][6] = 1;
            print(board);

            print(3, 2, 2, estim.estimate(3, 2, 2, board));
            print(3, 1, 2, estim.estimate(3, 1, 2, board));
            print(6, 4, 2, estim.estimate(6, 4, 2, board));
            print(4, 3, 2, estim.estimate(4, 3, 2, board));
            print(7, 6, 2, estim.estimate(7, 6, 2, board));
            print(2, 3, 2, estim.estimate(2, 3, 2, board));
            print(2, 6, 2, estim.estimate(2, 6, 2, board));
            print(3, 7, 2, estim.estimate(3, 7, 2, board));

            /*
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 0) {
                        print(i, j, 2, estim.estimate(i, j, 2, board));
                    }
                }
            }
              */
            board[4][3] = 2;
            //print(board);

            /*
            estim.printNeeded = true;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 0) {
                        print(i, j, 1, estim.estimate(i, j, 1, board));                                               
                    }
                }
            } */

        }

        static void print(int [][] possibilities) {
            for (int k = 0; k < possibilities.length; k++) {
                for (int l = 0; l < possibilities.length; l++) {
                    System.out.print(possibilities[k][l] + " ");
                }
                System.out.println(" ");
            }
            System.out.println(" ");
        }

}
