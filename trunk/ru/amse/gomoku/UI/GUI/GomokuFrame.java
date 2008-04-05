package ru.amse.gomoku.UI.GUI;

import ru.amse.gomoku.Board.IBoard;
import ru.amse.gomoku.Board.Impl.Board;
import ru.amse.gomoku.ImageProvider.IImageProvider;
import ru.amse.gomoku.ImageProvider.Impl.ImageProvider;
import ru.amse.gomoku.PlayerProvider.IPlayerProvider;
import ru.amse.gomoku.Players.IPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 */
public class GomokuFrame extends JFrame {

    public static final int myActualWidth;
    public static final int myActualHeight;

    public static final boolean ALLOW_STOP = false;

    static {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        myActualWidth = screenSize.height / 2;
        myActualHeight = myActualWidth * 9 / 8;
    }

    private IPlayerProvider myPlayerProvider;
    private IImageProvider myImageProvider;
    private ImageIcon[] myActionIcons;

    private Controller myGame = null;
    final IBoard myBoard;
    final BoardView myBoardView;
    private JDialog mySelectView;
    private JLabel myStatus;
    private JLabel myPreviousGames;
    private int countGames = 0;

    private boolean myGameFinished = false;
    private boolean myGameStarted = false;
    private boolean myGamePaused = false;
    private boolean isUndoNeeded = false;

    public GomokuFrame(IPlayerProvider playerProvider) {

        super("Gomoku game!");
        setSize(398, 550);
        setLocation(myActualWidth + 50, myActualHeight / 3);
        setResizable(false);

        myBoard = new Board();
        myBoardView = new BoardView();
        myPlayerProvider = playerProvider;
        myImageProvider = new ImageProvider();
        myActionIcons = myImageProvider.getActionIcons();
        addComponents();
    }

    private void addComponents() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        myBoardView.setSize(myActualWidth / 2
                         , myActualHeight / 2);

        JPanel status = new JPanel();
        status.setLayout(new BorderLayout());
        status.setBackground(Color.getHSBColor(30, 400, 100));
        status.setBorder(BorderFactory.createCompoundBorder(
                         BorderFactory.createTitledBorder("Status of the current game")
                        , BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        myStatus = new JLabel("Waiting for start....");
        myStatus.setHorizontalTextPosition(JLabel.CENTER);
        myPreviousGames = new JLabel(" total games played = 0");
        myPreviousGames.setHorizontalTextPosition(JLabel.CENTER);
        status.add(myStatus, BorderLayout.NORTH);
        status.add(myPreviousGames, BorderLayout.CENTER);

        JPanel pane = new JPanel();
        pane.setBackground(Color.getHSBColor(30,500, 100));
        pane.setBorder(BorderFactory.createCompoundBorder(
                       BorderFactory.createTitledBorder("Current Board!")
                       , BorderFactory.createEmptyBorder(5, 10, 10, 11)));
        pane.setLayout(new GridLayout(0, 1));
        pane.add(myBoardView);

        mainPanel.add(status, BorderLayout.NORTH);
        mainPanel.add(pane, BorderLayout.CENTER);

        JToolBar buttonPanel = new JToolBar();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 10));
        buttonPanel.setBackground(Color.getHSBColor(30, 400, 100));     
        buttonPanel.setFloatable(false);
        buttonPanel.setLayout(new GridLayout(1, 5));
        buttonPanel.add(new AbstractAction("New Game", myActionIcons[0]) {

            public void actionPerformed(ActionEvent event) {
                this.putValue(Action.SHORT_DESCRIPTION, "start a new game");
                if (myGame == null) {
                    startNewGame();
                } else if ((myGameFinished)) {
                    myBoard.refreshBoard();
                    myBoardView.refresh();
                    startNewGame();
                } else if (ALLOW_STOP) {
                    //  myGame.stop()
                    startNewGame();
                }
            }

            private void startNewGame() {
                createSelectPlayerView();
            }
        });
        buttonPanel.add(new JLabel());

