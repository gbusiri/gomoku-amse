package ru.amse.gomoku.logic.player;

import java.util.Random;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 04.03.2008
 * Time: 0:03:11
 * To change this template use File | Settings | File Templates.
 */
class ControlElement {

    static final boolean ALLOW_RANDOMIZING = false;
    boolean myProbableSet = false;
    private final int myHeightOfAdded;
    private final int myWidthOfAdded;
    private LinkedList<ControlElement> myChildren;
    private int myProbableQuantity;
    private int myFirstQuantity;
    private int myStatus;
    private int myMaxNumberOfChildren;
    private ControlElement myParent;


    ControlElement(int height, int width, int max) {
        myChildren = new LinkedList<ControlElement>();
        myHeightOfAdded = height;
        myWidthOfAdded = width;
        myMaxNumberOfChildren = max;
    }

    ControlElement(int max) {
        this(-1, -1, max);
    }

    void changeMax(int change) {
        myMaxNumberOfChildren += change;
        checkIfMaxReached();
    }

    private ControlElement getHighestElement() {
        Random r = new Random();
        ControlElement t = new ControlElement(0);
        int max = - ControlPlayer.MY_MAX * 2;

        for (ControlElement tree : myChildren) {
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

    int[] getOneOfHighest() {
        ControlElement t = getHighestElement();
        return new int[] {t.getHeightOfAdded(), t.getWidthOfAdded()};
    }

    int getHighestQuantity() {
        return getHighestElement().getQuantity();
    }

    private ControlElement getLowestElement() {
        int min = 0;
        Random r = new Random();
        ControlElement t = new ControlElement(0);

        for (ControlElement tree : myChildren) {
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
        ControlElement t = getLowestElement();
        return new int[] {t.getHeightOfAdded(), t.getWidthOfAdded()};
    }

    int getLowestQuantity() {
        return getLowestElement().getQuantity();
    }

    void addChild(ControlElement element) {
        myChildren.add(element);
        checkIfMaxReached();
        element.setParent(this);
    }

    private void checkIfMaxReached() {
        if (myChildren.size() >= myMaxNumberOfChildren) {
            setStatus(2);
        }
    }

    int getStatus() {
        return myStatus;
    }

    void setStatus(int status) {
        myStatus = status;
    }

    int getQuantity() {
        return myProbableQuantity;
    }

    int setQuantity(int probableQuantity) {
        myProbableQuantity = probableQuantity;
        myProbableSet = true;
        return myProbableQuantity;
    }

    int getHeightOfAdded() {
        return myHeightOfAdded;
    }

    int getWidthOfAdded() {
        return myWidthOfAdded;
    }

    ControlElement getParent() {
        return myParent;
    }

    private void setParent(ControlElement parent) {
        myParent = parent;
    }

    int getFirstQuantity() {
        return myFirstQuantity;
    }

    void setFirstQuantity(int firstQuantity) {
        myFirstQuantity = firstQuantity;
    }
}
