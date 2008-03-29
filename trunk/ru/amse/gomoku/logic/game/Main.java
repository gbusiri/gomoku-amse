package ru.amse.gomoku.logic.game;

import ru.amse.gomoku.logic.player.*;
import ru.amse.gomoku.logic.aiPlayer.MyAIPlayer;
import ru.amse.gomoku.logic.cleverPlayer.CleverPlayer;

import java.util.ArrayList;

/**
 * 
 */
public class Main {

    public static void main(String[] args) {

        IPlayer first = new CleverPlayer("Intellect", 1);
        IPlayer second = new PPlayer("Person", 2);
        Game game = new Game(first, second);
        game.runGame();
    }

}
