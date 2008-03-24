package ru.amse.gomoku.logic.board;

import junit.framework.TestCase;
/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 23.02.2008
 * Time: 2:15:08
 * To change this template use File | Settings | File Templates.
 */
public class BoardTest extends TestCase {

    private Board myBoard;

    public void setUp() {
        myBoard = new Board();
    }

    public void testColour() {
        myBoard.addDib(7, 7, 1);
        myBoard.addDib(8, 6, 2);
        assertEquals(1, myBoard.getDibColour(7, 7));
        assertFalse(myBoard.getDibColour(7, 7) == myBoard.getDibColour(8, 6));
    }

    public void testAddDib() {
        /*
        assertFalse(myBoard.addDib(15, 3, 1));
        assertEquals(0, myBoard.getNumberOfDibs());
        assertFalse(myBoard.addDib(3, 3, 3));
        assertFalse(myBoard.addDib(7, 7, 0));
        */
    }

    public void testNumberOfDibs() {
        assertEquals(0, myBoard.getNumberOfDibs());
        myBoard.addDib(7, 7, 1);
        assertEquals(1, myBoard.getNumberOfDibs());
    }

    public void testIsWin() {
        for (int i = 0; i < 4; i++) {
            myBoard.addDib(3 + i, 4 + i, 1);
        }
        assertFalse(myBoard.isWin());
        myBoard.addDib(7, 8, 2);
        assertFalse(myBoard.isWin());
        myBoard.addDib(2, 3, 1);
        assertEquals(true, myBoard.isWin());
    }

    public void testIsWinFailing() {
        for (int i = 0; i < 5; i++) {
            myBoard.addDib(i, i, 1);
        }
        assertEquals(true, myBoard.isWin());
    }

    public void testComplexIsWin() {
        assertFalse(myBoard.isWin());
        myBoard.addDib(3, 3, 1);
        myBoard.addDib(3, 4, 1);
        myBoard.addDib(2, 2, 1);
        myBoard.addDib(4, 4, 1);
        myBoard.addDib(5, 5, 1);
        myBoard.addDib(4, 5, 1);
        myBoard.addDib(4, 3, 2);
        myBoard.addDib(5, 4, 2);
        myBoard.addDib(3, 2, 2);
        myBoard.addDib(2, 3, 2);
        myBoard.addDib(4, 1, 2);
        myBoard.addDib(1, 4, 2);
        assertFalse(myBoard.isWin());
        myBoard.addDib(0, 5, 2);
        assertEquals(true, myBoard.isWin());
    }
}
