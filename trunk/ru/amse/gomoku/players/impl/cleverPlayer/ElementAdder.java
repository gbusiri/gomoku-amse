package ru.amse.gomoku.players.impl.cleverPlayer;

/**
 * 
 */
class ElementAdder {

    Border myBorder;

    ElementAdder(Border border) {
        myBorder = border;
    }

    /*
     * LookElement
     */
    void addElement(LookElement element, int level) {
        int[] needed = myBorder.getCoordinates(level);
        int height = needed[0];
        int width = needed[1];

        

    }
}
