package ru.amse.gomoku.ui.gui;

import ru.amse.gomoku.providers.IImageProvider;
import ru.amse.gomoku.providers.IIntellectProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 */
public class SelectPlayersForTournamentView extends JDialog {

    private GomokuFrame owner;
    private JTextField myNumber;
    private String[] myPlayersChosen;
    private final String[] myAllAvailablePlayers;
    private JList myChosen;
    private final JList myPlayers;
    private IImageProvider myImages;

    private JButton myStart;

    public SelectPlayersForTournamentView(IIntellectProvider intellectProvider
                                         , GomokuFrame frame
                                         , IImageProvider images) {
        super(frame, "Select the players for tournament!", true);
        owner = frame;
        myAllAvailablePlayers = intellectProvider.getAllNames();
        myPlayers = new JList(myAllAvailablePlayers);
        myImages = images;

        setLocation(owner.getX(), owner.getY() + 150);
        setSize(owner.getWidth(), owner.getHeight() / 2);
        setResizable(false);
        setLayout(new BorderLayout());

        addComponents();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void addComponents() {
        JPanel main = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel selected = new JPanel(new BorderLayout());
        selected.setBorder(BorderFactory.createCompoundBorder
                           (BorderFactory.createTitledBorder("Players for Tournament")
                           , BorderFactory.createEmptyBorder(10, 10, 0, 10)));

        JPanel forSelection = new JPanel(new BorderLayout());
        forSelection.setBorder(BorderFactory.createCompoundBorder
                           (BorderFactory.createTitledBorder("Available Players")
                           , BorderFactory.createEmptyBorder(10, 10, 0, 10)));

        JButton add = new JButton(myImages.getActionIcon("Add"));
        add.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                addPlayers();
            }
        });
        JButton remove = new JButton(myImages.getActionIcon("Remove"));
        remove.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                removePlayers();
            }
        });

        JPanel forAdd = new JPanel();
        forAdd.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        forAdd.add(add);

        forSelection.add(forAdd, BorderLayout.SOUTH);
        forSelection.add(new JScrollPane(myPlayers));

        myPlayersChosen = new String[0];
        myChosen = new JList(myPlayersChosen);

        JPanel forRemove = new JPanel();
        forRemove.add(remove);
        selected.add(forRemove, BorderLayout.SOUTH);
        selected.add(new JScrollPane(myChosen));

        myNumber = new JTextField("1");

        JPanel mainButtons = new JPanel();
        mainButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainButtons.setLayout(new GridLayout(1, 2));
        addGameNumberView(mainButtons);
        addMainButtons(mainButtons);

        main.add(selected);
        main.add(forSelection);

        add(mainButtons, BorderLayout.SOUTH);
        add(main, BorderLayout.CENTER);
    }

    private void addGameNumberView(JPanel mainButtons) {
        JPanel main = new JPanel(new GridLayout(1, 2, 5, 5));
        main.add(myNumber);
        main.add(new JLabel("Number of Games"));
        mainButtons.add(main);
    }

    private void startGame() {
        int number;
        try {
            number = Integer.parseInt(myNumber.getText());
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this
                                         , "Number of games \n must be Integer type"
                                         , "Wrong data!"
                                         , JOptionPane.OK_OPTION);
            return;            
        }
        owner.setPlayersForTournament(myPlayersChosen, number);
    }

    private void addPlayers() {
        int[] forAdd =  myPlayers.getSelectedIndices();
        LinkedList<String> checked = new LinkedList<String>();
        String[] newPlayers;

        for (int add : forAdd) {
            boolean check = true;
            for (String playerChosen : myPlayersChosen) {
                if (playerChosen.equals(myAllAvailablePlayers[add])) {
                    check = false;
                    break;
                }
            }
            if (check) {
                checked.add(myAllAvailablePlayers[add]);
            }
        }
        newPlayers = Arrays.copyOf(myPlayersChosen
                                  , myPlayersChosen.length + checked.size());
        int i = myPlayersChosen.length;
        for (String check : checked) {
            newPlayers[i] = check;
            i++;
        }
        myPlayersChosen = newPlayers;
        myChosen.setListData(myPlayersChosen);
        if (myPlayersChosen.length > 0) {
            myStart.setEnabled(true);            
        }
    }

    private void removePlayers() {
        int[] forRemove = myChosen.getSelectedIndices();
        String[] newPlayers = new String[myPlayersChosen.length - forRemove.length];
        int i = 0;

        if (newPlayers.length != 0) {
            for (int j = 0; j < myPlayersChosen.length; j++) {
                boolean check = true;
                for (int remove : forRemove) {
                    if (j == remove) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    newPlayers[i] = myPlayersChosen[j];
                    i++;
                }
            }
        }
        myPlayersChosen = newPlayers;
        myChosen.setListData(myPlayersChosen);
        if (myPlayersChosen.length == 0) {
            myStart.setEnabled(false);
        }
    }

    private void addMainButtons(JPanel buttonPanel) {
        JPanel main = new JPanel(new GridLayout(1, 2, 5, 5));
        myStart = new JButton("Start");

        myStart.setEnabled(false);
        myStart.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                SelectPlayersForTournamentView.this.dispose();
            }
        });
        main.add(myStart);
        main.add(cancel);
        buttonPanel.add(main);
    }
}
