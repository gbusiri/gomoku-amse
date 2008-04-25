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
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 */
public class GomokuFrame extends JFrame implements IListener {

    public static final int myActualWidth;
    public static final int myActualHeight;

    public static final int MY_PERCENT = 100;
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
    private StatusView myStatus;
    private TournamentView myTournament;

    private UndoTurnListener myUndo;
    private ContinueGameListener myContinue;

    private JButton myUndoButton;
    private JButton myContinueButton;

    private volatile boolean myGameFinished = false;
    private volatile boolean myGameStarted = false;
    private volatile boolean myGamePaused = false;
    private volatile boolean isUndoNeeded = false;
    private volatile boolean isInterrupted = false;

    public GomokuFrame(IIntellectProvider intellectProvider) {

        super("GOMOKU");
        setSize(400, 595);
        setLocation(myActualWidth + 50, myActualHeight / 3);
        setResizable(false);

        myBoard = new Board();
        myIntellectProvider = intellectProvider;
        myImageProvider = new ImageProvider();
        myBoardView = new BoardView(myImageProvider);
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

        JButton newGame = new JButton(myImageProvider.getActionIcon("NewGame"));
        myUndoButton = new JButton(myImageProvider.getActionIcon("Undo"));
        myContinueButton = new JButton(myImageProvider.getActionIcon("Play"));

        StartGameListener myStart = new StartGameListener();
        myUndo = new UndoTurnListener();
        myContinue = new ContinueGameListener();

        addSettings(myStart, myUndo, myContinue);
        JPanel mainPanel = new JPanel(new BorderLayout());

        myBoardView.setSize(myActualWidth / 2
                         , myActualHeight / 2);

        JPanel pane = new JPanel();
        pane.setBackground(new Color(150, 150, 150));
        pane.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 17));
        pane.setLayout(new GridLayout(0, 1));
        pane.add(myBoardView);

        mainPanel.add(myStatus, BorderLayout.NORTH);
        mainPanel.add(pane, BorderLayout.CENTER);

        JToolBar buttonPanel = new JToolBar();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
        buttonPanel.setBackground(new Color(230, 250, 230));
        buttonPanel.setFloatable(false);
        buttonPanel.setLayout(new GridLayout(1, 4));

        newGame.addActionListener(myStart);
        myUndoButton.addActionListener(myUndo);
        myContinueButton.addActionListener(myContinue);

        toContinue(false);
        undo(false);

        buttonPanel.add(newGame);
        buttonPanel.add(new JLabel());
        buttonPanel.add(myUndoButton);
        buttonPanel.add(myContinueButton);
        GomokuFrame.this.add(buttonPanel, BorderLayout.SOUTH);
        GomokuFrame.this.add(mainPanel, BorderLayout.CENTER);
    }

    private void addSettings(AbstractAction ...var) {

        JMenuBar settingsBar = new JMenuBar();
        JMenu game = new JMenu("Game");
        for (AbstractAction aVar : var) {
            game.add(aVar);
        }
        JMenu settings = new JMenu("Edit");
        settings.add(new AbstractAction("Add Player") {

            public void actionPerformed(ActionEvent event) {

                JFileChooser addPlayer = new JFileChooser();
                addPlayer.setCurrentDirectory(new File("."));
                addPlayer.setFileFilter(new FileFilter() {

                    public boolean accept(File f) {
                        return f.getName().toLowerCase().endsWith(".class")
                               || f.isDirectory();
                    }

                    public String getDescription() {
                        return "Java player";
                    }
                });
                int result = addPlayer.showDialog(GomokuFrame.this, "Add");
                if (result == JFileChooser.APPROVE_OPTION) {
                    File playerFile = addPlayer.getSelectedFile();
                    String path = playerFile.getPath();
                    String name = playerFile.getName();
                    path = path.substring(0, path.length() - name.length()).replace('\\', '.');                    
                    try {
                        System.out.println(path);
                        URL url = new URL(path);
                        ClassLoader cl = new URLClassLoader(new URL[]{url});
                        try {
                            Class c = cl.loadClass(name);
                            try {
                                myIntellectProvider
                                .registerPlayer(playerFile.getName()
                                 , (IPlayer) c.getConstructor(String.class, byte.class)
                                            .newInstance(playerFile.getName()
                                                        , IBoard.DEFAULT_DIB_COLOUR));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                addPlayer.setVisible(false);
            }
        });
        settings.add(new AbstractAction("Change BoardColour") {

            public void actionPerformed(ActionEvent e) {
                myBoardView.setBackground(JColorChooser
                                         .showDialog(GomokuFrame.this
                                                    , "ChooseBoardColor"
                                                    , BoardView.DEFAULT_COLOR));
            }
        });
        game.add(new AbstractAction("Tournament") {

            public void actionPerformed(ActionEvent e) {
                new SelectPlayersForTournamentView(myIntellectProvider
                                                  , GomokuFrame.this
                                                  , myImageProvider);
            }
        });
        game.add(settings);
        game.add(new AbstractAction("Exit") {

            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane
                             .showConfirmDialog(GomokuFrame.this
                                               , "Do you really want to exit?"
                                               , "Exit"
                                               , JOptionPane.OK_CANCEL_OPTION
                                               , JOptionPane.INFORMATION_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    System.exit(1);
                }
            }
        });
        settingsBar.add(game);
        GomokuFrame.this.setJMenuBar(settingsBar);
    }

    public void setPlayers(IPlayer first, IPlayer second) {

        isInterrupted = false;
        myBoard.refreshBoard();
        myGame = new Controller(myBoard, this, false);
        myGame.setPlayers(first, second);
        myStatus.setPlayers(first, second);
        myStatus.setNextTurnStatus();
        
        myGameStarted = true;
        myGameFinished = false;
    }

    public boolean isStarted() {
        return myGameStarted;
    }

    private void createSelectPlayerView() {
        new SelectPlayerView(this, myIntellectProvider, myImageProvider);
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

            case(IBoard.ADD_PERFORMED) :
                myUndo.setEnabled(true);
                myUndoButton.setEnabled(true);
                break;

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
        undo(false);
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

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public void setInterrupted(boolean set) {
        isInterrupted = set;
    }

    public void setPlayersForTournament(String[] playersChosen, int number) {

        Tournament tournament = new Tournament(playersChosen
                                              , number
                                              , myIntellectProvider
                                              , GomokuFrame.this);
        myTournament = new TournamentView(this, tournament);
    }

    public void undo(boolean undo) {
        myUndo.setEnabled(undo);
        myUndoButton.setEnabled(undo);
        setUndoNeeded(false);
    }

    private void toContinue(boolean toContinue) {
        myContinue.setEnabled(toContinue);
        myContinueButton.setEnabled(toContinue);

        if (toContinue) {
            myContinueButton.setIcon(myImageProvider.getActionIcon("Pause"));
        } else {
            myContinueButton.setIcon(myImageProvider.getActionIcon("Play"));
        }
    }

    private class StartGameListener extends AbstractAction
                                    implements ActionListener {

        public StartGameListener(Icon icon) {
            super("New Game", icon);
            putValue(Action.SHORT_DESCRIPTION, "click here to start a new game");
        }

        public StartGameListener() {
            this(null);
        }

        public void actionPerformed(ActionEvent event) {
            if (myGame == null) {
                startNewGame();
            } else if ((myGameFinished)) {
                startNewGame();
            } else {
                int result = JOptionPane
                             .showConfirmDialog(GomokuFrame.this
                                               , "Do you wish to start a new game\n"
                                               + " and leave current?"
                                               , "New Game"
                                               , JOptionPane.OK_CANCEL_OPTION
                                               , JOptionPane.INFORMATION_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    isInterrupted = true;
                    myBoard.refreshBoard();
                    undo(false);
                    startNewGame();
                }
            }
        }

        private void startNewGame() {
            
            myGamePaused = false;
            setUndoNeeded(false);
            toContinue(false);

            createSelectPlayerView();
        }
    }

    private class UndoTurnListener extends AbstractAction
                                   implements ActionListener {

        public UndoTurnListener(Icon icon) {
            super("Undo", icon);
            putValue(Action.SHORT_DESCRIPTION, "undo last made move");
        }

        public UndoTurnListener() {
            this(null);
        }

        public void actionPerformed(ActionEvent e) {
            if (myGameStarted) {

                myGamePaused = true;
                setUndoNeeded(true);
                toContinue(true);
            }
        }
    }

    private class ContinueGameListener extends AbstractAction
                                       implements ActionListener {

        public ContinueGameListener(Icon icon) {
            super("Continue", icon);
            putValue(Action.SHORT_DESCRIPTION
                    , "continue playing current game");
        }

        public ContinueGameListener() {
            this(null);
        }

        public void actionPerformed(ActionEvent e) {
            if (myGameStarted) {

                myGamePaused = false;
                setUndoNeeded(false);
                if (myBoard.isUndoPossible()) {
                    undo(true);
                }
                toContinue(false);
            }
        }
    }
}
