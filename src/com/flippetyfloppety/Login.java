package com.flippetyfloppety;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                System.out.println("HI");
            }
        });
    }

    public static void main(String[] args) {
        loginFrame = new JFrame("Login");
        loginFrame.setContentPane(new Login().panel1);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setVisible(true);
    }
}
