package com.flippetyfloppety;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Created by Jeanie on 3/25/2016.
 */
public class MainPage extends JFrame {
    private JTabbedPane inventoryPane;
    private JTextArea iSearchQuery;
    private JButton iSearchBtn;
    private JTextField eSearchQuery;
    private JButton eSearchBtn;
    private JTextField rsid;
    private JPasswordField rpwd;
    private JButton createAccBtn;
    private JTextField rname;
    private JTabbedPane statsTabs;
    private JTable resultTable;
    private JButton viewConsumablesButton;
    private JComboBox filterComboBox;
    private JTable filterResultsTable;
    private JFrame mainFrame;
    private JPanel inventory;
    private JPanel experiment;
    private JPanel statistics;
    private JPanel userSettings;

    private int user;


    public MainPage(int userType, DatabaseSetup db) {
        System.out.println("In Main Page");
        this.user = userType;

        iSearchBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when inventory search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        eSearchBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when experiment search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        mainFrame = new JFrame("Main");
        getContentPane().add(inventoryPane);
        if (this.user == Login.RESEARCHER) {
            inventoryPane.setEnabledAt(2, false);
            inventoryPane.setEnabledAt(3, false);
        }
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        createAccBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when create account button is pressed
             * adds a new researcher tuple to the database
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = db.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO researcher VALUES (?, ?, ?)");
                    ps.setString(1, rsid.getText().toLowerCase());
                    ps.setString(2, rname.getText());
                    ps.setString(3, String.valueOf(rpwd.getPassword()));

                    ResultSet rs = ps.executeQuery();
                    // alert the user that the insert was succesful
                    JOptionPane.showMessageDialog(mainFrame, "Researcher " + rsid.getText().toLowerCase() + " has succesfully been added to the database!");
                } catch (SQLException ce) {
                    // error code 00001 = unique tuple check failed
                    if (ce.getMessage().substring(4, 9).equals("00001")) {
                        JOptionPane.showMessageDialog(mainFrame, "Researcher already exists in database! Please login with appropriate credentials.", "Error!", JOptionPane.ERROR_MESSAGE);
                    } else if (ce.getMessage().substring(4,0).equals("01438")) {
                        JOptionPane.showMessageDialog(mainFrame, "ID's must be less than 10 characters long. Names and passwords must be less than 40 characters long.", "Error!", JOptionPane.ERROR_MESSAGE);
                    } else JOptionPane.showMessageDialog(mainFrame, ce.getMessage());
                } finally {
                    // clear the user input fields
                    rsid.setText("");
                    rname.setText("");
                    rpwd.setText("");
                }
            }
        });
        viewConsumablesButton.addActionListener(new ActionListener() {
            /**
             * Invoked when view consumables button on statistics tab is pressed.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String filter = filterComboBox.getSelectedItem().toString();
                    String quantity = "";
                    if (filter.equals("Low Quantity")) {
                        quantity = "< 50";
                    } else if (filter.equals("None Remaining")) {
                        quantity = " = 0";
                    }
                    String query = "SELECT * FROM consumable NATURAL JOIN inventory WHERE amnt " + quantity;
                    Connection con = db.getConnection();
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    // code adapted from http://stackoverflow.com/questions/29662235/how-to-get-jtable-data-to-update-from-resultset?rq=1
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
                        // code adapted from http://www.coderanch.com/t/491763/GUI/java/Adding-row-existing-Swing-JTable
                        DefaultTableModel model = (DefaultTableModel) filterResultsTable.getModel();
                        model.setDataVector(data, columnNames);

                        // code adapted from http://stackoverflow.com/questions/8216116/name-columns-in-a-jtable
                        for (int k = 0; k < numColumns; k++) {
                            TableColumn tc = filterResultsTable.getColumnModel().getColumn(k);
                            tc.setHeaderValue(columnNames.get(k));
                        }
                    }

                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        });
    }
}
