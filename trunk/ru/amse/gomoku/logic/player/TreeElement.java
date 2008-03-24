package ru.amse.gomoku.logic.player;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 28.02.2008
 * Time: 12:30:39
 * To change this template use File | Settings | File Templates.
 */
class TreeElement {

    static final boolean ALLOW_RANDOMIZING = true;
    private final int myHeightOfAdded;
    private final int myWidthOfAdded;
    private TreeElement[] myChildren;
    private int myQuantity;
    private int myCurrentChild;
    private int myStatus;

    TreeElement(int estimatedQuantity
                , int height
                , int width) {
        myStatus = 0;
        myQuantity = estimatedQuantity;
        myHeightOfAdded = height;
        myWidthOfAdded = width;
    }

    TreeElement(int estimatedQuantity) {
        this(estimatedQuantity, -1, -1);
    }

    TreeElement getNext() {
        if (myCurrentChild > 0) {
            myStatus = 1;
        } else {
            myStatus = 2;
        }
        myCurrentChild--;
        return myChildren[myCurrentChild + 1];
    }

    private TreeElement getHighestElement() {
        Random r = new Random();
        TreeElement t = new TreeElement(0);
        int max = -1;

        for (TreeElement tree : myChildren) {
            if ((max < tree.getQuantity())
               || (((ALLOW_RANDOMIZING)
                       && (max == tree.getQuantity()))
                       && (r.nextBoolean()))) {
                max = tree.getQuantity();
                t = tree;
            }
        }
        return t;
    }
/*System.out.println(" max =" + max + "  height=" + tree.getHeightOfAdded() + "  width=" + tree.getWidthOfAdded());
*/
    int[] getOneOfHighest() {
        TreeElement t = getHighestElement();
        return new int[] {t.getHeightOfAdded(), t.getWidthOfAdded()};
    }

    int getHighestQuantity() {
        return getHighestElement().getQuantity();
    }

    private TreeElement getLowestElement() {
        int min = 0;
        Random r = new Random();
        TreeElement t = new TreeElement(0);

        for (TreeElement tree : myChildren) {
            if ((min > tree.getQuantity())
               || ((ALLOW_RANDOMIZING)
                  && (min == tree.getQuantity())
                  && (r.nextBoolean()))) {
                min = tree.getQuantity();
            }
        }
        return t;
    }

    int[] getOneOfLowest() {
        TreeElement t = getLowestElement();
        return new int[] {t.getHeightOfAdded(), t.getWidthOfAdded()};
    }

    int getLowestQuantity() {
        return getLowestElement().getQuantity();
    }

    /*
     * assuming that no child is null
     */
    private void sortChildren() {
        int length = myChildren.length;

        if (length > 1) {
            Random r = new Random();
            for (int i = 0; i < length; i++) {
                for (int j = length - 1; j > i; j--) {
                    if ((myChildren[j - 1].getQuantity() > myChildren[j].getQuantity())
                       ||((ALLOW_RANDOMIZING)
                          && (myChildren[j - 1].getQuantity() == myChildren[j].getQuantity())
                          && (r.nextBoolean()))) {
                        change(j - 1, j);
                    }
                }
            }
        }
    }

    private void change(int j, int i) {
         TreeElement tree = myChildren[j];
         myChildren[j] = myChildren[i];
         myChildren[i] = tree;
    }

    void setStatus(int status) {
        myStatus = status;
    }

    int getStatus() {
        return myStatus;
    }

    int getQuantity() {
        return myQuantity;
    }

    void setQuantity(int quantity) {
        myQuantity = quantity;
    }

    void setChildren(TreeElement[] children) {
        myChildren = children;
        myCurrentChild = myChildren.length - 1;
        sortChildren();
    }

    int numberOfChildren() {
        return myChildren.length;
    }

    int getHeightOfAdded() {
        return myHeightOfAdded;
    }

    int getWidthOfAdded() {
        return myWidthOfAdded;
    }
}
