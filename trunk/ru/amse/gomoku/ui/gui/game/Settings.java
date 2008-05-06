/**
 * Created by IntelliJ IDEA.
 * User: Taalai
 * Date: Apr 28, 2008
 * Time: 1:46:01 AM
 * To change this template use File | Settings | File Templates.
 */
package ru.amse.gomoku.ui.gui.game;

public class Settings {
    private static Settings ourInstance = new Settings();
    private static boolean isCreated = false;

    public static Settings getInstance() {
        if (isCreated) {
            return null;
        }
        isCreated = true;
        return ourInstance;
    }

    private Settings() {
        

    }
}
