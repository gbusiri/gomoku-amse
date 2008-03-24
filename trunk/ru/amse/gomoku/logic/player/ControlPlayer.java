package ru.amse.gomoku.logic.player;

import ru.amse.gomoku.logic.board.Board;
import java.util.Random;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 03.03.2008
 * Time: 14:04:10
 * To change this template use File | Settings | File Templates.
 */
public class ControlPlayer/* extends Player*/{    /*

    public static final int MY_MAX = 50000;
    private final int MY_MAX_LEVEL = 3;
    private int[][] myBoard;
    private final int myOpponentsColour;
    private Border border;
    private boolean notEmpty = false;
    private int myPossibilities;

    private int countEs = 0;

    public ControlPlayer(String name, int colour) {
        super(name, true, colour);
        myOpponentsColour = colour % 2 + 1;
        border = new Border(Board.MY_BOARD_SIZE
                           , Board.MY_BOARD_SIZE / 2 - Board.MY_WINNING_SIZE
                           , Board.MY_BOARD_SIZE / 2 + Board.MY_WINNING_SIZE
                           , Board.MY_BOARD_SIZE / 2 - Board.MY_WINNING_SIZE
                           , Board.MY_BOARD_SIZE / 2 + Board.MY_WINNING_SIZE);
    }

    public int[] makeNextTurn(int[][] board, int[] coordinates) {
        myBoard = board;
        countEs = 0;
        if (checkIfEmpty()) {
            return new int[]{Board.MY_BOARD_SIZE / 2, Board.MY_BOARD_SIZE / 2};
        }
        myPossibilities = getPossibleTurns().size();
        ControlElement myHead = new ControlElement(myPossibilities);

        int[] needed = findTurn(myHead);
        border.setBorder(needed[0] - Board.MY_WINNING_SIZE
                        , needed[0] + Board.MY_WINNING_SIZE
                        , needed[1] - Board.MY_WINNING_SIZE
                        , needed[1] + Board.MY_WINNING_SIZE);
        System.out.println(" mfhnmgmg,kbcmf, = " + countEs);

        return needed;
    }

    private int[] findTurn(ControlElement element) {
        int value;
        if (MY_MAX_LEVEL % 2 == 0) {
            value = - MY_MAX * 2;
        } else {
            value = 0;
        }
        element.setQuantity(value);
        searchTurns(element, 0);
        int[] needed = element.getOneOfHighest();
        System.out.println(" quantity = " + element.getQuantity());
        System.out.println(needed[0] + "  and  " + needed[1]);
        return needed;
    }

    private void searchTurns(ControlElement element, int level) {
        if ((level < MY_MAX_LEVEL) && (myPossibilities > level)) {
            int count = 0;
            while (element.getStatus() != 2) {
                ControlElement currentChild = addChild(element, level);
                if (currentChild.getFirstQuantity() < MY_MAX - 2000) {
                    searchTurns(currentChild, level + 1);
                } else {
                    currentChild.setQuantity(currentChild.getFirstQuantity());
                    minMaxing(currentChild, level);
                }
                if ((level != 0) && (element.getParent().myProbableSet)) {
                    int checkValue = element.getParent().getQuantity();
                    if (((level % 2 == 0) && (element.getQuantity() > checkValue))
                        || ((level % 2 == 1) && (element.getQuantity() < checkValue))) {
                        element.setStatus(2);
                    }
                }
                count++;
                myBoard[currentChild.getHeightOfAdded()][currentChild.getWidthOfAdded()] = 0;
            }
            
        }
        if (myPossibilities != level) {
            element.setStatus(2);
            if ((level < MY_MAX_LEVEL) && (level > 0)) {
                minMaxing(element, level);
            } else if (level > 0) {
                estimatingLeaf(element, level);
            }
        }
    }

    private void minMaxing(ControlElement element, int level) {
        if ((level % 2 == 0)
           && ((!element.getParent().myProbableSet)
              || (element.getQuantity() < element.getParent().getQuantity()))) {
            element.getParent().setQuantity(element.getQuantity());
        } else if ((level % 2 == 1)
                  && ((!element.getParent().myProbableSet)
                     || (element.getQuantity() > element.getParent().getQuantity()))) {
            element.getParent().setQuantity(element.getQuantity());
        }
    }

    private void estimatingLeaf(ControlElement element, int level) {
        if (level % 2 == 0) {
            int probableValue = element.setQuantity(- estimate(element.getHeightOfAdded()
                                                              , element.getWidthOfAdded()
                                                              , myOpponentsColour));
            if (probableValue < element.getParent().getQuantity()) {
                element.getParent().setQuantity(probableValue);
            }
        } else {
            int probableValue = element.setQuantity(estimate(element.getHeightOfAdded()
                                                            , element.getWidthOfAdded()
                                                            , myColour));
            if (probableValue > element.getParent().getQuantity()) {
                element.getParent().setQuantity(probableValue);
            }
        }
    }

    private ControlElement addChild(ControlElement element, int level) {
        Random r = new Random();
        int height;
        int width;
        int colour;

        do {
            height = r.nextInt(Board.MY_BOARD_SIZE);
            width = r.nextInt(Board.MY_BOARD_SIZE);
        } while (!coordinateAcceptance(height, width));
        ControlElement child = new ControlElement(height
                                                 , width
                                                 , myPossibilities - level);
        if (level % 2 == 0) {
            colour = myColour;
        } else {
            colour = myOpponentsColour;
        }
        element.addChild(child);
        child.setFirstQuantity(estimate(height, width, colour));
        myBoard[height][width] = colour;
        return child;
    }

    private boolean coordinateAcceptance(int height, int width) {
        return myBoard[height][width] == 0;
    }

    private boolean checkIfEmpty() {
        if (notEmpty) {
            return false;
        } else {
            int k = 0;
            int length = myBoard.length;
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if (myBoard[i][j] != 0) {
                        k++;
                    }
                }
            }
            notEmpty = true;
            return (k == 0);
        }
    }

    private LinkedList<Turn> getPossibleTurns() {
        LinkedList<Turn> turns = new LinkedList<Turn>();

        for (int i = border.myEast; i < border.myWest; i++) {
            for (int j = border.myNorth; j < border.mySouth; j++) {
                if (myBoard[i][j] == 0) {
                    turns.add(new Turn(i, j));
                }
            }
        }
        return turns;
    }

    private int estimate(int height, int width, int colour) {
        countEs++;
            int needed = checkDirection(1, 1, height, width, colour);
            needed += checkDirection(1, 0, height, width, colour);
            needed += checkDirection(0, 1, height, width, colour);
            needed += checkDirection(-1, 1, height, width, colour);
            return needed;
        }

        private int checkDirection(int vDirection
                                  , int hDirection
                                  , int height
                                  , int width
                                  , int colour) {
            int[] count= search(height, width, -vDirection, -hDirection, colour, 1, 1, true);
            int[] empty = search(height, width, vDirection, hDirection, colour, count[0], count[1], true);
            count = search(height, width, -vDirection, -hDirection, colour % 2 + 1, 0, 1, true);
            count = search(height, width, vDirection, hDirection, colour % 2 + 1, count[0], count[1], true);
            if ((empty[1] + count[1] - 1)< Board.MY_WINNING_SIZE) {
                return 0;
            }
            return getValue(empty[0], true) + getValue(count[0], false);
        }

        private int getValue(int count, boolean check) {
            int needed;

            switch(count) {
                case(0) :
                case(1) :
                    needed = 2;
                    break;
                case(2) :
                    needed = MY_MAX / 16;
                    break;
                case(3) :
                    if (check) {
                        needed = MY_MAX / 6;
                    } else {
                        needed = MY_MAX / 3 - MY_MAX / 10;
                    }
                    break;
                case(4):
                    if (check) {
                        needed = MY_MAX / 3;
                    } else {
                        needed = getValue(5, true) - MY_MAX / 500;
                    }
                    break;
                default :
                    needed = MY_MAX;
                    break;
            }
            return needed;
        }

        private int[] search(int height
                          , int width
                          , int heightChange
                          , int widthChange
                          , int colour
                          , int count
                          , int empty
                          , boolean checking) {
            if (count < Board.MY_WINNING_SIZE
               && checking
               && coordinateAccepted(height + heightChange, width + widthChange)
               && checkColour(colour, height + heightChange, width + widthChange)) {
                return search(height + heightChange
                                 , width + widthChange
                                 , heightChange
                                 , widthChange
                                 , colour
                                 , ++count, ++empty, checking);
            } else if ((count < Board.MY_WINNING_SIZE)
                      && coordinateAccepted(height + heightChange, width + widthChange)
                      && checkColour(0, height + heightChange, width + widthChange)) {
                return search(height + heightChange
                                 , width + widthChange
                                 , heightChange
                                 , widthChange
                                 , colour
                                 , count, ++empty, false);
            } else {
                return new int[] {count, empty};
            }
        }

        private boolean coordinateAccepted(int height, int width) {
            return !((height < 0) || (height >= myBoard.length)
                    || (width < 0) || (width >= myBoard.length));
        }

        private boolean checkColour(int colour
                                    , int height2
                                    , int width2) {
            return colour == myBoard[height2][width2];
        }
*/
    public static final int MY_MAX = 10;
}
