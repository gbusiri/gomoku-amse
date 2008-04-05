package ru.amse.gomoku.Players.Impl.cleverPlayer;

import ru.amse.gomoku.Board.IBoard;

import java.text.MessageFormat;

/**
 * the main part for searching one of the best moves.
 * using this class does not guarantee that move is perfect,
 * it is just not bad and sometimes even very good.
 */
class Looker {

    static final int MY_MAX = 1000000;
    private int myOpponentsColour;
    private int myColour;
    final int MY_MAX_LEVEL;
    final Border myBorder;
    final Estimator myEstimator;
    final Possibilities myPossibleTurns;

    /**
     * it is a mark for searching algorithm.
     * it becomes true as soon as we reached the max level.
     */
    boolean myEndReached = false;

    int[][] myBoard;
    private int myPossibilities;

    //to be deleted................
    private boolean printingNeeded = false;
    private boolean checkDone = false;

    Looker(int colour1, int colour2) {
        if (IBoard.MY_BOARD_SIZE > 20) {
            MY_MAX_LEVEL = 1;
        } else {
            MY_MAX_LEVEL = 1;
        }
        myBorder = new Border(IBoard.MY_WINNING_SIZE - 2, IBoard.MY_BOARD_SIZE);
        myPossibleTurns = new Possibilities(myBorder);
        myEstimator = new Estimator();
        myColour = colour1;
        myOpponentsColour = colour2;
    }

    int[] look(int[][] board, int[] coordinates) {
        myBoard = board;

        if (coordinates.length == 2) {
            myBorder.addCoordinates(coordinates);
        }
        myPossibilities = myBorder.myPossibilities;

        LookElement head = new LookElement(myPossibilities);
        int[] turn = lookForTurn(head);

        //to be deleted................
        if (printingNeeded) {
             System.out.println("    printing children after the modifications....");
            head.printChildren();
        }

        myEndReached = false;
        head.disposeChildren();

        //to be deleted................
        checkDone = false;

        myBorder.addCoordinates(turn);
        return turn;
    }

    int[] lookForTurn(LookElement head) {
        search(head, 0);

        return  head.getOneOfHighest();
    }

    private void search(LookElement element, int level) {
        if ((!myEndReached) && (myPossibilities > level)) {
            if (level + 1 == MY_MAX_LEVEL) {
                myEndReached = true;
            }
            firstSearch(element, level);

            //myBorder.reseting(level + 1);
        } else if (myPossibilities > level) {
            secondSearch(element, level);
        }
    }

    private void firstSearch(LookElement element, int level) {
        int total = myPossibilities - level;
        int nextLevel = level + 1;

        myPossibleTurns.resetCoordinates();
        for (int i = 0; i < total; i++) {
            if (level == 0) {
                LookElement currentChild = addChild(element, level);
                if (currentChild.myFirstQuantity > (MY_MAX / 9 * 10)) {
                    currentChild.setQuantity(currentChild.myFirstQuantity);
                    return;
                }
            } else {
                addChild(element, level);
            }
        }
        //to be deleted................
        if (printingNeeded && (level == 0)) {
            System.out.println("    printing children before....");
                element.printChildren();
        }

        element.sortChildren(level % 2);

        for (LookElement look : element.myChildren) {
            if ((level == 0) && (look.myFirstQuantity == 0)) {    //????????????????????????????????????
            } else {
                fillBoard(look.myHeightOfAdded, look.myWidthOfAdded, level);
                myBorder.setCoordinates(look.myHeightOfAdded, look.myWidthOfAdded, level);
                search(look, nextLevel);
                minMaxing(look, nextLevel);

                unfillBoard(look.myHeightOfAdded, look.myWidthOfAdded);
            }
        }
    }

    private void secondSearch(LookElement element, int level) {
        if (level < MY_MAX_LEVEL) {
            //to be deleted................
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
                //to be deleted................
                count++;

                unfillBoard(currentChild.myHeightOfAdded
                           , currentChild.myWidthOfAdded);
            }

            //to be deleted................
            if (printingNeeded) {
                System.out.println("   count = " + count + "  level = " + level);
            }
        } else {
            element.setQuantity(element.myFirstQuantity);
        }
        minMaxing(element, level);
    }

    /**
     * alpha - beta procedure.
     * compares quantity of child with its parent's.
     *
     * @param element - child
     * @param level - determines whoes turn it is now
     * @return true if currentChild is not perspective to explore
     *         , false otherwise.
     */
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
           && ((!element.myParent.isProbableSet())
              || (quantity < element.myParent.getQuantity()))) {

            //to be deleted................
            if (printingNeeded && element.myParent.isProbableSet()) {
                System.out.println("  elementsQuantity = " + quantity + "   parents = "
                        + element.myParent.getQuantity() + " level =" + level);
            }

            element.myParent.setQuantity(quantity);
        } else if ((level % 2 == 1)
                  && ((!element.myParent.isProbableSet())
                     || (quantity > element.myParent.getQuantity()))) {

            //to be deleted................
            if (printingNeeded && element.myParent.isProbableSet()) {
                System.out.println("  elementsQuantity = " + quantity + "   parents = "
                        + element.myParent.getQuantity() + " level =" + level);
            }

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

    /**
     * adds child with the first estimation to the given parent.
     *
     * @param element - parent.
     * @param level - level of the parent.
     * @return the added child.
     */
    private LookElement addChild(LookElement element, int level) {
        int height;
        int width;
        int colour;
        int valueEstimated;

        do {
            int[] needed;
            if (myEndReached) {
                needed = myBorder.getCoordinates(level);
            } else {

                //to be deleted................
                if ((level == 0) && (checkDone)) {
                    System.out.println("   colour =" + myColour + "//////////////////////////////////");
                    myPossibleTurns.print();
                }

                needed = myPossibleTurns.getNextPossibility();
            }
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

        //to be deleted................
        if (printingNeeded) {
            if (level > 0) {
                System.out.print("                       ");
            }
            if (level > 1) {
                System.out.print("                       ");
            }
            System.out.println(MessageFormat.format(" height = {0}  width = {1}   estimated ={2}   colour ={3}   level ={4}"
                                                    , height, width, valueEstimated, colour, level));
        }

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

    public void changeColour() {
        int y = myOpponentsColour;
        myOpponentsColour = myColour;
        myColour = y;
    }
}
