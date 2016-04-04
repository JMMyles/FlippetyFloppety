package com.flippetyfloppety;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by Jeanie on 4/3/2016.
 */
public class DeleteInv extends JFrame {
    private JList inventoryItems;
    private JButton deleteButton;
    private JPanel deletePane;
    private DatabaseSetup db;
    private GUIHelper guiHelper;
    private JFrame mainFrame;

    public DeleteInv(DatabaseSetup db) {
        this.db = db;
        this.guiHelper = new GUIHelper(db);

        inventoryItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryItems.setModel(new DefaultListModel());
        DefaultListModel iModel = (DefaultListModel)inventoryItems.getModel();

        try {
            String invQuery = "SELECT iid, iname FROM inventory";
            ResultSet rs = db.executeSQLQuery(mainFrame, invQuery);
            while (rs.next()) {
                iModel.addElement(rs.getString("iid") + "," + rs.getString("iname"));
            }
        } catch (SQLException sqle) {
            guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
        }

        mainFrame = new JFrame("deleteInv");
        getContentPane().add(deletePane);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        deleteButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = inventoryItems.getSelectedValue().toString();
                String[] splitSelected = selectedItem.split(",");

                String query = "DELETE FROM inventory WHERE iid=" + splitSelected[0];
                db.executeSQLQuery(mainFrame, query);
            }
        });
    }
}
