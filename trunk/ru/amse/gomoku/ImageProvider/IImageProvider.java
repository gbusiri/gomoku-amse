package ru.amse.gomoku.ImageProvider;

import javax.swing.*;

/**
 * 
 */
public interface IImageProvider {

    public void loadPlayerImages();

    public void loadActionIcons();

    public ImageIcon[] getPlayerImages();

    public ImageIcon[] getActionIcons();
}
