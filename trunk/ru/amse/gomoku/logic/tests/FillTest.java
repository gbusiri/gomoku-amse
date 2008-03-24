package ru.amse.gomoku.logic.tests;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Tushka
 * Date: 21.03.2008
 * Time: 2:00:59
 * To change this template use File | Settings | File Templates.
 */
public class FillTest {
    public static void main(String[] args) {
        FillFrame gameFrame = new FillFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }


}

class FillFrame extends JFrame {

    FillFrame() {
        setTitle("gfgd");
        setSize(300, 400);
        FillPanel panel = new FillPanel();
        add(panel);
    }


}

class FillPanel extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

         Graphics2D g2 = (Graphics2D) g;
            Rectangle2D rec = new Rectangle2D.Double(100,100,250,150);
                g2.fill(rec);
         }
    }
