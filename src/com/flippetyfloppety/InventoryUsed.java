package com.flippetyfloppety;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.sql.ResultSet;

/**
 * Created by Jeanie on 4/2/2016.
 */

public class InventoryUsed extends JFrame implements TableModelListener {
    private JList inventoryList;
    private JTextField amntUsed;
    private JTable inventoryTable;
    private JPanel updateInv;
    private GUIHelper guiHelper;
    private DatabaseSetup db;
    private JFrame mainFrame;

    public InventoryUsed(DatabaseSetup db) {
        guiHelper = new GUIHelper(db);
        this.db = db;
        mainFrame = new JFrame("InventoryUsed");
        getContentPane().add(updateInv);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        fillInvTable();

        inventoryTable.getModel().addTableModelListener(this);

    }

    public void tableChanged(TableModelEvent e) {
        try {
            int row = e.getFirstRow();

            int column = e.getColumn();
            TableModel model = (TableModel) e.getSource();
            String iid = (model.getValueAt(row, 0).toString());
            String columnName = model.getColumnName(column).toLowerCase();
            if (columnName.equals("iid") || columnName.equals("iname")) {
                JOptionPane.showMessageDialog(mainFrame, "You may not edit the iid or iname column", "Error!", JOptionPane.ERROR_MESSAGE);
                mainFrame.invalidate();
                fillInvTable();
                return;
            }
            String newVal;

//        if (!columnName.equals("datec") && !columnName.equals("iname") && !columnName.equals("iloc")) {
            newVal = model.getValueAt(row, column).toString();
//        }
            System.out.println(newVal);

            String query = "UPDATE inventory SET " + columnName + "=" + newVal + " where iid = '" + iid + "'";
            System.out.println(query);
            db.executeSQLQuery(query);
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            // do nothing == this happens when we redraw the table
        }


    }

    private void fillInvTable() {
        String invQuery = "SELECT * FROM inventory order by iname asc";
        ResultSet rs = db.executeSQLQuery(invQuery);
        guiHelper.fillTable(rs, inventoryTable);
    }
}
