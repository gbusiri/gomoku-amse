package ru.amse.gomoku.UI;

import ru.amse.gomoku.logic.board.Board;
import ru.amse.gomoku.logic.player.IPlayer;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 25.02.2008
 * Time: 0:21:32
 * To change this template use File | Settings | File Templates.
 */
public class BoardView extends JPanel {

    private LinkedList<Ellipse2D> myDibs = new LinkedList<Ellipse2D>();
    private boolean myFlagSet = true;
    private double myDibSide;

    private int[] nextTurnAccepted = new int[2];
    private boolean turnIsReady = false;
    private IPlayer myFirst;
    private IPlayer mySecond;
    private Double myBoardSize;

    public BoardView() {

        setBackground(Color.LIGHT_GRAY);
        setSize(MyFrame.myActualWidth * 9 / 10, MyFrame.myActualHeight * 9 / 10);
        addListener();
    }

    public void setDibSide(double side) {
        myDibSide = side;
    }

    private void addListener() {
        int size = Board.MY_BOARD_SIZE;
        myBoardSize = (double) getWidth();
        myDibSide = myBoardSize / size;

        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent event) {
                if (!myFlagSet) {
                    double myX = event.getX();
                    double myY = event.getY();

                    int width = (int) (myX / myDibSide);
                    int height = (int) (myY / myDibSide);

                    if ((!isContained(myX, myY))
                       && ((!myFirst.isComputer()
                          || !mySecond.isComputer()))) {
                        setTurnAccepted(height, width);
                        turnIsReady = true;
                    }
                    myFlagSet = true;
                }
            }
        });
    }

    public boolean turnIsReady() {
        return turnIsReady;
    }

    public int[] getTurn() {
        turnIsReady = false;
        return nextTurnAccepted;
    }

    private void setTurnAccepted(int height, int width) {
        nextTurnAccepted[0] = height;
        nextTurnAccepted[1] = width;
    }

    public void setFlag(boolean flag) {
        myFlagSet = flag;
    }

    public void addDib(int height, int width) {
        myFlagSet = false;
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
                    g2.setPaint(Color.YELLOW);
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
        myDibs = new LinkedList<Ellipse2D>();
        myFlagSet = false;
        repaint();
    }

    public void setPlayers(IPlayer first, IPlayer second) {
        myFirst = first;
        mySecond = second;
    }
}
