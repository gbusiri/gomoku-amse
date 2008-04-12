package ru.amse.gomoku.players.impl.aiPlayer;

import ru.amse.gomoku.board.IBoard;

/**
 *
 */
class Looker {

    static final int MY_MAX = 1000000;
    private byte myOpponentsColour;
    private byte myColour;
    final byte MY_MAX_LEVEL;
    Border myBorder;
    Estimator estimator;
    byte[][] myBoard;
    private int myPossibilities;

    Looker(byte colour1, byte colour2) {
        if (IBoard.MY_BOARD_SIZE > 20) {
            MY_MAX_LEVEL = 1;
        } else {
            MY_MAX_LEVEL = 1;
        }
        myBorder = new Border((byte)(IBoard.MY_WINNING_SIZE - 2), (byte)(IBoard.MY_BOARD_SIZE));
        estimator = new Estimator(myBoard);
        myColour = colour1;
        myOpponentsColour = colour2;
    }

    byte[] search(byte[][] board, byte[] coordinates) {
        myBoard = board;
        myBorder.addCoordinates(coordinates);
        myPossibilities = myBorder.myPossibilities;
         /*
        System.out.println(" my possibilities = " + myPossibilities);
           */
        MyElement head = new MyElement(myPossibilities);

        coordinates = findTurn(head);
        /*
        System.out.println(MessageFormat.format("  height = {0}   width = {1}", coordinates[0], coordinates[1]));
        */
        myBorder.addCoordinates(coordinates);
        return coordinates;
    }

    private byte[] findTurn(MyElement element) {
        searching(element, (byte)(0));
        return element.getOneOfHighest();
    }

    private void searching(MyElement element, byte level) {
        if ((level < MY_MAX_LEVEL) && (myPossibilities > level)) {
            int count = 0;
            while (element.getStatus() != 2) {
                MyElement currentChild = addChild(element, level);
                if ((currentChild.myFirstQuantity >  - MY_MAX)
                   && (currentChild.myFirstQuantity < MY_MAX)) {
                    searching(currentChild, (byte)(level + 1));
                } else {
                    int quantity = currentChild.myFirstQuantity;
                    if (level == 0) {
                        quantity *= 2;
                        element.setStatus((byte)2);
                    }
                    currentChild.setQuantity(quantity);
                    minMaxing(currentChild, (byte)(level + 1));
                    myBorder.reseting(level);
                }
                if ((level != 0) && (element.myParent.myProbableSet)) {
                    int checkValue = element.myParent.getQuantity();
                    if (((level % 2 == 0) && (element.getQuantity() >= checkValue))
                        || ((level % 2 == 1) && (element.getQuantity() <= checkValue))) {
                        element.setStatus((byte)2);
                    }
                } 
                count++;
                myBoard[currentChild.myHeightOfAdded][currentChild.myWidthOfAdded] = 0;
            }
            /*
            System.out.println("   count = " + count + "  level = " + level);
              */
        }
        if ((level > 0) && (myPossibilities != level)) {
            if (level == MY_MAX_LEVEL) {
                element.setQuantity(element.myFirstQuantity);
            }
            minMaxing(element, level);
        }
    }

    private void minMaxing(MyElement element, byte level) {
        int quantity = element.getQuantity();
        if ((level % 2 == 0)
           && ((!element.myParent.myProbableSet)
              || (quantity < element.myParent.getQuantity()))) {
            element.myParent.setQuantity(quantity);
        } else if ((level % 2 == 1)
                  && ((!element.myParent.myProbableSet)
                     || (quantity > element.myParent.getQuantity()))) {
            element.myParent.setQuantity(quantity);
        }
    }

    private MyElement addChild(MyElement element, byte level) {
        byte height;
        byte width;
        byte colour;
        int valueEstimated;

        do {
            byte[] needed = myBorder.getCoordinates(level);
            height = needed[0];
            width = needed[1];
        } while (!coordinateAcceptance(height, width));
        if (level % 2 == 0) {
            colour = myColour;
            valueEstimated = estimator.estimate(height, width, colour, myBoard);
        } else {
            colour = myOpponentsColour;
            valueEstimated = - estimator.estimate(height, width, colour, myBoard);
        }
        /*
        if (level > 0) {
            System.out.print("                       ");
        }
        if (level > 1) {
            System.out.print("                       ");            
        }
        System.out.println(MessageFormat.format(" height = {0}  width = {1}   estimated ={2}   colour ={3}   level ={4}"
                                                , height, width, valueEstimated, colour, level));  
          */
        MyElement child = new MyElement(height
                                       , width
                                       , myPossibilities - level - 1
                                       , valueEstimated);
        element.addChild(child);
        myBoard[height][width] = colour;
        return child;
    }

    private boolean coordinateAcceptance(byte height, byte width) {
        return myBoard[height][width] == 0;
    }
}
