package com.flippetyfloppety;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Jeanie on 4/2/2016.
 */
public class InventoryUsed extends JFrame {
    private JList inventoryList;
    private JTextField amntUsed;
    private JButton updateBtn;
    private JTable inventoryTable;
    private JPanel updateInv;
    private GUIHelper guiHelper;

    public InventoryUsed(DatabaseSetup db) {
        guiHelper = new GUIHelper(db);
        JFrame mainFrame = new JFrame("InventoryUsed");
        getContentPane().add(updateInv);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

//        inventoryList.setModel((new DefaultListModel()));
//        DefaultListModel iModel = (DefaultListModel) inventoryList.getModel();

//        String urgentQuery = "SELECT * FROM consumable NATURAL JOIN inventory";
//        guiHelper.fillProjectionList(iModel, urgentQuery);
//        MainPage mp = new MainPage();
//        mp.setVisible(false);
//        guiHelper.fillTable(rs, inventoryTable);

        updateBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
