package ru.amse.gomoku.logic.cleverPlayer;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 13.03.2008
 * Time: 13:57:09
 * To change this template use File | Settings | File Templates.
 */
class LookElement {

    static final boolean ALLOW_RANDOMIZING = false;
    boolean myProbableSet;
    int myMaxNumberOfChildren;
    LinkedList<LookElement> myChildren;
    int myProbableQuantity;
    int myStatus;
    LookElement myParent;
    int myHeightOfAdded;
    int myWidthOfAdded;
    int myFirstQuantity;

    LookElement(int height, int width, int max, int firstQuantity) {
        myChildren = new LinkedList<LookElement>();
        myMaxNumberOfChildren = max;
        myStatus = 0;
        myProbableSet = false;
        myHeightOfAdded = height;
        myWidthOfAdded = width;
        myFirstQuantity = firstQuantity;
    }

    LookElement(int max) {
        this(-1, -1, max, 0);
    }

    public boolean probableSet() {
        return myProbableSet;
    }

    public int getHeight() {
        return myHeightOfAdded;
    }

    public int getWidth() {
        return myWidthOfAdded;
    }

    public void addChild(LookElement element) {
        myChildren.add(element);
        checkIfMaxReached();
        element.setParent(this);
    }

    private void setParent(LookElement parent) {
        myParent = parent;
    }

    private void checkIfMaxReached() {
        if (myChildren.size() >= myMaxNumberOfChildren) {
            setStatus(2);
        }
    }

    public int getStatus() {
        return myStatus;
    }

    public void setStatus(int status) {
        myStatus = status;
    }

    public int getQuantity() {
        return myProbableQuantity;
    }

    public int setQuantity(int probableQuantity) {
        myProbableQuantity = probableQuantity;
        myProbableSet = true;
        return myProbableQuantity;
    }

    void sortChildren(int choose) {
        int length = myChildren.size();
        boolean check = false;
        if (choose == 0) {
            check = true;
        }
        if (length > 1) {
            Random r = new Random();
            for (int i = 0; i < length; i++) {
                for (int j = length - 1; j > i; j--) {
                    if (check(j - 1, j, check)
                       ||((ALLOW_RANDOMIZING)
                          && (myChildren.get(j - 1).myFirstQuantity == myChildren.get(j).myFirstQuantity)
                          && (r.nextBoolean()))) {
                        change(j - 1, j);
                    }
                }
            }
        }
    }

    private boolean check(int i, int j, boolean check) {
        if (check) {
            return myChildren.get(j - 1).myFirstQuantity < myChildren.get(j).myFirstQuantity;
        }
        return myChildren.get(j - 1).myFirstQuantity > myChildren.get(j).myFirstQuantity;        
    }

    private void change(int j, int i) {
         LookElement tree = myChildren.get(j);
         myChildren.set(j, myChildren.get(i));
         myChildren.set(i, tree);
    }

    private LookElement getHighestElement() {
        Random r = new Random();
        LookElement t = new LookElement(0);
        int max = - Looker.MY_MAX * 2;

        for (LookElement tree : myChildren) {
            if ((max < tree.getQuantity())
               || (((ALLOW_RANDOMIZING)
                       && (max == tree.getQuantity()))
                       && (r.nextBoolean()))) {
                max = tree.getQuantity();
                t = tree;
                 /*
                System.out.println("    max is " + max);
                   */
            }
        }
        return t;
    }

    int[] getOneOfHighest() {
        LookElement t = getHighestElement();
        return new int[] {t.myHeightOfAdded, t.myWidthOfAdded};
    }

    int getHighestQuantity() {
        return getHighestElement().getQuantity();
    }
}
