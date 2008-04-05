package ru.amse.gomoku.UI.GUI;

import ru.amse.gomoku.Board.IBoard;
import ru.amse.gomoku.ImageProvider.IImageProvider;
import ru.amse.gomoku.ImageProvider.Impl.ImageProvider;
import ru.amse.gomoku.PlayerProvider.IPlayerProvider;
import ru.amse.gomoku.Players.IPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class SelectingPlayerView extends JPanel {
    public static final int NUM_IMAGES = 5;
    public static final int START_INDEX = 0;

    public ImageIcon[] myPlayerImages;

    private JLabel player1IconLabel;
    private JLabel player2IconLabel;
    private GomokuFrame frame;

    private IPlayerProvider myPlayerProvider;
    private IPlayer myFirstPlayer;
    private IPlayer mySecondPlayer;
    private IImageProvider myImageProvider;

    public SelectingPlayerView(GomokuFrame frame
                              , IPlayerProvider playerProvider
                              , IImageProvider imageProvider) {
        this.frame = frame;
        myPlayerProvider = playerProvider;
        myImageProvider = imageProvider;

        JLabel title;
        title = new JLabel("<html><font color = blue> Click the \"Start\" button \n"
                           + " once you have selected the players.</font></html>"
                           , JLabel.CENTER);

        JPanel choicePanel = createDialogBox();
        choicePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(choicePanel, BorderLayout.CENTER);
    }

    private JPanel createDialogBox() {
        JPanel choice = new JPanel(new BorderLayout());
        JPanel pane = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton startButton;

        final String[] names = myPlayerProvider.getAllNames();

        final JComboBox box = new JComboBox(names);
        box.setSelectedIndex(START_INDEX);
        box.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
             	if ("comboBoxChanged".equals(event.getActionCommand())) {
	                player1IconLabel.setIcon(myPlayerImages[box.getSelectedIndex()
                                                    % ImageProvider.NUM_PLAYER_IMAGES]);
	            }
            }
        });

        final JComboBox box2 = new JComboBox(names);
        box2.setSelectedIndex(START_INDEX);
        box2.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
             	if ("comboBoxChanged".equals(event.getActionCommand())) {
	                player2IconLabel.setIcon(myPlayerImages[box2.getSelectedIndex()
                                                     % ImageProvider.NUM_PLAYER_IMAGES]);
	            }
            }
        });

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {

           public void actionPerformed(ActionEvent event) {
               IPlayer player;
               player = myPlayerProvider.getPlayer(names[box.getSelectedIndex()]);
               player.setName(names[box.getSelectedIndex()]);
               player.setColour(IBoard.DIB_COLOUR_FIRST);
               myFirstPlayer = player;

               player = myPlayerProvider.getPlayer(names[box2.getSelectedIndex()]);
               player.setColour(IBoard.DIB_COLOUR_SECOND);
               mySecondPlayer = player;

               if (myFirstPlayer.getName().equals(mySecondPlayer.getName())) {
                   myFirstPlayer.setName(myFirstPlayer.getName() + " - first ");
                   mySecondPlayer.setName(mySecondPlayer.getName() + " - second ");                   
               }

               frame.setPlayers(myFirstPlayer, mySecondPlayer);
               myFirstPlayer = null;
               mySecondPlayer = null;
           }
        });
        myPlayerImages = myImageProvider.getPlayerImages();

        player1IconLabel = new JLabel();
        setLabelPosition(player1IconLabel);

        player2IconLabel = new JLabel();
        setLabelPosition(player2IconLabel);

        JPanel panel1 = new JPanel();
        panel1.add(box);
        JPanel panel2 = new JPanel();
        panel2.add(box2);

        pane.add(panel1);
        pane.add(panel2);
        pane.add(player1IconLabel);
        pane.add(player2IconLabel);

        JPanel paneStart = new JPanel();
        paneStart.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        paneStart.add(startButton);

        choice.add(paneStart, BorderLayout.SOUTH);
        choice.add(pane, BorderLayout.CENTER);

        return choice;
    }

    private void setLabelPosition(JLabel locate) {

        locate.setPreferredSize(new Dimension(10, 10));
        locate.setIcon(myPlayerImages[START_INDEX]);
        locate.setHorizontalAlignment(JLabel.CENTER);
	    locate.setVerticalAlignment(JLabel.CENTER);
	    locate.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }
}