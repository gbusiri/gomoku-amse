package ru.amse.gomoku.ui.gui.game;

import ru.amse.gomoku.gomokuio.XMLReader;
import ru.amse.gomoku.players.impl.AIPlayer;
import ru.amse.gomoku.players.impl.aiPlayer.MyAIPlayer;
import ru.amse.gomoku.players.impl.cleverPlayer.CleverPlayer;
import ru.amse.gomoku.providers.IIntellectProvider;
import ru.amse.gomoku.providers.impl.IntellectProvider;
import ru.amse.gomoku.ui.gui.view.GomokuFrame;

import javax.swing.*;
import java.net.URL;

/**
 *
 */
public class GUIGame {

    public static final byte MY_DEFAULT_COLOUR = 1;
    public static XMLReader myReader;

    public static String ourPath = "settings.xml";

    public static void start() {
        try {
	        UIManager.setLookAndFeel(
		    UIManager.getSystemLookAndFeelClassName());
	    } catch(Exception e) {}

        String imageName = "ru/amse/gomoku/images/computer.png";
        URL url = ClassLoader.getSystemResource(imageName);
        ImageIcon icon = new ImageIcon(url, "computer");

        IIntellectProvider intellectProvider = new IntellectProvider();
        try {
            intellectProvider.registerPlayer("Intellect"
                                     , new MyAIPlayer("Intellect"
                                                     , MY_DEFAULT_COLOUR));
        } catch (IllegalAccessException e) {}
        try {
            intellectProvider.registerPlayer("Strange"
                                     , new CleverPlayer("Strange"
                                                       , MY_DEFAULT_COLOUR));
        } catch (IllegalAccessException e) {}
        try {
            intellectProvider.registerPlayer("Interesting"
                                     , new AIPlayer("Interesting"
                                                   , MY_DEFAULT_COLOUR, icon));
        } catch (IllegalAccessException e) {}

        try {
            intellectProvider.registerPlayer("Special"
                                     , new MyAIPlayer("Special"
                                                     , MY_DEFAULT_COLOUR));
        } catch (IllegalAccessException e) {}

        myReader = new XMLReader(ourPath);
        myReader.parse();

        GomokuFrame gameFrame = new GomokuFrame(intellectProvider);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }
}
