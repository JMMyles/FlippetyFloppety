package com.flippetyfloppety;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jeanie on 3/19/2016.
 */
public class Login {
    private static JFrame loginFrame;
    private JPanel panel1;
    private JPasswordField passwordField1;
    private JTextField usernameInput;
    private JButton loginBtn;

    public Login() {
        loginBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when login button is pressed.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean researcherLogin = false;
                System.out.println("HI");
                DatabaseSetup db = new DatabaseSetup();

                int type = isRegistered(usernameInput.getText(), String.valueOf(passwordField1.getPassword()), db);
                if (type != 0) {
                    // go to next form
                    if (type == 1) {
                        // researcher
                    } else if (type == 2) {
                        // supervisor
                    }
                } else {
                    passwordField1.setText("");
                }


            }
        });
    }

    private int isRegistered(String username, String password, DatabaseSetup db) {
        try {
            ResultSet rs = db.executeQuery("SELECT COUNT(*) as validUser FROM researchers WHERE rsid =" + username + " AND rpwd =" + password);
            int userType = 0;

            while (rs.next()) {
                if (rs.getInt("validUser") > 0) {
                    userType = 1;
                    break;
                }
            }

            if (userType != 1) {
                rs = db.executeQuery("SELECT COUNT(*) as validUser FROM supervisors WHERE ssid =" + username + " AND spwd =" + password);
                while (rs.next()) {
                    if (rs.getInt("validUser") > 0) {
                        userType = 2;
                        break;
                    }
                }
            }
            return userType;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }

    public static void main(String[] args) {
        loginFrame = new JFrame("Login");
        loginFrame.setContentPane(new Login().panel1);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setVisible(true);
    }
}
