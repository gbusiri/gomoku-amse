package ru.amse.gomoku.ui.gui;

import ru.amse.gomoku.players.IPlayer;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class TournamentView extends JDialog {

    public static final int MY_PERCENT = 100;

    private Tournament myTournament;
    private JProgressBar myProgress;
    private JTable myResults;
    private Timer myTimer;

    private JButton myClose;
    private JButton myStop;
    private boolean isStopPressed = false;

    private int myCurrentGame = 0;

    public TournamentView(GomokuFrame frame, Tournament tournament) {
        super(frame, "Tournament has started!", true);
        myTournament = tournament;

        myProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, MY_PERCENT);
        makeTable();

        setSize(frame.getWidth(), frame.getHeight() / 3);
        setLocation((int)frame.getLocation().getX() , (int)frame.getLocation().getY() * 2);
        
        addButtons();
        addTimer();
        addWidgets();

        myTournament.setReady(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void makeTable() {
        String[] columnHeaders = new String[] {"Game"
                                              , "First Player"
                                              , "Second Player"
                                              , "Winner"};
        myResults = new JTable(
                      new Object[myTournament.totalGameNumber()][columnHeaders.length]
                      , columnHeaders);
        myResults.setEnabled(false);
        myResults.setFont(Font.decode("Serif"));

        JTableHeader header = myResults.getTableHeader();
        header.setResizingAllowed(true);
        header.setReorderingAllowed(false);
    }

    private void addWidgets() {
        JPanel progressPanel = new JPanel(new GridLayout(1, 2));
        JPanel buttonPanel = new JPanel();

        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        buttonPanel.add(myClose);
        buttonPanel.add(myStop);

        progressPanel.add(myProgress);
        progressPanel.add(buttonPanel);

        add(progressPanel, BorderLayout.NORTH);
        add(new JScrollPane(myResults), BorderLayout.CENTER);
    }

    private void addButtons() {
        myStop = new JButton("stop");
        myClose = new JButton("close");

        myClose.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                TournamentView.this.dispose();
            }
        });

        myStop.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                isStopPressed = true;
                myStop.setEnabled(false);
            }
        });
    }

    private void addTimer() {

        final int totalGames = myTournament.totalGameNumber();
        myTimer = new Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int progress = myTournament.getGame();

                myProgress.setValue(progress * MY_PERCENT / totalGames);
                if ((progress == totalGames) || isStopPressed) {
                    myTimer.stop();
                }
            }
        });
        myTimer.start();
    }

    public void setWinner(boolean withWin, IPlayer first, IPlayer second, IPlayer winner) {
        if (!isStopPressed) {
            String result = "Draw";

            if (withWin) {
                result = winner.getName();
            }
            myResults.setValueAt(myCurrentGame + 1, myCurrentGame, 0);
            myResults.setValueAt(first.getName(), myCurrentGame, 1);
            myResults.setValueAt(second.getName(), myCurrentGame, 2);
            myResults.setValueAt(result, myCurrentGame, 3);
            myCurrentGame++;
            if (myCurrentGame + 1 == myResults.getRowCount()) {
                myStop.setEnabled(false);
            }
        }
    }
}
