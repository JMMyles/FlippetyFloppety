package com.flippetyfloppety;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * Created by Jeanie on 4/2/2016.
 */
public class GUIHelper {
//    public GUIHelpher(){}
    private DatabaseSetup db;
    public GUIHelper(DatabaseSetup database) {
        this.db = database;
    }
    public void fillProjectionList(DefaultListModel model, String query) {
        model.removeAllElements();
        // GET ALL COLUMN NAMES FROM INVENTORY CONSUMABLE JOIN
        try {
            ResultSet rs = db.executeSQLQuery(null, query);

            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if (!metaData.getColumnLabel(i).toLowerCase().contains("pwd")) {
                    model.addElement(metaData.getColumnLabel(i));
                }
            }
        } catch (SQLException sqle) {

        }
    }

    public String getProjectedAttributes(JList list) {
        String proj = "";
        List<String> projection = list.getSelectedValuesList();

        if (projection.size() == 0) {
            DefaultListModel dlm = (DefaultListModel) list.getModel();
            proj = dlm.toString().substring(1, dlm.toString().length() - 1);

        } else {
            proj = projection.get(0);
            for (int i = 1; i < projection.size(); i++) {
                proj = proj.concat(", " + projection.get(i).toLowerCase());
            }
        }
        System.out.println("proj = " + proj);
        return proj;
    }

    // code adapted from http://stackoverflow.com/questions/29662235/how-to-get-jtable-data-to-update-from-resultset?rq=1
    public void fillTable(ResultSet rs, JTable table) {
        try {

            ResultSetMetaData metaData = rs.getMetaData();
            int numColumns = metaData.getColumnCount();
            if (numColumns > 0) {
                Vector<String> columnNames = new Vector<String>();
                for (int i = 1; i <= numColumns; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                Vector<Vector<Object>> data = new Vector<Vector<Object>>();
                while (rs.next()) {
                    Vector<Object> rowVal = new Vector<Object>();
                    for (int j = 1; j <= numColumns; j++) {
                        rowVal.add(rs.getObject(j));
                    }
                    data.add(rowVal);
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setDataVector(data, columnNames);

                for (int k = 0; k < numColumns; k++) {
                    TableColumn tc = table.getColumnModel().getColumn(k);
                    tc.setHeaderValue(columnNames.get(k));
                }
            }
            System.out.println("tabledfilled");
        } catch (SQLException sqle) {

        }
    }

    public void showErrorDialog(JFrame mainFrame, String errorMsg) {
        JOptionPane.showMessageDialog(mainFrame, errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
    }

    // get column index where string column matches
    public int getColumnIndex(JTable table, String columnTitle) {
        int columnCount = table.getColumnCount();

        for (int column = 0; column < columnCount; column++) {
            if (table.getColumnName(column).equalsIgnoreCase(columnTitle)) {
                return column;
            }
        }

        return -1;
    }
}
