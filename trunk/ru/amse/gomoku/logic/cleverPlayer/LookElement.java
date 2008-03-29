package ru.amse.gomoku.logic.cleverPlayer;

import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 */
class LookElement {

    static final boolean ALLOW_RANDOMIZING = false;

    int myFirstQuantity;
    int myProbableQuantity;

    /**
     * becomes true when we set firstQuantity for the first time.
     * remains true all the time.
     */
    boolean myProbableSet;

    /**
     * reference to the previous turn.
     */
    LookElement myParent;

    /**
     * maximum number of children cuurent object can have.
     */
    int myMaxNumberOfChildren;
    ArrayList<LookElement> myChildren;

    /**
     * shows whether this element is totally checked.
     * it s 0 in the beginning, 1 just when we start checking it,
     * 2 when we finish checking all the children needed to be checked.
     */
    int myStatus;

    /**
     * vertical coordinate of the turn.
     */
    int myHeightOfAdded;

    /**
     * horizontal coordinate of the turn.
     */
    int myWidthOfAdded;

    LookElement(int height, int width, int max, int firstQuantity) {
        myMaxNumberOfChildren = max;
        myChildren = new ArrayList<LookElement>(myMaxNumberOfChildren);        
        myStatus = 0;
        myProbableSet = false;
        myHeightOfAdded = height;
        myWidthOfAdded = width;
        myFirstQuantity = firstQuantity;
    }

    LookElement(int max) {
        this(-1, -1, max, 0);
    }

    public boolean isProbableSet() {
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

    /**
     * sorts children.
     *
     * @param choose - defines the type of sorting: descending or ascending.
     */
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
            return myChildren.get(i).myFirstQuantity
                   < myChildren.get(j).myFirstQuantity;
        }
        return myChildren.get(i).myFirstQuantity
               > myChildren.get(j).myFirstQuantity;
    }

    private void change(int j, int i) {
         LookElement tree = myChildren.get(j);
         myChildren.set(j, myChildren.get(i));
         myChildren.set(i, tree);
    }

    /**
     * finds first maximum element cause others with the same quantity may be worse.
     * we definitely know that others a not better.
     *
     * @return first maximum element
     */
    private LookElement getHighestElement() {
        Random r = new Random();
        LookElement t = new LookElement(0);
        int max = - Looker.MY_MAX * 2;

        for (LookElement tree : myChildren) {
            if (max < tree.getQuantity()) {
                max = tree.getQuantity();
                t = tree;
                /*
                //to be deleted................
                    S ystem.out.println("    max is " + max);
                */
            }
        }
        return t;
    }

    /**
     *
     * @return LookElement which has one of the highest estimatedQuantity.
     */
    int[] getOneOfHighest() {
        LookElement t = getHighestElement();
        return new int[] {t.myHeightOfAdded, t.myWidthOfAdded};
    }

    void disposeChildren() {
        if (myChildren != null) {
            for (LookElement look : myChildren) {
                look.disposeChildren();
                look.myParent = null;
            }
        }
        this.myParent = null;
        this.myChildren = null;
    }

    //to be deleted................?????????????
    int getHighestQuantity() {
        return getHighestElement().getQuantity();
    }

    //to be deleted................
    public void printChildren() {
        for (LookElement look : myChildren) {
            System.out.println(" height = " + look.myHeightOfAdded
                              + "  width = " + look.myWidthOfAdded
                              + "  quantity = " + look.myProbableQuantity);
        }
    }
}
