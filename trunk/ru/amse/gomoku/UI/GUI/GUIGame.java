package ru.amse.gomoku.UI.GUI;

import ru.amse.gomoku.PlayerProvider.IPlayerProvider;
import ru.amse.gomoku.PlayerProvider.Impl.PlayerProvider;
import ru.amse.gomoku.Players.Impl.PersonPlayer;
import ru.amse.gomoku.Players.Impl.AIPlayer;
import ru.amse.gomoku.Players.Impl.cleverPlayer.CleverPlayer;
import ru.amse.gomoku.Players.Impl.MyaiPlayer.MyAIPlayer;

import javax.swing.*;

/**
 *
 */
public class GUIGame {

    public static final byte MY_DEFAULT_COLOUR = 1;

    public static void start() {
        try {
	        UIManager.setLookAndFeel(
		    UIManager.getSystemLookAndFeelClassName());
	    } catch(Exception e) {}
        
        IPlayerProvider playerProvider = new PlayerProvider();
        GomokuFrame gameFrame = new GomokuFrame(playerProvider);

        try {
            playerProvider.registerPlayer("Person"
                                     , new PersonPlayer("Person"
                                                           , MY_DEFAULT_COLOUR
                                                           , gameFrame.myBoardView
                                                           , gameFrame));
        } catch (IllegalAccessException e) {}
        try {
            playerProvider.registerPlayer("Intellect"
                                     , new MyAIPlayer("Intellect"
                                                         , MY_DEFAULT_COLOUR));
        } catch (IllegalAccessException e) {}
        try {
            playerProvider.registerPlayer("Strange"
                                     , new CleverPlayer("Strange"
                                                         , MY_DEFAULT_COLOUR));
        } catch (IllegalAccessException e) {}
        try {
            playerProvider.registerPlayer("Interesting"
                                     , new AIPlayer("Interesting"
                                                         , MY_DEFAULT_COLOUR));
        } catch (IllegalAccessException e) {}
        
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }
}