package ru.amse.gomoku.providers.impl;

import ru.amse.gomoku.providers.IImageProvider;

import javax.swing.*;
import java.net.URL;

public class ImageProvider implements IImageProvider {

    private ImageIcon[] myPlayerImages = new ImageIcon[NUM_PLAYER_IMAGES + 1];
    private ImageIcon[] myActionIcons = new ImageIcon[NUM_ACTION_ICONS];
    private ImageIcon myPersonImage;

    public ImageProvider() {
        loadPersonImage();
        loadDefaultPlayerImages();
        loadActionIcons();
    }

    public void loadDefaultPlayerImages() {

        myPlayerImages[0] = myPersonImage;
        for (int i = 1; i < NUM_PLAYER_IMAGES; i++) {
            String imageName = "ru/amse/gomoku/images/Player" + i + ".jpg";
            URL url = ClassLoader.getSystemResource(imageName);
            myPlayerImages[i] = new ImageIcon(url, "Player" + i + ".jpg");
        }

        String imageName = "ru/amse/gomoku/images/notAperson.png";
        myPlayerImages[1] = new ImageIcon(ClassLoader.getSystemResource(imageName));
        imageName = "ru/amse/gomoku/images/intellect.png";
        myPlayerImages[2] = new ImageIcon(ClassLoader.getSystemResource(imageName));
        imageName = "ru/amse/gomoku/images/intellect1.png";
        myPlayerImages[3] = new ImageIcon(ClassLoader.getSystemResource(imageName));
    }

    public void loadPersonImage() {
        URL url = ClassLoader.getSystemResource("ru/amse/gomoku/images/person.png");
        myPersonImage = new ImageIcon(url, "Person");
    }

    public void loadActionIcons() {
        myActionIcons[0] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/images/Run.png")
                                        , "NewGame");
        myActionIcons[1] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/images/undo.png")
                                        , "Undo");
        myActionIcons[2] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/images/play.png")
                                        , "Play");
        myActionIcons[3] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/images/pause.png")
                                        , "Pause");
         myActionIcons[4] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/images/draw.jpg")
                                        , "Draw");
         myActionIcons[5] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/images/ok.png")
                                        , "Win");
    }

    public ImageIcon[] getPlayerImages() {
        return myPlayerImages;
    }

    public ImageIcon getActionIcon(String name) {
        for (ImageIcon actionIcon: myActionIcons) {
            if (actionIcon.getDescription().equals(name)) {
                return actionIcon;
            }
        }
        return null;
    }

    public ImageIcon getPersonImage() {
        return myPersonImage;
    }
}
