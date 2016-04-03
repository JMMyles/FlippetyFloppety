package com.flippetyfloppety;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private int booknum;
    private java.sql.Date expdate;

    public InventoryUsed(DatabaseSetup db, int booknum, java.sql.Date date) {
        guiHelper = new GUIHelper(db);
        this.db = db;
        this.booknum = booknum;
        this.expdate = date;
        inventoryTable.getTableHeader().setReorderingAllowed(false);
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
            float qtyUsed = 0;
            String query;

            int row = e.getFirstRow();
            int column = e.getColumn();

            TableModel model = (TableModel) e.getSource();
            String iid = (model.getValueAt(row, 0).toString());
            String columnName = model.getColumnName(column).toLowerCase();
            String newVal = model.getValueAt(row, column).toString();

            if (columnName.equals("iid") || columnName.equals("iname")) {
                JOptionPane.showMessageDialog(mainFrame, "You may not edit the iid or iname column", "Error!", JOptionPane.ERROR_MESSAGE);
                mainFrame.invalidate();
                fillInvTable();
                return;
            } else if (columnName.equals("qnty")) {
                // get current quantity level to calculate amount used in experiment
                query = "SELECT qnty FROM inventory WHERE iid=" + iid;
                ResultSet rs = db.executeSQLQuery(query);
                rs.next();

                float oldQnty = rs.getFloat("qnty");
                float newQnty = Float.parseFloat(newVal);
                qtyUsed = oldQnty - newQnty;

                // check constraint on table for mysql 5.5 does not work
                if (newQnty < 0) {
                    JOptionPane.showMessageDialog(mainFrame, "Quantity must be a positive value", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // update inventory with new lastChecked date and new quantity to reflect changes
            query = "UPDATE inventory SET " + columnName + "=" + newVal + ", datec=? where iid=" + iid;
            PreparedStatement ps = db.getConnection().prepareStatement(query);
            ps.setDate(1, expdate);
            ps.executeUpdate();

            try {
                ps = db.getConnection().prepareStatement("INSERT INTO eusesi (booknum, cdate, iid, qtyUsed) VALUES (" + booknum + ",?," + iid + ", " + qtyUsed + ")");
                ps.setDate(1, expdate);
                ps.execute();
            } catch (SQLException sqle) {
                System.out.println(booknum);
                System.out.println(iid);
                System.out.println(qtyUsed);
                System.out.println(expdate);

                guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
            }
            // update rupdatesi table
            // first get rsid associated with booknum
            query = "SELECT rsid FROM researcher NATURAL JOIN labbook WHERE booknum=" + booknum;
            ResultSet rs = db.executeSQLQuery(query);
            rs.next();

            String rsid = rs.getString("rsid");

            query = "INSERT INTO rupdatei (rsid, iid, lastchecked) values (?, ?, ?)";
            ps = db.getConnection().prepareStatement(query);
            ps.setString(1, rsid);
            ps.setInt(2, Integer.parseInt(iid));
            ps.setDate(3, expdate);
            ps.executeUpdate();

        } catch (ArrayIndexOutOfBoundsException aiobe) {
            // do nothing == this happens when we redraw the table
        } catch (SQLException sqle) {
            guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
        }


    }

    private void fillInvTable() {
        String invQuery = "SELECT * FROM inventory order by iname asc";
        ResultSet rs = db.executeSQLQuery(invQuery);
        guiHelper.fillTable(rs, inventoryTable);
    }
}
