package ru.amse.gomoku.tests;

import junit.framework.TestCase;
import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.board.impl.Board;

/**
 * test is for winningSize == 5
 */
public class BoardTest extends TestCase {

    private IBoard myBoard;

    public void setUp() {
        myBoard = new Board();
    }

    public void testColour() {
        myBoard.addDib((byte)7, (byte)7, Board.DIB_COLOUR_FIRST);
        myBoard.addDib((byte)8, (byte)6, Board.DIB_COLOUR_SECOND);
        assertEquals(1, myBoard.getDibColour((byte)7, (byte)7));
        assertFalse(myBoard.getDibColour((byte)7, (byte)7) == myBoard.getDibColour((byte)8, (byte)6));
    }

    public void testAddDib() {

        assertFalse(myBoard.addDib((byte)IBoard.MY_BOARD_SIZE, (byte)3, Board.DIB_COLOUR_FIRST));
        assertFalse(myBoard.addDib((byte)3, (byte)3, (byte)3));
        assertFalse(myBoard.addDib((byte)7, (byte)7, Board.EMPTY_TURN));

    }

    public void testNumberOfDibs() {
        myBoard.addDib((byte)7, (byte)7, Board.DIB_COLOUR_FIRST);
    }

    public void testIsWin() {
        for (int i = 0; i < 4; i++) {
            myBoard.addDib((byte)(3 + i), (byte)(4 + i), (byte)1);
        }
        assertFalse(myBoard.isWin());
        myBoard.addDib((byte)7, (byte)8, Board.DIB_COLOUR_SECOND);
        assertFalse(myBoard.isWin());
        myBoard.addDib((byte)2, (byte)3, Board.DIB_COLOUR_FIRST);
        assertEquals(true, myBoard.isWin());
    }

    public void testIsWinFailing() {
        for (byte i = 0; i < 5; i++) {
            myBoard.addDib(i, i, Board.DIB_COLOUR_FIRST);
        }
        assertEquals(true, myBoard.isWin());
    }

    public void testComplexIsWin() {
        assertFalse(myBoard.isWin());
        myBoard.addDib((byte)3, (byte)3, Board.DIB_COLOUR_FIRST);
        myBoard.addDib((byte)3, (byte)4, Board.DIB_COLOUR_FIRST);
        myBoard.addDib((byte)2, (byte)2, Board.DIB_COLOUR_FIRST);
        myBoard.addDib((byte)4, (byte)4, Board.DIB_COLOUR_FIRST);
        myBoard.addDib((byte)5, (byte)5, Board.DIB_COLOUR_FIRST);
        myBoard.addDib((byte)4, (byte)5, Board.DIB_COLOUR_FIRST);
        myBoard.addDib((byte)4, (byte)3, Board.DIB_COLOUR_SECOND);
        myBoard.addDib((byte)5, (byte)4, Board.DIB_COLOUR_SECOND);
        myBoard.addDib((byte)3, (byte)2, Board.DIB_COLOUR_SECOND);
        myBoard.addDib((byte)2, (byte)3, Board.DIB_COLOUR_SECOND);
        myBoard.addDib((byte)4, (byte)1, Board.DIB_COLOUR_SECOND);
        myBoard.addDib((byte)1, (byte)4, Board.DIB_COLOUR_SECOND);
        assertFalse(myBoard.isWin());
        myBoard.addDib((byte)0, (byte)5, Board.DIB_COLOUR_SECOND);
        assertEquals(true, myBoard.isWin());
    }
}
