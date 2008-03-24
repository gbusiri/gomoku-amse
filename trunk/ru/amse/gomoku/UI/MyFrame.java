package ru.amse.gomoku.UI;

import ru.amse.gomoku.logic.player.IPlayer;
import ru.amse.gomoku.logic.player.AIPlayer;
import ru.amse.gomoku.logic.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 24.02.2008
 * Time: 23:51:16
 * To change this template use File | Settings | File Templates.
 */
public class MyFrame extends JFrame {

    public static final int myActualWidth;
    public static final int myActualHeight;
    public static final boolean ALLOW_STOP = false;

    static {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        myActualWidth = screenSize.height / 2;
        myActualHeight = myActualWidth * 9 / 8;

    }

    private IPlayer myFirst = new AIPlayer("  first assigned", 1);
    private IPlayer mySecond = new AIPlayer(" second assigned", 2);
    private Controller myGame = null;
    private final Board myBoard = new Board();
    private final BoardView myBoardView = new BoardView();
    private JFrame mySelectView;

    private boolean myGameFinished = false;
    private boolean myGameStarted = false;

    public MyFrame() {
        super(myActualWidth + "Gomoku game!" + myActualHeight);
        setSize(myActualWidth, myActualHeight);   
        setLocation(myActualWidth, myActualHeight / 2);
        setResizable(true);

        addComponents();
    }

    private void addComponents() {

        myBoardView.setSize(MyFrame.myActualWidth / 2
                         , MyFrame.myActualHeight / 2);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createCompoundBorder(
                      BorderFactory.createTitledBorder("Current Board!")
                      , BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        pane.setLayout(new GridLayout(0, 1));
        pane.add(myBoardView, BorderLayout.CENTER);

        final MyFrame frame = this;
        add(pane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(new JButton(new AbstractAction("New Game") {
            public void actionPerformed(ActionEvent event) {
                if (myGame == null) {
                    startNewGame();
                } else if (myGameFinished) {
                    myBoard.refreshBoard();
                    myBoardView.refresh();
                    startNewGame();
                } else if (ALLOW_STOP) {
                  /*  myGame.stop();     */
                    startNewGame();
                }
            }

            private void startNewGame() {
                createSelectPlayerView(myBoardView);
                myGame = new Controller(myBoardView
                                       , myBoard
                                       , frame);
            }
        }));

        buttonPanel.add(new JButton(new AbstractAction("do this") {
            public void actionPerformed(ActionEvent event) {
                JFrame tyu= new JFrame("did it!");
                tyu.setVisible(true);
            }
        }));
        add(buttonPanel, BorderLayout.NORTH);
    }

    public void setPlayers(IPlayer first, IPlayer second) {
        myFirst = first;
        mySecond = second;
        myGame.setPlayers(first, second);
        myBoardView.setPlayers(first, second);
        mySelectView.dispose();//????????????????????
        myGameStarted = true;
    }

    public boolean isStarted() {
        return myGameStarted;
    }

    private void createSelectPlayerView(BoardView boardView) {
        mySelectView = new JFrame("Select the players");

        Container contentPane = mySelectView.getContentPane();
        contentPane.add(new SelectPlayerView(this, boardView));
        mySelectView.setLocation(myActualWidth / 2 + 200, myActualWidth / 2 + 100);
        mySelectView.pack();
        mySelectView.setVisible(true);
    }

    public void setGameFinished(boolean gameFinished, IPlayer winner) {
        myGameFinished = gameFinished;
        myGameStarted = false;

        JFrame fr = new JFrame("There is a winner!");
        fr.setSize(250, 80);
        fr.setLocation(myActualWidth / 2 + 200, myActualWidth / 2 + 100);
        fr.add(new JTextField("!!" + winner.getName() + "!!"));
        fr.setVisible(true);
    }
}
