package ru.amse.gomoku.logic.player;

import ru.amse.gomoku.logic.board.Board;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 27.02.2008
 * Time: 11:30:18
 * To change this template use File | Settings | File Templates.
 */
public class AIPlayer extends Player {

    public static final int MY_MAX = 500000;
    private final int myMaxLevel = 1;
    private int[][] myBoard;
    private final int myOpponentsColour;

    private int[] myTurn;

    public AIPlayer(String name, int colour) {
        super(name, true, colour);
        myOpponentsColour = colour % 2 + 1;
    }

    public void makeNextTurn(int[][] board, int[] coordinates) {
        myBoard = board;
        if (checkIfEmpty()) {
            myTurn = new int[]{Board.MY_BOARD_SIZE / 2, Board.MY_BOARD_SIZE / 2};
        } else {
            TreeElement myHead = new TreeElement(0);
            myTurn = findTurn(myHead);
        }
    }

    public int[] giveNextTurn() {
        return myTurn;
    }

    private boolean checkIfEmpty() {
        int k = 0;
        int length = myBoard.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (myBoard[i][j] != 0) {
                    k++;
                }
            }
        }
        return (k == 0);
    }

    private int[] findTurn(TreeElement element) {
        if (element.getStatus() == 0) {
            searchTurns(element, 0);
        }
        int[] needed;
        needed = element.getOneOfHighest();
        System.out.println(needed[0] + "  and  " + needed[1]);     
        return needed;
    }

    private void searchTurns(TreeElement element, int level) {    
        if (level < myMaxLevel) {
            addChildren(element, level);
            if (element.numberOfChildren() < 1) {
                element.setStatus(2);                
            }
            int height;
            int width;
            while (element.getStatus() != 2) {
                TreeElement currentChild = element.getNext();
                height = currentChild.getHeightOfAdded();
                width = currentChild.getWidthOfAdded();
                setDibByLevel(level, height, width);
                searchTurns(currentChild, level + 1);
                myBoard[height][width] = 0;
            }
        }
        element.setStatus(2);
        if (level != myMaxLevel) {
            element.setQuantity(element.getHighestQuantity());
        }
    }

    private void setDibByLevel(int level, int height, int width) {
         if (level % 2 == 0) {
             myBoard[height][width] = myColour;
         } else {
             myBoard[height][width] = myOpponentsColour;
         }
    }

    private void addChildren(TreeElement parent, int level) {
        ArrayList<Turn> list = getPossibleTurns();
        int size = list.size();
        int height;
        int width;
        int estimated;
        TreeElement[] children = new TreeElement[size];
        int colour;
        if (level % 2 == 0) {
            colour = myColour;
        } else {
            colour = myOpponentsColour;
        }
        for (int i = 0; i < size; i++ ) {
            height = list.get(i).getHeight();
            width = list.get(i).getWidth();
            estimated = estimatingFunction(height, width, colour);
            children[i] = new TreeElement(estimated, height, width);
        }
        parent.setChildren(children);
    }

    private int estimatingFunction(int height, int width, int colour) {
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
                    needed = MY_MAX / 5;
                } else {
                    needed = MY_MAX / 2 - MY_MAX / 10;
                }
                break;
            case(4):
                if (check) {
                    needed = MY_MAX / 2;
                } else {
                    needed = getValue(5, true) - MY_MAX / 1000;
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
           && coordinateAcceptance(height + heightChange, width + widthChange)
           && checkColour(colour, height + heightChange, width + widthChange)) {
            return search(height + heightChange
                             , width + widthChange
                             , heightChange
                             , widthChange
                             , colour
                             , ++count, ++empty, checking);
        } else if ((count < Board.MY_WINNING_SIZE)
                  && coordinateAcceptance(height + heightChange, width + widthChange)
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

    private boolean coordinateAcceptance(int height, int width) {
        return !((height < 0) || (height >= myBoard.length)
                || (width < 0) || (width >= myBoard.length));
    }

    private boolean checkColour(int colour
                                , int height2
                                , int width2) {
        return colour == myBoard[height2][width2];
    }

    private ArrayList<Turn> getPossibleTurns() {
        ArrayList<Turn> turns = new ArrayList<Turn>();
        int length = myBoard.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (myBoard[i][j] == 0) {
                    turns.add(new Turn(i, j));
                }
            }
        }
        return turns;
    }
}
