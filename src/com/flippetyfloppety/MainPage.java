package com.flippetyfloppety;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by Jeanie on 3/25/2016.
 */
public class MainPage extends JFrame {
    private JTabbedPane inventoryPane;
    private JTextArea iSearchQuery;
    private JButton iSearchBtn;
    private JTextField eSearchQuery;
    private JButton eSearchBtn;
    private JTextField rsid;
    private JPasswordField rpwd;
    private JButton createAccBtn;
    private JTextField rname;
    private JFrame mainFrame;
    private JPanel inventory;
    private JPanel experiment;
    private JPanel statistics;
    private JPanel userSettings;

    private int user;


    public MainPage(int userType, DatabaseSetup db) {
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
            inventoryPane.setEnabledAt(3, false);
        }
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        createAccBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when create account button is pressed
             * adds a new researcher tuple to the database
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = db.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO researcher VALUES (?, ?, ?)");
                    ps.setString(1, rsid.getText().toLowerCase());
                    ps.setString(2, rname.getText());
                    ps.setString(3, String.valueOf(rpwd.getPassword()));

                    ResultSet rs = ps.executeQuery();
                    // alert the user that the insert was succesful
                    JOptionPane.showMessageDialog(mainFrame, "Researcher " + rsid.getText().toLowerCase() + " has succesfully been added to the database!");
                } catch (SQLException ce) {
                    // error code 00001 = unique tuple check failed
                    if (ce.getMessage().substring(4, 9).equals("00001")) {
                        JOptionPane.showMessageDialog(mainFrame, "Researcher already exists in database! Please login with appropriate credentials.", "Error!", JOptionPane.ERROR_MESSAGE);
                    } else JOptionPane.showMessageDialog(mainFrame, ce.getMessage());

                } finally {
                    // clear the user input fields
                    rsid.setText("");
                    rname.setText("");
                    rpwd.setText("");
                }
            }
        });
    }
}
