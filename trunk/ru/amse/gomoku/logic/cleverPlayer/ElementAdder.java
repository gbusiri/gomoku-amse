package ru.amse.gomoku.logic.cleverPlayer;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 14.03.2008
 * Time: 0:25:34
 * To change this template use File | Settings | File Templates.
 */
class ElementAdder {

    Border myBorder;

    ElementAdder(Border border) {
        myBorder = border;
    }

    /*LookElement*/
    void addElement(LookElement element, int level) {
        int[] needed = myBorder.getCoordinates(level);
        int height = needed[0];
        int width = needed[1];

        

    }
}
