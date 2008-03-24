package ru.amse.gomoku.logic.aiPlayer;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 06.03.2008
 * Time: 21:31:00
 * To change this template use File | Settings | File Templates.
 */
public class PossibilityBorderTest extends TestCase {

    private PossibilityBorder border;
    private PossibilityBorder borderr;

    public void setUp() {
        border = new PossibilityBorder(2, 5, 3);
        borderr = new PossibilityBorder(2, 15, 4);
    }

    public void testAddCoordinates() {
        
        border.addCoordinates(new int[] {2, 2});
        border.print(border.possibleTurns);
        int[] needed = border.getCoordinates(0);
        assertEquals(0, needed[0]);
        assertEquals(0, needed[1]);
        assertFalse(border.checkUpperLevels(needed[0], needed[1], 3));
        assertEquals(new int[] {0, 1}, border.getCoordinates(1), 0);
        assertEquals(new int[] {0, 2}, border.getCoordinates(1), 1);
        assertEquals(new int[] {0, 3}, border.getCoordinates(2), 2);
        assertEquals(new int[] {1, 0}, border.getCoordinates(3), 3);
        assertFalse(border.checkUpperLevels(0, 3, 3));
        assertFalse(border.checkUpperLevels(0, 1, 2));
        assertEquals(new int[] {1, 1}, border.getCoordinates(3), 4);
        assertEquals(new int[] {0, 3}, border.getCoordinates(1), 5);
        assertEquals(new int[] {0, 1}, border.getCoordinates(0), 6);
        assertEquals(new int[] {0, 0}, border.getCoordinates(1), 7);
        border.addCoordinates(new int[] {4, 4});
        border.addCoordinates(new int[] {1, 4});


    }

    public void testGetCoordinates() {
        borderr.addCoordinates(new int[] {1, 1});
        borderr.addCoordinates(new int[] {8, 8});
        borderr.addCoordinates(new int[] {7, 9});
        
        assertEquals(new int[] {0, 0}, borderr.getCoordinates(0), 11);
    }

    public void testGetNext() {

    }

    public void testReseting() {

    }


        protected void assertEquals(int[] value, int[] expected, int i) {
			if (Arrays.equals(value, expected)) {
					System.err.println("Test " + i + " passed");
			} else {
				System.err.println("Test " + i + " failed: "
                        + " expexted " + value[0] + " , " + value[1] +
                "  got " + expected[0] + " , " + expected[1]);
			}
        }
}
