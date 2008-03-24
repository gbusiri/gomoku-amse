package ru.amse.gomoku.UI;

import ru.amse.gomoku.logic.player.IPlayer;
import ru.amse.gomoku.logic.player.PersonPlayer;
import ru.amse.gomoku.logic.player.AIPlayer;
import ru.amse.gomoku.logic.aiPlayer.MyAIPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 23.03.2008
 * Time: 19:57:51
 * To change this template use File | Settings | File Templates.
 */
public class SelectPlayerView extends JPanel {

    static final String[] myPlayers = {"PersonPlayer", "Intellect", "CleverPlayer"};
    static final int numberOfPlayers = myPlayers.length;

    MyFrame frame;
    BoardView myBoard;

    public SelectPlayerView(MyFrame frame, BoardView boardView) {
        this.frame = frame;
        myBoard = boardView;
        JLabel title;
        title = new JLabel("Click the \"Start\" button"
                           + " once you have selected the players.",
                           JLabel.CENTER);

        JPanel choicePanel = createDialogBox();
        choicePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(choicePanel, BorderLayout.CENTER);
    }

    private JPanel createDialogBox() {
        JPanel choice = new JPanel(new BorderLayout());
        JPanel pane = new JPanel(new GridLayout(1, 2, 20, 20));
        JButton startButton = null;

        final ButtonGroup group1 = createGroup(pane);
        final ButtonGroup group2 = createGroup(pane);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            IPlayer first = null;
            IPlayer second = null;

            public void actionPerformed(ActionEvent event) {
                String command1 = group1.getSelection().getActionCommand();
                String command2 = group2.getSelection().getActionCommand();
                first = reactOnChoice(command1, 1);
                second = reactOnChoice(command2, 2);
                frame.setPlayers(first, second);
            }

            private IPlayer reactOnChoice(String command, int colour) {
                IPlayer player = null;

                if (command == myPlayers[0]) {
                    player = new PersonPlayer("Person", colour, myBoard);
                } else if (command == myPlayers[1]) {
                    player = new AIPlayer("Intellect", colour);
                } else if (command == myPlayers[2]) {
                    player = new MyAIPlayer("Clever", colour);
                }
                return player;
            }
        });

        choice.add(pane, BorderLayout.CENTER);
        choice.add(startButton, BorderLayout.SOUTH);
        return choice;
    }

    private ButtonGroup createGroup(JPanel pane) {
        ButtonGroup needed = new ButtonGroup();
        JRadioButton[] radioButtons = addButtons();

        for (int i = 0; i < numberOfPlayers; i++) {
            needed.add(radioButtons[i]);
        }
        pane.add(createChoisePanel(radioButtons));
        return needed;
    }

    private JPanel createChoisePanel(JRadioButton[] radioButtons) {
        int numChoices = radioButtons.length;

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        for (int i = 0; i < numChoices; i++) {
            box.add(radioButtons[i]);
        }

        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(box, BorderLayout.CENTER);
        return pane;
    }

    private JRadioButton[] addButtons() {
        JRadioButton[] radioButtons = new JRadioButton[numberOfPlayers];

        radioButtons[0] = new JRadioButton("<html><font color = green> Peson </font></html>");
        radioButtons[0].setActionCommand(myPlayers[0]);

        radioButtons[1] = new JRadioButton("<html><font color = green> Artificial </font></html>");
        radioButtons[1].setActionCommand(myPlayers[1]);

        radioButtons[2] = new JRadioButton("<html><font color = red> Strange </font></html>");
        radioButtons[2].setActionCommand( myPlayers[2]);

        radioButtons[0].setSelected(true);
        return radioButtons;
    }
}
