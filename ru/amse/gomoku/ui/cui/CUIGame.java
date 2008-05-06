package ru.amse.gomoku.ui.cui;

import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.players.impl.PPlayer;
import ru.amse.gomoku.players.impl.aiPlayer.MyAIPlayer;

/**
 * 
 */
public class CUIGame {

    public static void start() {

        IPlayer first = new MyAIPlayer("Intellect", (byte)1);
        IPlayer second = new PPlayer("Person", (byte)2);
        Game game = new Game(first, second);
        game.runGame();
    }

}
