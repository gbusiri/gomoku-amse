package ru.amse.gomoku.ui.gui.view;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.board.IListener;
import ru.amse.gomoku.players.IPlayer;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class StatusView extends JPanel implements IListener {

    private IPlayer myFirstPlayer;
    private IPlayer mySecondPlayer;
    private IPlayer myCurrentPlayer;

    private int totalPlayedGames;

    private JLabel myTurnStatus;
    private JLabel myGeneralStatus;

    public StatusView() {

        totalPlayedGames = 0;
        addStatusComponents();
        setStatusWait();
    }

    public void setPlayers(IPlayer firstPlayer, IPlayer secondPlayer) {
        myFirstPlayer = firstPlayer;
        mySecondPlayer = secondPlayer;
        myCurrentPlayer = myFirstPlayer;
    }

    private void addStatusComponents() {
        setLayout(new GridLayout(2, 1, 5, 5));
        setBackground(new Color(240, 250, 240));
        setBorder(BorderFactory.createCompoundBorder(
                 BorderFactory.createTitledBorder("Status")
                 , BorderFactory.createEmptyBorder(6, 10, 5, 10)));

        myTurnStatus = new JLabel();
        myTurnStatus.setHorizontalTextPosition(JLabel.CENTER);
        myTurnStatus.setFont(new Font(null, Font.TRUETYPE_FONT, 12));
        
        myGeneralStatus = new JLabel("Total games played = 0");
        myGeneralStatus.setFont(new Font(null, Font.TRUETYPE_FONT, 12));
        myGeneralStatus.setHorizontalTextPosition(JLabel.CENTER);

        add(myTurnStatus);
        add(myGeneralStatus);
    }

    public void actionPerformed(int action, Object description) {

        switch(action) {

            case(IBoard.ADD_PERFORMED) :
                nextPlayer();
                setNextTurnStatus();
                break;

            case(IBoard.UNDO_PERFORMED) :
                nextPlayer();
                setNextTurnStatus();
                break;

            case(IBoard.REFRESH_PERFORMED) :
                setStatusWait();
                break;

            default:
                break;
        }
    }

    public void setGameFinished(boolean isWin, IPlayer winner) {
        String message;

        totalPlayedGames++;
        if (isWin) {
            message = "won by " + winner.getName();
        } else {
            message = "a draw";
        }
        myGeneralStatus.setText("<html><b>Total games:</b>"+ totalPlayedGames
                               + " <b>Previous game was</b> " + message);
        setStatusWait();
    }

    public void setNextTurnStatus() {
        myTurnStatus.setText("It is " + myCurrentPlayer.getName() + "'s turn now");
    }

    private void setStatusWait() {
        myTurnStatus.setText("<html><b>Waiting for start...</b></html>");
    }

    private void nextPlayer() {
        if (myCurrentPlayer == myFirstPlayer) {
            myCurrentPlayer = mySecondPlayer;
        } else {
            myCurrentPlayer = myFirstPlayer;
        }
    }
}
