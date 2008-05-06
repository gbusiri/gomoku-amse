package ru.amse.gomoku.providers.impl;

import ru.amse.gomoku.providers.IImageProvider;

import javax.swing.*;
import java.net.URL;

public class ImageProvider implements IImageProvider {

    private ImageIcon[] myPlayerImages = new ImageIcon[NUM_PLAYER_IMAGES + 1];
    private ImageIcon[] myActionIcons = new ImageIcon[NUM_ACTION_ICONS];
    private ImageIcon myPersonImage;
    private int myDibNumber = 0;

    public ImageProvider() {
        loadPersonImage();
        loadDefaultPlayerImages();
        loadActionIcons();
    }

    public void loadDefaultPlayerImages() {

        myPlayerImages[0] = myPersonImage;

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
        myActionIcons[6] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/images/add.png")
                                        , "Add");
        myActionIcons[7] = new ImageIcon(ClassLoader
                                        .getSystemResource("ru/amse/gomoku/images/remove.png")
                                        , "Remove");
        String path = "ru/amse/gomoku/images/dib";
        String name = "Dib";
        for (int i = 1; i < 8; i++) {
            myActionIcons[i + 7] = new ImageIcon(ClassLoader
                                        .getSystemResource(path + i + ".png")
                                        , name + i);
            myDibNumber++;
        }
        myActionIcons[15] = new ImageIcon(ClassLoader
                                         .getSystemResource("ru/amse/gomoku/images/stop.png")
                                         , "Stop");
        myActionIcons[16] = new ImageIcon(ClassLoader
                                         .getSystemResource("ru/amse/gomoku/images/ok.png")
                                         , "Ok");
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

    public int getDibNumber() {
        return myDibNumber;
    }

    public ImageIcon getPersonImage() {
        return myPersonImage;
    }
}
