package ru.amse.gomoku.ui.gui;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.players.impl.PersonPlayer;
import ru.amse.gomoku.providers.IImageProvider;
import ru.amse.gomoku.providers.IIntellectProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class SelectPlayerView extends JDialog {
    public static final int START_INDEX = 0;

    public ImageIcon[] myPlayerImages;

    private JLabel player1IconLabel;
    private JLabel player2IconLabel;

    private IIntellectProvider myIntellectProvider;
    private IPlayer myFirstPlayer;
    private IPlayer mySecondPlayer;
    private IImageProvider myImageProvider;

    private GomokuFrame myFrame;

    public SelectPlayerView(GomokuFrame frame
                           , IIntellectProvider intellectProvider
                           , IImageProvider imageProvider) {
        super(frame, "Select The Players", true);

        myFrame = frame;
        myImageProvider = imageProvider;
        myIntellectProvider = intellectProvider;
        addWidgets();

        Point x = myFrame.getLocationOnScreen();
        setLocation((int)(x.getX() + 80),(int)(x.getY() + 100));
        setSize(200, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void addWidgets() {
        JPanel main = new JPanel(new BorderLayout());

        JPanel players = new JPanel(new GridLayout(2, 2, 10, 10));
        players.setBorder(BorderFactory.createEmptyBorder());

        JPanel list1 = new JPanel();
        JPanel list2 = new JPanel();
        list1.add(new JLabel("<html><b>White</b></html>"), BorderLayout.NORTH);
        list2.add(new JLabel("<html><b>Black</b></html>"), BorderLayout.NORTH);

        String[] namesProvided = myIntellectProvider.getAllNames();
        final String[] names = new String[myIntellectProvider.size() + 1];
        System.arraycopy(namesProvided, 0, names, 1, namesProvided.length);
        names[0] = "Person";

        final JComboBox box = new JComboBox(names);
        box.setSelectedIndex(START_INDEX);
        box.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                if ("comboBoxChanged".equals(event.getActionCommand())) {
                    ImageIcon icon;
                    if (box.getSelectedIndex() > 0) {
                        icon = myIntellectProvider
                               .getPlayerImage((String) box.getSelectedItem());
                    } else {
                        icon = myPlayerImages[0];
                    }
                    if (icon == null) {
                        icon = myPlayerImages[box.getSelectedIndex()
                                             % IImageProvider.NUM_PLAYER_IMAGES];
                    }
                    player1IconLabel.setIcon(icon);
	            }
            }
        });

        final JComboBox box2 = new JComboBox(names);
        box2.setSelectedIndex(START_INDEX);
        box2.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
             	if ("comboBoxChanged".equals(event.getActionCommand())) {
                    ImageIcon icon;
                    if (box2.getSelectedIndex() > 0) {
                        icon = myIntellectProvider
                               .getPlayerImage((String) box2.getSelectedItem());
                    } else {
                        icon = myPlayerImages[0];
                    }
                    if (icon == null) {
                        icon = myPlayerImages[box2.getSelectedIndex()
                                             % IImageProvider.NUM_PLAYER_IMAGES];
                    }
                    player2IconLabel.setIcon(icon);
	            }
            }
        });
        list1.add(box, BorderLayout.CENTER);
        list2.add(box2, BorderLayout.CENTER);

        myPlayerImages = myImageProvider.getPlayerImages();
        player1IconLabel = new JLabel();
        setLabelPosition(player1IconLabel);

        player2IconLabel = new JLabel();
        setLabelPosition(player2IconLabel);

        players.add(list1);
        players.add(list2);
        players.add(player1IconLabel);
        players.add(player2IconLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myFrame.setInterrupted(false);
                SelectPlayerView.this.dispose();
            }
        });

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IPlayer player;
                if (box.getSelectedIndex() == 0) {
                    player = createPerson();
                } else {
                    player = myIntellectProvider.getPlayer(names[box.getSelectedIndex()]);
                    if (player == null) {
                        JOptionPane.showMessageDialog(myFrame, "couldn load the player"
                                                     + names[box.getSelectedIndex()]);
                        return;
                    }
                }

                player.setName(names[box.getSelectedIndex()]);
                player.setColour(IBoard.DIB_COLOUR_FIRST);
                myFirstPlayer = player;

                if (box2.getSelectedIndex() == 0) {
                    player = createPerson();
                } else {
                    player = myIntellectProvider.getPlayer(names[box2.getSelectedIndex()]);
                    if (player == null) {
                        JOptionPane.showMessageDialog(myFrame, "couldn load the player"
                                                     + names[box2.getSelectedIndex()]);
                        return;
                    }
                }

                player.setName(names[box2.getSelectedIndex()]);
                player.setColour(IBoard.DIB_COLOUR_SECOND);
                mySecondPlayer = player;

                if (myFirstPlayer.getName().equals(mySecondPlayer.getName())) {
                    myFirstPlayer.setName(myFirstPlayer.getName() + " - first ");
                    mySecondPlayer.setName(mySecondPlayer.getName() + " - second ");
                }
                myFrame.setPlayers(myFirstPlayer, mySecondPlayer);
                SelectPlayerView.this.dispose();
            }
        });
        buttonPanel.add(ok);
        buttonPanel.add(cancel);

        main.add(buttonPanel,BorderLayout.SOUTH);
        main.add(players, BorderLayout.CENTER);
        this.add(main);
    }

    private IPlayer createPerson() {
        return new PersonPlayer(null
                               , IBoard.DIB_COLOUR_FIRST
                               , myImageProvider.getPersonImage()
                               , myFrame.getBoardView()
                               , myFrame);
    }

    private void setLabelPosition(JLabel locate) {

        locate.setPreferredSize(new Dimension(10, 10));
        locate.setIcon(myPlayerImages[START_INDEX]);
        locate.setHorizontalAlignment(JLabel.CENTER);
	    locate.setVerticalAlignment(JLabel.CENTER);
	    locate.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }
}
