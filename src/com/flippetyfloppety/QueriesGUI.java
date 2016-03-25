package com.flippetyfloppety;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.awt.*;
import javax.swing.*;
//import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueriesGUI {
// TODO handle query action via button
// TODO stats tab
// TODO differentiate between supervisors and researchers (if 1, then...)
// TODO users tab for supervisors
// Delete w/out cascade: delete researcher

    private static JFrame queryFrame

    private JButton sPBtn, jBtn, divideBtn, aggBtn, nestAggMinBtn, nestAggMaxBtn, cDeleteBtn, nCDeleteBtn;

    public queryGUI() {

//        // panel costruction
//        private JPanel queryPanel;
//        queryFrame = new JFrame("Query");
//        queryFrame.setContentPane(new queriesGUI.queryPanel);
//        queryFrame.pack();
//        queryFrame.setVisible(true);

        TextField tfSelection, tfAttribute, tfJoin, tfDivision, tfAgg, tfNestAgg, tfCascadeDeletion, tfNCascadeDeletion;
        // projection and selection text field
        tfSelection = new TextField("selection condition", 30);  //arbitrary length - test
        tfAttribute = new TextField("attribute", 30);
        sPBtn.addActionListener(new ActionListener() {
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
        jBtn.addActionListener(new ActionListener() {
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
        divideBtn.addActionListener(new ActionListener() {
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
        aggBtn.addActionListener(new ActionListener() {
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
        nestAggMinBtn.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
        nestAggMaxBtn.addActionListener(new ActionListener() {
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
        cDeleteBtn.addActionListener(new ActionListener() {
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
        nCDeleteBtn.addActionListener(new ActionListener() {
            /**
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the query
            }
        });
    }

    // function to construct panel
    public void queryPanel() {
        
        // panel costruction
        private JPanel queryPanel;
        queryFrame = new JFrame("Query");
        queryFrame.setContentPane(new queriesGUI.queryPanel);
        queryFrame.pack();
        queryFrame.setVisible(true);
    }
}
