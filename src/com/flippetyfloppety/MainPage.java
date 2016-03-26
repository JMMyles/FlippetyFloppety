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
    private JPanel inventory;
    private JPanel experiment;
    private JPanel statistics;
    private JPanel userSettings;

    private int user;

    public MainPage(int userType) {
        System.out.println("In Main Page");
        this.user = userType;

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
        if (this.user == Login.RESEARCHER) {
            inventoryPane.setEnabledAt(2, false);
//            inventoryPane.remove(getPanel("Statistics"));
        }
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
