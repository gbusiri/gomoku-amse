package ru.amse.gomoku.logic.cleverPlayer;

import ru.amse.gomoku.logic.board.Board;
import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 13.03.2008
 * Time: 13:42:45
 * To change this template use File | Settings | File Templates.
 */
class Looker {

    static final int MY_MAX = 1000000;
    final int myOpponentsColour;
    final int myColour;
    final int MY_MAX_LEVEL;
    final Border myBorder;
    final Estimator myEstimator;
    final ElementAdder myAdder;

    boolean myEndReached = false;
    int[][] myBoard;
    private int myPossibilities;

    Looker(int colour1, int colour2) {
        if (Board.MY_BOARD_SIZE > 20) {
            MY_MAX_LEVEL = 1;
        } else {
            MY_MAX_LEVEL = 3;
        }
        myBorder = new Border(Board.MY_WINNING_SIZE - 2, Board.MY_BOARD_SIZE);
        myAdder = new ElementAdder(myBorder);
        myEstimator = new Estimator(myBoard);
        myColour = colour1;
        myOpponentsColour = colour2;
    }

    int[] look(int[][] board, int[] coordinates) {
        myBoard = board;
        myBorder.addCoordinates(coordinates);
        myPossibilities = myBorder.myPossibilities;
        LookElement head = new LookElement(myPossibilities);
        Possibilities possible = new Possibilities(board);

        int[] coor;
        int estimated;

        for (int i = 0; i < myPossibilities; i++) {
            coor = possible.getNextPossibility();
            estimated = myEstimator.estimate(coor[0], coor[1], myColour, board);
            if (estimated >= MY_MAX) {
                return coor;
            }
            head.addChild(new LookElement(coor[0]
                                         , coor[1]
                                         , myPossibilities - 1
                                         , estimated));
        }
        head.sortChildren(0);
        int[] turn = lookForTurn(head);
        myBorder.addCoordinates(turn);
        return turn;
    }

    int[] lookForTurn(LookElement head) {
        for (LookElement look : head.myChildren) {
            myBorder.setCoordinates(look.myHeightOfAdded, look.myWidthOfAdded, 0);
            search(look, 1);
            minMaxing(look, 1);
        }
        head.sortChildren(0);
        return new int[] {head.myChildren.get(0).myHeightOfAdded
                         , head.myChildren.get(0).myWidthOfAdded};
    }

    private void search(LookElement element, int level) {
        if ((!myEndReached) && (myPossibilities > level)) {
            if (level + 1 == MY_MAX_LEVEL) {
                myEndReached = true;
            }
            firstSearch(element, level);
        } else if (myPossibilities > level) {
            secondSearch(element, level);
        }
    }

    private void firstSearch(LookElement element, int level) {
        int total = myPossibilities - level;
        int nextLevel = level + 1;

        for (int i = 0; i < total; i++) {
             addChild(element, level);
        }
        element.sortChildren(level % 2);

        for (LookElement look : element.myChildren) {
            fillBoard(look.myHeightOfAdded, look.myWidthOfAdded, level);
            myBorder.setCoordinates(look.myHeightOfAdded, look.myWidthOfAdded, level);
            search(look, nextLevel);
            minMaxing(look, nextLevel);
            unfillBoard(look.myHeightOfAdded, look.myWidthOfAdded);
        }
    }

    private void secondSearch(LookElement element, int level) {
         if (level < MY_MAX_LEVEL) {
            int count = 0;

            while (element.getStatus() != 2) {
                LookElement currentChild = addChild(element, level);
                fillBoard(currentChild.myHeightOfAdded
                         , currentChild.myWidthOfAdded
                         , level);

                if ((currentChild.myFirstQuantity >  - MY_MAX)
                   && (currentChild.myFirstQuantity < MY_MAX)) {
                    secondSearch(currentChild, level + 1);
                } else {
                    int quantity = currentChild.myFirstQuantity;

                    currentChild.setQuantity(quantity);
                    minMaxing(currentChild, level + 1);
                    myBorder.reseting(level);
                }
                if (checkIfNotNeededToContinue(element, level)) {
                    element.setStatus(2);
                }
                count++;
                unfillBoard(currentChild.myHeightOfAdded
                           , currentChild.myWidthOfAdded);
            }
            /*
            System.out.println("   count = " + count + "  level = " + level);
              */
        } else {
            element.setQuantity(element.myFirstQuantity);
        }
        minMaxing(element, level);
    }

    private boolean checkIfNotNeededToContinue(LookElement element, int level) {
        if (element.myParent.myProbableSet) {
            int checkValue = element.myParent.getQuantity();
            if (((level % 2 == 0)
                && (element.getQuantity() >= checkValue))
               || ((level % 2 == 1)
                  && (element.getQuantity() <= checkValue))) {
                return true;

            }
        }
        return false;
    }

    private void minMaxing(LookElement element, int level) {
        int quantity = element.getQuantity();

        if ((level % 2 == 0)
           && ((!element.myParent.probableSet())
              || (quantity < element.myParent.getQuantity()))) {
            element.myParent.setQuantity(quantity);
        } else if ((level % 2 == 1)
                  && ((!element.myParent.probableSet())
                     || (quantity > element.myParent.getQuantity()))) {
            element.myParent.setQuantity(quantity);
        }
    }

    private void fillBoard(int height, int width, int level) {
        int colour;
        
        if (level % 2 == 0) {
            colour = myColour;
        } else {
            colour = myOpponentsColour;
        }
        myBoard[height][width] = colour;
    }

    private void unfillBoard(int height, int width) {
        myBoard[height][width] = 0;
    }

    /*
    gets parent and its level
     */
    private LookElement addChild(LookElement element, int level) {
        int height;
        int width;
        int colour;
        int valueEstimated;

        do {
            int[] needed = myBorder.getCoordinates(level);
            height = needed[0];
            width = needed[1];
        } while (!coordinateAcceptance(height, width));
        if (level % 2 == 0) {
            colour = myColour;
            valueEstimated = myEstimator.estimate(height, width, colour, myBoard);
        } else {
            colour = myOpponentsColour;
            valueEstimated = - myEstimator.estimate(height, width, colour, myBoard);
        }

        if (level > 0) {
            System.out.print("                       ");
        }
        if (level > 1) {
            System.out.print("                       ");
        }
        System.out.println(MessageFormat.format(" height = {0}  width = {1}   estimated ={2}   colour ={3}   level ={4}"
                                                , height, width, valueEstimated, colour, level));
      
        LookElement child = new LookElement(height
                                       , width
                                       , myPossibilities - level - 1
                                       , valueEstimated);
        element.addChild(child);
        return child;
    }

    private boolean coordinateAcceptance(int height, int width) {
        return myBoard[height][width] == 0;
    }
}
