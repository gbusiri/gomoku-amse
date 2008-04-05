package ru.amse.gomoku.Players.Impl.MyaiPlayer;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 */
class MyElement {

    static final boolean ALLOW_RANDOMIZING = true;
    boolean myProbableSet = false;
    final byte myHeightOfAdded;
    final byte myWidthOfAdded;
    final int myFirstQuantity;
    MyElement myParent;
    int myMaxNumberOfChildren;
    private LinkedList<MyElement> myChildren;
    private int myProbableQuantity;
    private byte myStatus;

    MyElement(byte height, byte width, int max, int firstQuantity) {
        myChildren = new LinkedList<MyElement>();
        myHeightOfAdded = height;
        myWidthOfAdded = width;
        myMaxNumberOfChildren = max;
        myFirstQuantity = firstQuantity;
    }

    MyElement(int max) {
        this((byte)(-1),(byte)(-1), max,(byte)(0));
    }

    void addChild(MyElement element) {
        myChildren.add(element);
        checkIfMaxReached();
        element.setParent(this);
    }

    private void checkIfMaxReached() {
        if (myChildren.size() >= myMaxNumberOfChildren) {
            setStatus((byte)(2));
        }
    }

    int getStatus() {
        return myStatus;
    }

    void setStatus(byte status) {
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

    private void setParent(MyElement parent) {
        myParent = parent;
    }

    private MyElement getHighestElement() {
        Random r = new Random();
        MyElement t = new MyElement(0);
        int max = - Looker.MY_MAX * 2;

        for (MyElement tree : myChildren) {
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

    byte[] getOneOfHighest() {
        MyElement t = getHighestElement();
        return new byte[] {t.myHeightOfAdded, t.myWidthOfAdded};
    }

    int getHighestQuantity() {
        return getHighestElement().getQuantity();
    }
}
