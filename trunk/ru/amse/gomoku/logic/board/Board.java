package ru.amse.gomoku.logic.board;

/**
 * model of the game.
 * it is a board where the dibs are placed and presence of a win is analized. 
 */
public class Board implements IBoard{

    public static final int MY_BOARD_SIZE = 10;
    public static final int MY_WINNING_SIZE = 5;
    private final Dib[][] myBoard;
	private int myNumberOfDibs;
    private int myHeightOfLastAdded;
    private int myWidthOfLastAdded;

    public Board() {
		myBoard = new Dib[MY_BOARD_SIZE][MY_BOARD_SIZE];
		myNumberOfDibs = 0;
        myHeightOfLastAdded = 0;
        myWidthOfLastAdded = 0;
    }

    /**
     * adds dib without checking dibPresent.
     * position should be checked by isPossibleMove() function.
     *
     * @param height - vertical coordinate of the dib being added.
     * @param width - horizontal coordinate of the dib being added.
     * @param colour - colour of the dib to add.
     */
    public void addDib(int height, int width, int colour) {
        if ((colour ==1) || (colour == 2)) {
			myBoard[height][width] = new Dib(colour);
			myNumberOfDibs++;
            myHeightOfLastAdded = height;
            myWidthOfLastAdded = width;
        }
    }

    public int getDibColour(int height, int width) {
        if (isDibPresent(height, width)) {
			return myBoard[height][width].getColour();
		}
		return 0;
    }

    public int getNumberOfDibs() {
        return myNumberOfDibs;
    }

    public boolean isWin() {
        return (checkDirection(1, 1)
               || checkDirection(1, 0)
               || checkDirection(0, 1)
               || checkDirection(1, -1));
    }

    public int[][] getCurrentBoard() {
        int[][] board = new int[MY_BOARD_SIZE][MY_BOARD_SIZE];
        
        for (int i = 0; i < MY_BOARD_SIZE; i++) {
            for (int j = 0; j < MY_BOARD_SIZE; j++) {
                board[i][j] = getDibColour(i, j);
            }
        }
        return board;
    }

    public boolean possibleTurnsPresent() {
        for (int i = 0; i < MY_BOARD_SIZE; i++) {
            for (int j = 0; j < MY_BOARD_SIZE; j++) {
                if (myBoard[i][j] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPossibleMove(int height, int width) {
        return (coordinateAcceptance(height, width)
                && !(isDibPresent(height, width)));
    }

    public void refreshBoard() {
        for (int i = 0; i < MY_BOARD_SIZE; i++) {
            for (int j = 0; j < MY_BOARD_SIZE; j++) {
                myBoard[i][j] = null;
            }
        }
    }

    private boolean checkDirection(int vDirection, int hDirection) {
        return (search(myHeightOfLastAdded
                     , myWidthOfLastAdded
                     , vDirection
                     , hDirection
                     , search(myHeightOfLastAdded
                             , myWidthOfLastAdded
                             , -vDirection
                             , -hDirection
                             , 1))
               == MY_WINNING_SIZE);
    }

    private int search(int height
                      , int width
                      , int heightChange
                      , int widthChange
                      , int count) {
        if ((count < MY_WINNING_SIZE)
           && isDibPresent(height + heightChange, width + widthChange)
           && (checkColours(height
                           , width
                           , height + heightChange
                           , width + widthChange))) {
            return search(height + heightChange
                         , width + widthChange
                         , heightChange
                         , widthChange
                         , ++count);
        } else {
            return count;
        }
    }

    private boolean checkColours(int height1, int width1, int height2, int width2) {
        return (myBoard[height1][width1].getColour()
               == myBoard[height2][width2].getColour());
    }

    private boolean isDibPresent(int height, int width) {
        return (coordinateAcceptance(height, width)
                && (myBoard[height][width] != null));
    }

    private boolean coordinateAcceptance(int height, int width) {
        return !((height < 0) || (height >= MY_BOARD_SIZE)
                || (width < 0) || (width >= MY_BOARD_SIZE));
    }
}