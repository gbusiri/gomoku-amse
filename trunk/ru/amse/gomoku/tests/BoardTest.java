package ru.amse.gomoku.tests;

import junit.framework.TestCase;

/**
 * test is for winningSize == 5
 */
public class BoardTest extends TestCase {
    /*
    private board myBoard;

    public void setUp() {
        myBoard = new board();
    }

    public void testColour() {
        myBoard.addDib(7, 7, board.DIB_COLOUR_FIRST);
        myBoard.addDib(8, 6, board.DIB_COLOUR_SECOND);
        assertEquals(1, myBoard.getDibColour(7, 7));
        assertFalse(myBoard.getDibColour(7, 7) == myBoard.getDibColour(8, 6));
    }

    public void testAddDib() {

        assertFalse(myBoard.addDib(IBoard.MY_BOARD_SIZE, 3, board.DIB_COLOUR_FIRST));
        assertEquals(0, myBoard.getNumberOfDibs());
        assertFalse(myBoard.addDib(3, 3, 3));
        assertFalse(myBoard.addDib(7, 7, board.EMPTY_TURN));

    }

    public void testNumberOfDibs() {
        assertEquals(0, myBoard.getNumberOfDibs());
        myBoard.addDib(7, 7, board.DIB_COLOUR_FIRST);
        assertEquals(1, myBoard.getNumberOfDibs());
    }

    public void testIsWin() {
        for (int i = 0; i < 4; i++) {
            myBoard.addDib(3 + i, 4 + i, 1);
        }
        assertFalse(myBoard.isWin());
        myBoard.addDib(7, 8, board.DIB_COLOUR_SECOND);
        assertFalse(myBoard.isWin());
        myBoard.addDib(2, 3, board.DIB_COLOUR_FIRST);
        assertEquals(true, myBoard.isWin());
    }

    public void testIsWinFailing() {
        for (int i = 0; i < 5; i++) {
            myBoard.addDib(i, i, board.DIB_COLOUR_FIRST);
        }
        assertEquals(true, myBoard.isWin());
    }

    public void testComplexIsWin() {
        assertFalse(myBoard.isWin());
        myBoard.addDib(3, 3, board.DIB_COLOUR_FIRST);
        myBoard.addDib(3, 4, board.DIB_COLOUR_FIRST);
        myBoard.addDib(2, 2, board.DIB_COLOUR_FIRST);
        myBoard.addDib(4, 4, board.DIB_COLOUR_FIRST);
        myBoard.addDib(5, 5, board.DIB_COLOUR_FIRST);
        myBoard.addDib(4, 5, board.DIB_COLOUR_FIRST);
        myBoard.addDib(4, 3, board.DIB_COLOUR_SECOND);
        myBoard.addDib(5, 4, board.DIB_COLOUR_SECOND);
        myBoard.addDib(3, 2, board.DIB_COLOUR_SECOND);
        myBoard.addDib(2, 3, board.DIB_COLOUR_SECOND);
        myBoard.addDib(4, 1, board.DIB_COLOUR_SECOND);
        myBoard.addDib(1, 4, board.DIB_COLOUR_SECOND);
        assertFalse(myBoard.isWin());
        myBoard.addDib(0, 5, board.DIB_COLOUR_SECOND);
        assertEquals(true, myBoard.isWin());
    }       */
}
