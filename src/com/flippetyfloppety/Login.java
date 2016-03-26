package com.flippetyfloppety;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


/**
 * Created by Jeanie on 3/19/2016.
 */
public class Login extends JFrame{
    private static JFrame loginFrame;
    private JPanel panel1;
    private JPasswordField passwordField1;
    private JTextField usernameInput;
    private JButton loginBtn;

    public static final int RESEARCHER = 1;
    public static final int SUPERVISOR = 2;

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
                loginFrame.setVisible(false);

                DatabaseSetup db = new DatabaseSetup();
                System.out.println("username: " + usernameInput.getText());
                System.out.println("pwd: " + passwordField1.getText());
                int type = isRegistered(usernameInput.getText(), String.valueOf(passwordField1.getPassword()), db);
                if (type != 0) {
                    // go to next form
                    new MainPage(type).setVisible(true);
                } else {
                    passwordField1.setText("");
                }


            }
        });
    }

    private int isRegistered(String username, String password, DatabaseSetup db) {
        username = username.toLowerCase();
        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS validUser FROM researcher WHERE rsid=? AND rpwd=?");
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
//            ResultSet rs = db.executeSQLQuery("SELECT COUNT(*) as validUser FROM researcher WHERE rsid=" + username + " AND rpwd=" + password);
            int userType = 0;

            while (rs.next()) {
                System.out.println(rs.getInt("validUser"));
                if (rs.getInt("validUser") > 0) {
                    userType = RESEARCHER;
                    break;
                }
            }

            if (userType != this.RESEARCHER) {
                ps = con.prepareStatement("SELECT COUNT(*) AS validUser FROM supervisor WHERE ssid=? AND spwd=?");
                ps.setString(1, username);
                ps.setString(2, password);

                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("validUser") > 0) {
                        userType = SUPERVISOR;
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
//        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setVisible(true);
    }
}
