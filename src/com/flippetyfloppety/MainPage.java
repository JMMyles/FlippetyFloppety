package com.flippetyfloppety;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Jeanie on 3/25/2016.
 */
public class MainPage extends JFrame {
    private JTabbedPane inventoryPane;
    private JTextArea iSearchQuery;
    private JButton iSearchBtn;
    private JTextField eSearchQuery;
    private JButton eSearchBtn;
    private JFrame mainFrame;

    public MainPage() {
        System.out.println("In Main Page");


        iSearchBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when inventory search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        eSearchBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when experiment search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        mainFrame = new JFrame("Main");
        getContentPane().add(inventoryPane);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
