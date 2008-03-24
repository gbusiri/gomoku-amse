package ru.amse.gomoku.logic.aiPlayer;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 05.03.2008
 * Time: 12:48:04
 * To change this template use File | Settings | File Templates.
 */
class MyElement {

    static final boolean ALLOW_RANDOMIZING = true;
    boolean myProbableSet = false;
    final int myHeightOfAdded;
    final int myWidthOfAdded;
    final int myFirstQuantity;
    MyElement myParent;
    int myMaxNumberOfChildren;
    private LinkedList<MyElement> myChildren;
    private int myProbableQuantity;
    private int myStatus;

    MyElement(int height, int width, int max, int firstQuantity) {
        myChildren = new LinkedList<MyElement>();
        myHeightOfAdded = height;
        myWidthOfAdded = width;
        myMaxNumberOfChildren = max;
        myFirstQuantity = firstQuantity;
    }

    MyElement(int max) {
        this(-1, -1, max, 0);
    }

    void addChild(MyElement element) {
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

    int[] getOneOfHighest() {
        MyElement t = getHighestElement();
        return new int[] {t.myHeightOfAdded, t.myWidthOfAdded};
    }

    int getHighestQuantity() {
        return getHighestElement().getQuantity();
    }
}
