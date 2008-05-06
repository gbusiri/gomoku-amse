package ru.amse.gomoku.ui.gui.view;

import ru.amse.gomoku.providers.IImageProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 */
public class DibSelectionView extends JDialog {

    private BoardView myBoard;
    private IImageProvider myImages;
    private ButtonGroup group1 = new ButtonGroup();
    private ButtonGroup group2 = new ButtonGroup();

    public DibSelectionView(JFrame frame, BoardView board, IImageProvider images) {
        super(frame, "Select the dibs", true);

        myBoard = board;
        myImages = images;
        addSelectionPane();

        Point x = frame.getLocationOnScreen();
        setLocation((int)(x.getX() + 80),(int)(x.getY() + 100));
        setSize(200, 420);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void addSelectionPane() {
        JPanel main = new JPanel(new BorderLayout());

        JPanel buttons = new JPanel();
        addButtons(buttons);
        main.add(buttons, BorderLayout.SOUTH);

        JPanel selection = new JPanel(new GridLayout(1, 2, 10, 10));
        addSelection(selection);
        main.add(selection, BorderLayout.CENTER);

        add(main);
    }

    private void addButtons(JPanel pane) {
        JButton select = new JButton("Select");
        select.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ImageIcon first
                        = myImages.getActionIcon(group1.getSelection().getActionCommand());
                ImageIcon second
                        = myImages.getActionIcon(group2.getSelection().getActionCommand());
                myBoard.setDibs(first, second);
                DibSelectionView.this.dispose();
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                DibSelectionView.this.dispose();
            }
        });

        pane.add(select);
        pane.add(cancel);
    }

    private void addSelection(JPanel selection) {
        JPanel first = new JPanel();
        first.setBorder(BorderFactory.createBevelBorder(1));
        first.setLayout(new BoxLayout(first, BoxLayout.Y_AXIS));
        first.add(new JLabel("   firstPlayer dib"));

        JRadioButton[] buttons1 = new JRadioButton[myImages.getDibNumber()];
        for (int i = 0; i < buttons1.length; i++) {
            String name = "Dib" + (i + 1);
            buttons1[i] = new JRadioButton(name, myImages.getActionIcon(name));
            buttons1[i].setActionCommand(name);
            group1.add(buttons1[i]);
            first.add(buttons1[i]);
        }
        buttons1[0].setSelected(true);
        JPanel second = new JPanel();
        second.setLayout(new BoxLayout(second, BoxLayout.Y_AXIS));
        second.setBorder(BorderFactory.createBevelBorder(1));
        second.add(new JLabel(" secondPlayer dib"));

        JRadioButton[] buttons2 = new JRadioButton[myImages.getDibNumber()];
        for (int i = 0; i < buttons2.length; i++) {
            String name = "Dib" + (i + 1);
            buttons2[i] = new JRadioButton(name, myImages.getActionIcon(name));
            buttons2[i].setActionCommand(name);
            group2.add(buttons2[i]);
            second.add(buttons2[i]);
        }

        buttons2[5].setSelected(true);
        selection.add(first);
        selection.add(second);
    }
}
