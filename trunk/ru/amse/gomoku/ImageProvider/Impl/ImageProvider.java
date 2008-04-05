package ru.amse.gomoku.ImageProvider.Impl;

import ru.amse.gomoku.ImageProvider.IImageProvider;

import javax.swing.*;
import java.net.URL;

public class ImageProvider implements IImageProvider {

    public static final int NUM_PLAYER_IMAGES = 6;
    public static final int NUM_ACTION_ICONS = 7;

    public static final ImageIcon[] myPlayerImages = new ImageIcon[NUM_PLAYER_IMAGES];
    public static final ImageIcon[] myActionIcons = new ImageIcon[NUM_ACTION_ICONS];

    public ImageProvider() {
        loadPlayerImages();
        loadActionIcons();
    }

    public void loadPlayerImages() {

        URL urll = ClassLoader.getSystemResource("ru/amse/gomoku/Images/Player1.jpg");
        myPlayerImages[0] = new ImageIcon(urll);
        for (int i = 0; i < NUM_PLAYER_IMAGES; i++) {
            String imageName = "ru/amse/gomoku/Images/Player" + i + ".jpg";
            URL url = ClassLoader.getSystemResource(imageName);
            myPlayerImages[i] = new ImageIcon(url);
        }
    }

    public void loadActionIcons() {
        myActionIcons[0] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/Images/NewGame.jpg")
                                        , "NewGame");
        myActionIcons[1] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/Images/Undo.jpg")
                                        , "Undo");
        myActionIcons[2] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/Images/Continue.jpg")
                                        , "Continue..");
    }

    public ImageIcon[] getPlayerImages() {
        return myPlayerImages;
    }

    public ImageIcon[] getActionIcons() {
        return myActionIcons;
    }
}
