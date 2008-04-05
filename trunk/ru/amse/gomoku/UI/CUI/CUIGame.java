package ru.amse.gomoku.UI.CUI;

import ru.amse.gomoku.Players.Impl.PPlayer;
import ru.amse.gomoku.Players.Impl.cleverPlayer.CleverPlayer;
import ru.amse.gomoku.Players.IPlayer;

/**
 * 
 */
public class CUIGame {

    public static void start() {

        IPlayer first = new CleverPlayer("Intellect", (byte)1);
        IPlayer second = new PPlayer("Person", (byte)2);
        Game game = new Game(first, second);
        game.runGame();
    }

}
