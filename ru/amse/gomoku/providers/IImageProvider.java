package ru.amse.gomoku.providers;

import javax.swing.*;

/**
 * 
 */
public interface IImageProvider {

    public static final int NUM_PLAYER_IMAGES = 6;
    public static final int NUM_ACTION_ICONS = 7;

    public void loadDefaultPlayerImages();

    public void loadPersonImage();

    public void loadActionIcons();

    public ImageIcon[] getPlayerImages();

    public ImageIcon getPersonImage();

    public ImageIcon getActionIcon(String name);
}
