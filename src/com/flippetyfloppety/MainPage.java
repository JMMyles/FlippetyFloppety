package com.flippetyfloppety;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;
import java.util.Vector;

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
    private JList columnList;
    private JComboBox sortByComboBox;
    private JComboBox orderByComboBox;
    private JList iColumnList;
    private JTable iFilterResultsTable;
    private JButton inspectionLogBtn;
    private JList bColumnList;
    private JComboBox bOrderByComboBox;
    private JComboBox bSortByComboBox;
    private JButton viewBreakdownsButton;
    private JTable bFilterResultsTable;
    private JTextArea numResearchers;
    private JTextArea numSupervisors;
    private JTextArea numExperiments;
    private JTextArea mostSuper;
    private JTextArea numMostSuper;
    private JTextArea leastSuper;
    private JTextArea numLeastSuper;
    private JTextArea mostSupplier;
    private JTextArea avgBreakdown;
    private JButton calcNumResearchers;
    private JButton calcNumSupervisors;
    private JButton calcNumExp;
    private JButton calcMostInspec;
    private JButton calcLeastInspec;
    private JButton calcMostSupplier;
    private JButton calcAvgBreakdown;
    private JTextArea superAllMachines;
    private JButton calcSuperAllInspected;
    private JFrame mainFrame;
    private JPanel inventory;
    private JPanel experiment;
    private JPanel statistics;
    private JPanel userSettings;

    private int user;
    private DatabaseSetup db;

    public MainPage(int userType, DatabaseSetup db) {
        System.out.println("In Main Page");
        this.user = userType;
        this.db = db;

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
        } else {
            inventoryPane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent changeEvent) {

                    // set projection options in URGENT tab
                    columnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    columnList.setModel(new DefaultListModel());
                    DefaultListModel uModel = (DefaultListModel)columnList.getModel();

                    // set projection options in INSPECTION tab
                    iColumnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    iColumnList.setModel((new DefaultListModel()));
                    DefaultListModel iModel = (DefaultListModel) iColumnList.getModel();

                    // set projection options in BREAJDIWNS tab
                    bColumnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    bColumnList.setModel((new DefaultListModel()));
                    DefaultListModel bModel = (DefaultListModel) bColumnList.getModel();

                    uModel.removeAllElements();
                    // GET ALL COLUMN NAMES FOR CONSUMABLE INVENTORY JOIN
                    String urgentQuery = "SELECT * FROM consumable NATURAL JOIN inventory";
                    fillProjectionList(uModel, urgentQuery);

                    iModel.removeAllElements();
                    // GET ALL COLUMN NAMES FROM INSPECTION , MACHINERY, EQUIPMENT JOIN
                    String inspectionQuery = "SELECT * FROM inspection NATURAL JOIN machinery NATURAL JOIN equipment " +
                            " NATURAL JOIN inventory NATURAL JOIN rinspectm NATURAL JOIN SUPERVISOR";
                    fillProjectionList(iModel, inspectionQuery);

                    bModel.removeAllElements();
                    String breakdownQuery = "SELECT * FROM breakdown NATURAL JOIN machinery NATURAL JOIN equipment " +
                            " NATURAL JOIN inventory ";
                    fillProjectionList(bModel, breakdownQuery);
                }
            });
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
                    // alert the user that the insert was successful
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

                String filter = filterComboBox.getSelectedItem().toString();
                String quantity = "";
                if (filter.equals("Low Quantity")) {
                    quantity = "< 50";
                } else if (filter.equals("None Remaining")) {
                    quantity = " = 0";
                }

                String proj = getProjectedAttributes(columnList);

                String query = "SELECT " + proj + " FROM consumable NATURAL JOIN inventory WHERE amnt " + quantity;

                ResultSet rs = db.executeSQLQuery(query);

                fillTable(rs, filterResultsTable);
            }
        });

        inspectionLogBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String sortBy = sortByComboBox.getSelectedItem().toString().toLowerCase();
                String groupBy = orderByComboBox.getSelectedItem().toString().toLowerCase();
                String sqlGroup = "";
                if (groupBy.equals("item")) {
                    sqlGroup = "ORDER BY iid";
                } else if (groupBy.equals("all")) {
                    sqlGroup = "";
                    sortBy = "";
                } else if (groupBy.equals("inspector")) {
                    sqlGroup = "ORDER BY sname";
                } else if (groupBy.equals("date")) {
                    sqlGroup = "ORDER BY dateInspected";
                } else {
                    sortBy = "";
                }

                String proj = getProjectedAttributes(iColumnList);

                String query = "SELECT " + proj + " FROM inspection NATURAL JOIN machinery NATURAL JOIN equipment " +
                        " NATURAL JOIN inventory NATURAL JOIN rinspectm NATURAL JOIN SUPERVISOR " + sqlGroup + " " + sortBy;
                System.out.println("Query = " + query);
                ResultSet rs = db.executeSQLQuery(query);
                fillTable(rs, iFilterResultsTable);

            }
        });
        viewBreakdownsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String sortBy = bSortByComboBox.getSelectedItem().toString().toLowerCase();
                String groupBy = bOrderByComboBox.getSelectedItem().toString().toLowerCase();
                String sqlGroup = "";
                if (groupBy.equals("machine")) {
                    sqlGroup = "ORDER BY iname";
                } else if (groupBy.equals("date")) {
                    sqlGroup = "ORDER BY breakdownDate";
                } else if (groupBy.equals("all")) {
                    sqlGroup = "";
                    sortBy = "";
                } else {
                    sortBy = "";
                }

                String proj = getProjectedAttributes(bColumnList);
                String query = "SELECT " + proj + " FROM breakdown NATURAL JOIN machinery NATURAL JOIN equipment " +
                        " NATURAL JOIN inventory " + sqlGroup + " " + sortBy;
                System.out.println(query);
                ResultSet rs = db.executeSQLQuery(query);
                fillTable(rs, bFilterResultsTable);
            }
        });
    }

    private void fillProjectionList(DefaultListModel model, String query) {
        model.removeAllElements();
        // GET ALL COLUMN NAMES FROM INVENTORY CONSUMABLE JOIN
        try {
            ResultSet rs = this.db.executeSQLQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if (!metaData.getColumnLabel(i).toLowerCase().contains("pwd")) {
                    model.addElement(metaData.getColumnLabel(i));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }
    private String getProjectedAttributes(JList list) {
        String proj = "";
        List<String> projection = list.getSelectedValuesList();

        if (projection.size() == 0) {
            DefaultListModel dlm = (DefaultListModel) list.getModel();
            proj = dlm.toString().substring(1, dlm.toString().length() - 1);
//            proj = "*";
        } else {
            proj = projection.get(0);
            for (int i = 1; i < projection.size(); i++) {
                proj = proj.concat(", " + projection.get(i).toLowerCase());
            }
        }
        System.out.println("proj = " + proj);
        return proj;
    }

    private void fillTable(ResultSet rs, JTable table) {
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
            System.out.println("tabled filled");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
