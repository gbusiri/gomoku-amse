package ru.amse.gomoku.ui.gui.view;

import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.providers.IImageProvider;
import ru.amse.gomoku.ui.gui.tournament.StatisticsHolder;
import ru.amse.gomoku.ui.gui.tournament.Tournament;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public final class TournamentView extends JDialog {

    public static final int MY_PERCENT = 100;
    public static final int MY_GAME_NUMBER_ALLOWED = 40;

    private Tournament myTournament;
    private StatisticsHolder myHolder;
    private String[] myNames;

    private JProgressBar myProgress;
    private JTable myResults;
    private JTable myStatistics;
    private Timer myTimer;
    private IImageProvider myImages;

    private JButton myStop;
    private JButton myOk;
    private boolean isStopPressed = false;
    private boolean isLocked = false;

    private int myCurrentGame = 0;

    public TournamentView(GomokuFrame frame
                         , Tournament tournament
                         , IImageProvider images) {
        super(frame, "Tournament has started!", true);

        myTournament = tournament;
        myImages = images;
        myNames = myTournament.getAllPlayers();

        myProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, MY_PERCENT);
        myHolder = new StatisticsHolder(myNames);

        int height = frame.getHeight() / 3;
        if (myTournament.totalGameNumber() >= MY_GAME_NUMBER_ALLOWED) {
            height *= 2;
        } else {
            height += 20;
        }
        setSize(frame.getWidth() * 2 + 100, height);
        setLocation((int)frame.getLocation().getX() / 2 - 50
                   , (int)frame.getLocation().getY() + 50);
        makeTable();

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

        String[] statistics = new String[] {"Player1"
                                           , "vs    Player2"
                                           , "Player1 - wins"
                                           , "Player2 - wins"
                                           , "Draws"};
        int games = myTournament.getPlayersNumber();
        myStatistics = new JTable(
                         new Object[games * (games - 1) / 2][statistics.length]
                         , statistics);
        myStatistics.setEnabled(false);
        JTableHeader header2 = myStatistics.getTableHeader();
        header2.setReorderingAllowed(false);
        header2.setResizingAllowed(true);
        makeGameColumn();
    }

    private void addWidgets() {
        Container main = getContentPane();
        main.setLayout(new GridLayout(1, 2, 10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        buttonPanel.add(myStop);
        buttonPanel.add(myOk);

        JPanel statistics = new JPanel(new BorderLayout());
        statistics.add(new JScrollPane(myStatistics));

        JPanel actual = new JPanel(new BorderLayout());
        actual.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        actual.add(myProgress);

        JPanel result = new JPanel(new BorderLayout());
        result.add(new JScrollPane(myStatistics));

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        progressPanel.add(actual, BorderLayout.NORTH);
        progressPanel.add(result, BorderLayout.CENTER);
        progressPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel results = new JPanel(new BorderLayout());
        results.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        results.add(new JScrollPane(myResults));

        main.add(progressPanel, BorderLayout.NORTH);
        main.add(results);
    }

    private void addButtons() {
        myStop = new JButton("stop", myImages.getActionIcon("Stop"));
        myStop.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                isStopPressed = true;
                myStop.setEnabled(false);
            }
        });

        myOk = new JButton("OK", myImages.getActionIcon("Ok"));
        myOk.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                TournamentView.this.dispose();
            }
        });
        myOk.setEnabled(false);
    }

    private void addTimer() {

        final int totalGames = myTournament.totalGameNumber();
        myTimer = new Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int progress = myTournament.getGame();

                myProgress.setValue(progress * MY_PERCENT / totalGames);
                createStatistics();
                if ((progress == totalGames) || isStopPressed) {
                    myTimer.stop();
                }
            }
        });
        myTimer.start();
    }

    public synchronized void setWinner(boolean withWin
                                      , IPlayer first
                                      , IPlayer second
                                      , IPlayer winner) {
        setLock();
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
            if (myCurrentGame == myResults.getRowCount()) {
                myStop.setEnabled(false);
                myOk.setEnabled(true);
                createStatistics();
            }
            myHolder.addGameResult(withWin
                                  , first.getName()
                                  , second.getName()
                                  , winner.getName());
        }
        unLock();
    }

    private void createStatistics() {
        for (int i = 0; i < myStatistics.getRowCount(); i++) {
            Object ob1 = myStatistics.getValueAt(i, 0);
            Object ob2 = myStatistics.getValueAt(i, 1);
            String first;
            String second;
            if ((ob1 instanceof String) && (ob2 instanceof String)) {
                first = (String)ob1;
                second = (String)ob2;
                myStatistics.setValueAt(myHolder.getWins(first, second), i, 2);
                myStatistics.setValueAt(myHolder.getWins(second, first), i, 3);
                myStatistics.setValueAt(myHolder.getDraws(first, second), i, 4);
            }
        }
    }

    private void makeGameColumn() {
        int total = 0;

        for (int i = 0; i < myNames.length; i++) {
            for (int j = i + 1; j < myNames.length; j++) {
                myStatistics.setValueAt(myNames[i], total, 0);
                myStatistics.setValueAt(myNames[j], total, 1);
                total++;
            }
        }
        if (myNames.length == 1) {
            myStatistics.setValueAt(myNames[0] + " first", 0, 0);
            myStatistics.setValueAt(myNames[0] + " second", 0, 1);
        }
    }

    public void setLock() {
        isLocked = true;
    }

    private void unLock() {
        isLocked = false;
    }

    public boolean tryLock() {
        return isLocked;
    }
}
