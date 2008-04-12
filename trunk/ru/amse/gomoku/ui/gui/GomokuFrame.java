package ru.amse.gomoku.ui.gui;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.board.IListener;
import ru.amse.gomoku.board.impl.Board;
import ru.amse.gomoku.players.IPlayer;
import ru.amse.gomoku.providers.IImageProvider;
import ru.amse.gomoku.providers.IIntellectProvider;
import ru.amse.gomoku.providers.impl.ImageProvider;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class GomokuFrame extends JFrame implements IListener {

    public static final int myActualWidth;
    public static final int myActualHeight;

    public static final boolean ALLOW_STOP = false;

    static {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        myActualWidth = screenSize.height / 2;
        myActualHeight = myActualWidth * 9 / 8;
    }

    private IIntellectProvider myIntellectProvider;
    private IImageProvider myImageProvider;

    private Controller myGame = null;
    private final IBoard myBoard;
    private BoardView myBoardView;
    private JDialog mySelectView;
    private StatusView myStatus;

    private boolean myGameFinished = false;
    private boolean myGameStarted = false;
    private boolean myGamePaused = false;
    private boolean isUndoNeeded = false;

    public GomokuFrame(IIntellectProvider intellectProvider) {

        super("GOMOKU");
        setSize(400, 595);
        setLocation(myActualWidth + 50, myActualHeight / 3);
        setResizable(false);

        myBoard = new Board();
        myBoardView = new BoardView();
        myIntellectProvider = intellectProvider;
        myImageProvider = new ImageProvider();
        myStatus = new StatusView();

        registerListenersToBoard();
        addComponents();
    }

    public BoardView getBoardView() {
        return myBoardView;
    }

    private void registerListenersToBoard() {

        myBoard.registerListener(this);
        myBoard.registerListener(myBoardView);
        myBoard.registerListener(myStatus);
    }

    private void addComponents() {
        addSettings();
        JPanel mainPanel = new JPanel(new BorderLayout());

        myBoardView.setSize(myActualWidth / 2
                         , myActualHeight / 2);

        JPanel pane = new JPanel();
        pane.setBackground(new Color(0, 180, 150));
        pane.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Current board!")
                       , BorderFactory.createEmptyBorder(5, 10, 10, 11)));
        pane.setLayout(new GridLayout(0, 1));
        pane.add(myBoardView);

        mainPanel.add(myStatus, BorderLayout.NORTH);
        mainPanel.add(pane, BorderLayout.CENTER);

        JToolBar buttonPanel = new JToolBar();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
        buttonPanel.setBackground(new Color(230, 250, 230));
        buttonPanel.setFloatable(false);
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(new AbstractAction("New Game"
                                          , myImageProvider.getActionIcon("NewGame")) {

            public void actionPerformed(ActionEvent event) {

                this.putValue(Action.SHORT_DESCRIPTION, "start a new game");
                if (myGame == null) {
                    startNewGame();
                } else if ((myGameFinished)) {
                    myBoard.refreshBoard();
                    startNewGame();
                } else if (ALLOW_STOP) {
                    //  myGame.stop()
                    // startNewGame();
                }
            }

            private void startNewGame() {
                myGamePaused = false;
                isUndoNeeded = false;
                createSelectPlayerView();
            }
        });
        buttonPanel.add(new JLabel());

        buttonPanel.add(new AbstractAction("undo"
                                          , myImageProvider.getActionIcon("Undo")) {

            public void actionPerformed(ActionEvent event) {

                this.putValue(Action.SHORT_DESCRIPTION, "undo last made move");
                myGamePaused = true;
                isUndoNeeded = true;
            }
        });
        buttonPanel.add(new AbstractAction("continue.."
                                          , myImageProvider.getActionIcon("Play")) {

            public void actionPerformed(ActionEvent event) {
                this.putValue(Action.SHORT_DESCRIPTION
                             , "continue playing current game");
                myGamePaused = false;
                isUndoNeeded = false;
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void addSettings() {
        final JFrame current = this;
        JMenuBar settings = new JMenuBar();
        JMenu game = new JMenu("Game");
        JMenu edit = new JMenu("Edit");
        edit.add(new AbstractAction("Add Player") {

            public void actionPerformed(ActionEvent event) {

                JFileChooser addPlayer = new JFileChooser();
                addPlayer.setCurrentDirectory(new File("."));
                addPlayer.setFileFilter(new FileFilter() {

                    public boolean accept(File f) {
                        return f.getName().toLowerCase().endsWith(".java")
                               || f.isDirectory();
                    }

                    public String getDescription() {
                        return "Java player";
                    }
                });
                int result = addPlayer.showDialog(current, "add");
                if (result == JFileChooser.APPROVE_OPTION) {
                    File playerFile = addPlayer.getSelectedFile();
                    String path = playerFile.getPath();
                    path = path.substring(path.lastIndexOf("ru.amse")).replace('\\', '.');
                    path = path.substring(0, path.length() - ".java".length());

                    System.out.println(path);
                    Class c = null;
                    try {
                        c = Class.forName(path);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace(); 
                    }
                    try {
                        myIntellectProvider
                        .registerPlayer(playerFile.getName()
                                       , (IPlayer) c.getConstructor(String.class, byte.class)
                                         .newInstance(playerFile.getName()
                                                     , IBoard.DEFAULT_DIB_COLOUR));
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
                addPlayer.setVisible(false);
            }
        });
        edit.add(new AbstractAction("Change BoardColour") {

            public void actionPerformed(ActionEvent e) {
                myBoardView.setBackground(JColorChooser
                                         .showDialog(current
                                                    , "ChooseBoardColor"
                                                    , BoardView.DEFAULT_COLOR));
            }
        });
        settings.add(game);
        settings.add(edit);
        current.setJMenuBar(settings);
    }

    public void setPlayers(IPlayer first, IPlayer second) {

        myGame = new Controller(myBoard, this);
        myGame.setPlayers(first, second);
        myStatus.setPlayers(first, second);
        myStatus.setNextTurnStatus();
        
        mySelectView.dispose();
        myGameStarted = true;
        myGameFinished = false;
    }

    public boolean isStarted() {
        return myGameStarted;
    }

    private void createSelectPlayerView() {
        mySelectView = new SelectPlayerView(this
                                           , myIntellectProvider
                                           , myImageProvider);        
    }

    public boolean isGamePaused() {
        return myGamePaused;
    }

    public boolean isUndoNeeded() {
        return isUndoNeeded;
    }

    private void setUndoNeeded(boolean undoNeeded) {
        isUndoNeeded = undoNeeded;
    }

    public synchronized void actionPerformed(int action, Object description) {

        switch(action) {

            case(IBoard.UNDO_PERFORMED) :
                setUndoNeeded(false);
                break;

            case(IBoard.INVALID_MOVE) :
                //toMakeTurnIsImpossible((byte[])description);
                break;

            case(IBoard.WIN_GOT) :
                //setGameFinished(true, myGame.getCurrentPlayer());
                break;

            default:
                break;
        }
    }

    public void setGameFinished(boolean gameFinishedWithWin, IPlayer winner) {
        myGameFinished = true;
        myGameStarted = false;
        myStatus.setGameFinished(gameFinishedWithWin, winner);

        String message;
        String title;

        ImageIcon icon;
        if (gameFinishedWithWin) {
            title = "There is a winner!";
            message = "!!" + winner.getName() + "!!";
            icon = winner.getImage();
            if (icon == null) {
                icon = myImageProvider.getActionIcon("Win");
            }           
        } else {
            title = "It is a Draw!";
            message = "Congaratulations to both of You";
            icon = myImageProvider.getActionIcon("Draw");
        }
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.OK_OPTION, icon);
    }

    public void toMakeTurnIsImpossible(byte[] coordinates) {

        String message = "<html>Your move-<b> "+ coordinates[0]
                       + " , " + coordinates[1] + "</b> is invalid!  \n " +
                "  System will exit!";
        String title = "invalid move!";
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.OK_OPTION);
        System.exit(0);
    }
}
