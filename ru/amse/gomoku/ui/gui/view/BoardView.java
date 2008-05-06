package ru.amse.gomoku.ui.gui.view;

import ru.amse.gomoku.board.IBoard;
import ru.amse.gomoku.board.IListener;
import ru.amse.gomoku.providers.IImageProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

/**
 * 
 */
public class BoardView extends JPanel implements IListener {

    public static final Color DEFAULT_COLOR = new Color(204, 204, 204);

    private LinkedList<Ellipse2D> myDibs = new LinkedList<Ellipse2D>();
    private JLabel[][] myZone;
    private ImageIcon myDibFirst;
    private ImageIcon myDibSecond;
    private Color myColor;

    private boolean isTurnAllowed = false;
    
    private byte[] nextTurnAccepted = new byte[2];
    private boolean turnIsReady = false;

    private double myBoardSize;
    private double myDibSide;

    public BoardView(IImageProvider images, Color boardColor) {

        myDibFirst = images.getActionIcon("Dib6");
        myDibSecond = images.getActionIcon("Dib1");
        if (boardColor == null) {
            myColor = DEFAULT_COLOR;
        } else {
            myColor = boardColor;
        }
        setBackground(myColor);
        setSize(GomokuFrame.ourActualWidth * 9 / 10
               , GomokuFrame.ourActualHeight * 9 / 10);
        setLayout(new GridLayout(IBoard.MY_BOARD_SIZE, IBoard.MY_BOARD_SIZE));

        addListener();
        addLabels();
    }

    public void setDibs(ImageIcon first, ImageIcon second) {
        myDibFirst = first;
        myDibSecond = second;
    }

    public String getFirstDib() {
        return myDibFirst.getDescription();
    }

    public String getSecondDib() {
        return myDibSecond.getDescription();
    }

    private void addLabels() {
        myZone = new JLabel[IBoard.MY_BOARD_SIZE][IBoard.MY_BOARD_SIZE];

        for (JLabel[] labels : myZone) {
            for (int i = 0; i < IBoard.MY_BOARD_SIZE; i++) {

                labels[i] = new JLabel();
                labels[i].setSize((int)myDibSide, (int)myDibSide);
                labels[i].setAlignmentX(JLabel.WEST);
                this.add(labels[i]);
            }
        }
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

    private void undoTurn() {
        if ((myDibs != null) && (myDibs.size() > 0)) {
            Ellipse2D el = myDibs.removeLast();

            int x = (int)(el.getBounds2D().getMinX() / myDibSide);
            int y = (int)(el.getBounds2D().getMinY() / myDibSide);
            myZone[y][x].setIcon(null);
            repaint();
        }         
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

    private void addDib(byte height, byte width) {
        double leftX = width * myDibSide;
        double leftY = height * myDibSide;

        Ellipse2D ell = new Ellipse2D.Double(leftX, leftY, myDibSide, myDibSide);
        myDibs.add(ell);
        repaint();
    }

    private boolean isContained(double width, double height) {
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

        boolean checkColor = false;
        if (myDibs != null) {
            for (Ellipse2D rec : myDibs) {
                ImageIcon icon;
                if (checkColor) {
                    icon = myDibFirst;
                } else {
                    icon = myDibSecond;
                }
                int x = (int)(rec.getBounds2D().getMinX() / myDibSide);
                int y = (int)(rec.getBounds2D().getMinY() / myDibSide);

                Icon actual = myZone[y][x].getIcon();
                if ((actual == null) || !actual.equals(icon)) {
                    myZone[y][x].setIcon(icon);
                }
                checkColor = !checkColor;
            }
            if (myDibs.size() > 0) {
                g2.setPaint(Color.BLACK);
                Ellipse2D last = myDibs.get(myDibs.size() - 1);
                g2.draw(last.getFrame());
            }
        }
    }

    private void refresh() {
        myDibs.clear();
        for (JLabel[] labels : myZone) {
            for (int i = 0; i < IBoard.MY_BOARD_SIZE; i++) {
                labels[i].setIcon(null);
            }
        }
        isTurnAllowed = false;
        repaint();
    }

    public synchronized void actionPerformed(int action, Object description) {

        switch(action) {

            case(IBoard.ADD_PERFORMED) :
                byte[] coordinates = (byte[])description;
                addDib(coordinates[0], coordinates[1]);
                break;

            case(IBoard.UNDO_PERFORMED) :
                undoTurn();
                break;

            case(IBoard.REFRESH_PERFORMED) :
                refresh();
                break;

            default:
                break;
        }
    }

    public Color getColor() {
        return myColor;
    }

    public void setMyBackground(Color c) {
        setBackground(c);
        myColor = c;
    }
}
