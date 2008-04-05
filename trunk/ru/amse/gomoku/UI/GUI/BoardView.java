package ru.amse.gomoku.UI.GUI;

import ru.amse.gomoku.Players.IPlayer;
import ru.amse.gomoku.Board.IBoard;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;

/**
 * 
 */
public class BoardView extends JPanel {

    private LinkedList<Ellipse2D> myDibs = new LinkedList<Ellipse2D>();
    private boolean isTurnAllowed = false;
    
    private byte[] nextTurnAccepted = new byte[2];
    private boolean turnIsReady = false;

    private Double myBoardSize;
    private double myDibSide;

    public BoardView() {

        setBackground(Color.getHSBColor(20,8000,100));
        setSize(GomokuFrame.myActualWidth * 9 / 10, GomokuFrame.myActualHeight * 9 / 10);
        addListener();
    }

    public void setDibSide(double side) {
        myDibSide = side;
    }

    private void addListener() {
        int size = IBoard.MY_BOARD_SIZE;
        myBoardSize = (double) getWidth();
        myDibSide = myBoardSize / size;

        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent event) {
                if (isTurnAllowed) {

                    double myX = event.getX();
                    double myY = event.getY();
                    byte width = (byte) (myX / myDibSide);
                    byte height = (byte) (myY / myDibSide);

                    if (!isContained(myX, myY)) {
                        setTurnAccepted(height, width);
                        turnIsReady = true;
                    }
                }
            }
        });
    }

    public boolean turnIsReady() {
        return turnIsReady;
    }

    public void undoTurn() {
        if ((myDibs != null) && (myDibs.size() > 0)) {
            myDibs.removeLast();
        }
        repaint();
    }

    public byte[] getTurn() {
        turnIsReady = false;
        return nextTurnAccepted;
    }

    private void setTurnAccepted(byte height, byte width) {
        nextTurnAccepted[0] = height;
        nextTurnAccepted[1] = width;
    }

    public void setTurnAllowed(boolean allowTurn) {
        isTurnAllowed = allowTurn;
    }

    public void addDib(byte height, byte width) {
        double leftX = width * myDibSide;
        double leftY = height * myDibSide;

        Ellipse2D ell = new Ellipse2D.Double(leftX, leftY, myDibSide, myDibSide);
        myDibs.add(ell);
        repaint();
    }

    public boolean isContained(double width, double height) {
        if (myDibs != null) {
            for (Ellipse2D ell : myDibs) {
                Rectangle2D rec = ell.getFrame();
                if (rec.contains(width, height)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        boolean checkColor = false;
        if (myDibs != null) {
            for (Ellipse2D rec : myDibs) {
                if (checkColor) {
                    g2.setPaint(Color.BLACK);
                } else {
                    g2.setPaint(Color.WHITE);
                }
                checkColor = !checkColor;
                g2.fill(rec);
            }            
            if (myDibs.size() > 0) {
                g2.setPaint(Color.RED);
                Ellipse2D last = myDibs.get(myDibs.size() - 1);
                g2.fill(new Ellipse2D.Double(last.getCenterX() - myDibSide / 4
                        , last.getCenterY() - myDibSide / 4, myDibSide / 2
                        , myDibSide / 2));
            }
        }

        g2.setPaint(Color.WHITE);
        double quantity = myBoardSize / myDibSide;
        double height = myBoardSize;
        for (int i = 0; i <= quantity; i++) {
            g2.draw(new Line2D.Double(myDibSide * i, 0, myDibSide * i, height));            
        }
        double width = myBoardSize;
        for (int i = 0; i <= quantity; i++) {
            g2.draw(new Line2D.Double(0, myDibSide * i, width,myDibSide * i));            
        }
    }

    public void refresh() {
        myDibs.clear();
        isTurnAllowed = false;
        repaint();
    }
}