        final JFrame current = this;
        buttonPanel.add(new AbstractAction("ChooseBoard") {

            public void actionPerformed(ActionEvent e) {
                createSelectColourDialog();
            }

            public void createSelectColourDialog() {
                JDialog d = new JDialog(current,"Colours!");
                final Color[] colours = new Color[] {Color.getHSBColor(20,8000,100)
                        , Color.getHSBColor(30, 317, 100)
                        , Color.getHSBColor(30, 317, 10)
                        , Color.getHSBColor(30, 317, 20)
                        , Color.getHSBColor(30, 317, 33)
                        , Color.getHSBColor(30, 317, 46)
                        , Color.getHSBColor(30, 317, 62)
                        , Color.getHSBColor(30, 317, 81)
                        , Color.getHSBColor(30, 400, 100)
                        , Color.getHSBColor(30, 500, 100)
                        , Color.getHSBColor(30, 301, 100)
                        , Color.getHSBColor(30, 302, 100)
                        , Color.getHSBColor(30, 303, 100)
                        , Color.getHSBColor(30, 304, 100)
                        , Color.getHSBColor(30, 305, 100)
                        , Color.getHSBColor(30, 306, 100)
                        , Color.getHSBColor(30, 307, 100)
                        , Color.getHSBColor(30, 308, 22)
                        , Color.getHSBColor(30, 308, 37)
                        , Color.getHSBColor(30, 308, 55)
                        , Color.getHSBColor(30, 309, 100)
                        , Color.getHSBColor(30, 310, 100)
                        , Color.getHSBColor(30, 311, 100)
                        , Color.RED
                        , Color.BLUE
                        , Color.CYAN
                        , Color.LIGHT_GRAY
                        , Color.GREEN
                        , Color.MAGENTA};

                final JComboBox box = new JComboBox(colours);
                box.addActionListener(new AbstractAction() {

                    public void actionPerformed(ActionEvent event) {
             	        if ("comboBoxChanged".equals(event.getActionCommand())) {
	                        myBoardView.setBackground(colours[box.getSelectedIndex()]);
	                    }
                    }
                });
                d.add(box);
                Point x = current.getLocationOnScreen();
                d.setLocation((int)(x.getX() + 50),(int)(x.getY() + 100));
                d.pack();
                d.setVisible(true);
            }
        });
        buttonPanel.add(new AbstractAction("undo", myActionIcons[1]) {
            public void actionPerformed(ActionEvent event) {
                this.putValue(Action.SHORT_DESCRIPTION, "undo last made move");
                myGamePaused = true;
                isUndoNeeded = true;
            }
        });
        buttonPanel.add(new AbstractAction("continue..", myActionIcons[2]) {
            public void actionPerformed(ActionEvent event) {
                this.putValue(Action.SHORT_DESCRIPTION
                             , "continue playing current game");
                myGamePaused = false;
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void setStatus(IPlayer currentPlayer) {
        myStatus.setText("it is " + currentPlayer.getName() + "'s turn now" );        
    }

    public void setStatusWait() {
        myStatus.setText("Waiting for start....");    
    }

    public void setPlayers(IPlayer first, IPlayer second) {

        //System.out.println(first.getName() + "  and   " + second.getName());
        myGame = new Controller(myBoardView
                                       , myBoard
                                       , this);
        myGame.setPlayers(first, second);
        mySelectView.setVisible(false);
        myGameStarted = true;
        myGameFinished = false;
    }

    public boolean isStarted() {
        return myGameStarted;
    }

    private void createSelectPlayerView() {
        if (mySelectView == null) {
            mySelectView = new JDialog(this, "Select the players");
        } else {
            mySelectView.setVisible(true);
        }
        Container contentPane = mySelectView.getContentPane();
        contentPane.add(new SelectingPlayerView(this, myPlayerProvider, myImageProvider));
        Point x = this.getLocationOnScreen();
        mySelectView.setLocation((int)(x.getX() + 80),(int)(x.getY() + 100));
        mySelectView.setSize(200, 200);
        //mySelectView.pack();
        mySelectView.setResizable(false);
        mySelectView.setVisible(true);
    }

    public void setGameFinished(boolean gameFinishedWithWin, IPlayer winner) {
        myGameFinished = true;
        myGameStarted = false;

        String message;
        String title;

        String lastWon;
        if (gameFinishedWithWin) {
            title = "There is a winner!";
            message = "!!" + winner.getName() + "!!";
            lastWon = "the winner is " + winner.getName();
        } else {
            title = "It is a Draw!";
            message = "Congaratulations to both of You";
            lastWon = "is a draw";
        }
        countGames++;
        myPreviousGames.setText("<html><b>Previous game:</b> "
                + lastWon + "\n   <b>total games played:</b> " + countGames);
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.OK_OPTION);
    }

    public void toMakeTurnIsImpossible(byte[] coordinates) {

        String message = "<html>Your move-<b> "+ coordinates[0]
                       + " , " + coordinates[1] + "</b> is invalid!  \n " +
                "  Do you want to Exit?";
        String title = "invalid move!";
        int answer = JOptionPane
            .showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        } /*else {
            myGame.interrupt();           //////??????????????????????????????????????????????
            myGame = null;
            myGameFinished = true;
            myGameStarted = false;
            createSelectPlayerView();
        }   */
    }

    public boolean isGamePaused() {
        return myGamePaused;
    }

    public boolean isUndoNeeded() {
        return isUndoNeeded;
    }

    public void setUndoNeeded(boolean undoNeeded) {
        isUndoNeeded = undoNeeded;
    }
}
