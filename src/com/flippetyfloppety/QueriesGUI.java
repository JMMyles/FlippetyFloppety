package com.flippetyfloppety;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.awt.*;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueriesGUI {

    public void queryGUI() {
        TextField tfPs, tfJoin, tfDivision, tfAgg, tfNestAgg, tfCascadeDeletion, tfNCascadeDeletion;
        // projection and selection text field
        tfPs = new TextField("projection and selection query", 30);  //arbitrary length - test
        tfPs.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
        // join text field
        tfJoin = new TextField("join query", 30);
        tfJoin.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
        // division text field
        tfDivision = new TextField("division query", 30);
        tfDivision.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
        // aggregation text field
        tfAgg = new TextField("aggregation query", 30);
        tfAgg.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
        // nested aggregation text field
        tfNestAgg = new TextField("nested aggregation query", 30);
        tfNestAgg.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
        // deletion with cascades text field
        tfCascadeDeletion = new TextField("deletion with cascades", 30);
        tfCascadeDeletion.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
        // deletion without cascades text field
        tfNCascadeDeletion = new TextField("deletion without cascades", 30);
        tfNCascadeDeletion.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
    }
}
