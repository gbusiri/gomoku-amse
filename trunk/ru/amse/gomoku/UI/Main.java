package ru.amse.gomoku.UI;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 24.02.2008
 * Time: 23:52:19
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        try {
	        UIManager.setLookAndFeel(
		    UIManager.getSystemLookAndFeelClassName());
	    } catch(Exception e) {
        }
        MyFrame gameFrame = new MyFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }
}
