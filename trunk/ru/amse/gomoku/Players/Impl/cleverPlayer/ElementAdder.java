package ru.amse.gomoku.Players.Impl.cleverPlayer;

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
