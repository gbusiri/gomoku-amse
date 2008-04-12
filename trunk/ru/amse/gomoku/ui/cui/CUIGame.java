package ru.amse.gomoku.ui.cui;

import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.players.impl.AIPlayer;
import ru.amse.gomoku.players.impl.PPlayer;

/**
 * 
 */
public class CUIGame {

    public static void start() {

        IPlayer first = new AIPlayer("Intellect", (byte)1);
        IPlayer second = new PPlayer("Person", (byte)2);
        Game game = new Game(first, second);
        game.runGame();
    }

}
