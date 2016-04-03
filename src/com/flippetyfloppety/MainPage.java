package com.flippetyfloppety;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;


/**
 * Created by Jeanie on 3/25/2016.
 */
public class MainPage extends JFrame {
    private JTabbedPane inventoryPane;
    private JTextArea iSearchQuery;
    private JButton iSearchBtn;
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
    private JTextField searchQuery;
    private JButton eSearchButton;
    private JTable expSearchResults;
    private JFrame mainFrame;
    private JPanel inventory;
    private JPanel experiment;
    private JPanel statistics;
    private JPanel userSettings;
    //private JComboBox inventoryFilterComboBox;
    private JList inventoryColumnList;
    private JTable inventoryFilterResultsTable;
    private JTabbedPane inventory2Pane;
    private JFrame headFrame;
    private JTextArea inventorySearchQuery;

    private JButton iAntiSearchBtn;
    private JButton eAntiSearchBtn;
    private JTable eFilterResultsTable;

    private JTextField expName;
    private JFormattedTextField expDate;
    private JTextField booknum;
    private JTextField pagenum;
    private JButton insertNewExperimentButton;
    private JButton amountEqualsBtn;
    private JButton amountLessBtn;
    private JButton amountGreaterBtn;

    private int user;
    private DatabaseSetup db;

    public MainPage(int userType, DatabaseSetup db) {
        System.out.println("In Main Page");
        this.user = userType;
        this.db = db;

        mainFrame = new JFrame("Main");
        getContentPane().add(inventoryPane);
        if (this.user == Login.RESEARCHER) {
            inventoryPane.setEnabledAt(2, false);
            inventoryPane.setEnabledAt(3, false);
        } else {

            inventoryPane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent changeEvent) {
                    // set up date format in input field


//                        DateFormatter date = new DateFormatter("yyyy-MM-dd");
//                        DefaultFormatterFactory factory = new DefaultFormatterFactory(date);

                        expDate.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("yyyy-MM-dd"))));



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

                    // GET ALL COLUMN NAMES FOR CONSUMABLE INVENTORY JOIN
                    String urgentQuery = "SELECT * FROM consumable NATURAL JOIN inventory";
                    fillProjectionList(uModel, urgentQuery);

                    // GET ALL COLUMN NAMES FROM INSPECTION , MACHINERY, EQUIPMENT JOIN
                    String inspectionQuery = "SELECT * FROM inspection NATURAL JOIN machinery NATURAL JOIN equipment " +
                            " NATURAL JOIN inventory NATURAL JOIN rinspectm NATURAL JOIN SUPERVISOR";
                    fillProjectionList(iModel, inspectionQuery);

                    String breakdownQuery = "SELECT * FROM breakdown NATURAL JOIN machinery NATURAL JOIN equipment " +
                            " NATURAL JOIN inventory ";
                    fillProjectionList(bModel, breakdownQuery);
                }
            });
        }
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        iSearchBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when inventory search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String invItem = inventorySearchQuery.getText().toLowerCase();

                String query = "SELECT * FROM inventory WHERE iname LIKE '%" + invItem + "%'";

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    fillTable(rs, inventoryFilterResultsTable);
                }
            }
        });
        iAntiSearchBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when Anti-inventory search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String invItem = inventorySearchQuery.getText().toLowerCase();

                String query = "SELECT * FROM inventory WHERE iname NOT LIKE '%" + invItem + "%'";

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    fillTable(rs, inventoryFilterResultsTable);
                }
            }
        });
        amountEqualsBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when amount equals search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String invAmount = inventorySearchQuery.getText().toLowerCase();

                String query = "SELECT * FROM inventory WHERE qty = " + invAmount;

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    fillTable(rs, inventoryFilterResultsTable);
                }
            }
        });
        amountLessBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when amount less search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String invAmount = inventorySearchQuery.getText().toLowerCase();

                String query = "SELECT * FROM inventory WHERE qty < " + invAmount;

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    fillTable(rs, inventoryFilterResultsTable);
                }
            }
        });
       amountGreaterBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when amount greater search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String invAmount = inventorySearchQuery.getText().toLowerCase();

                String query = "SELECT * FROM inventory WHERE qty > " + invAmount;

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    fillTable(rs, inventoryFilterResultsTable);
                }
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

                String eItem = searchQuery.getText().toLowerCase();

                String query = "select * from experiment NATURAL JOIN labbook where booknum like '%" + eItem + "%' or ename like '%" + eItem + "%'";

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    fillTable(rs, expSearchResults);
                }
            }
        });
        eAntiSearchBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when Anti-experiment search button is pressed
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String eItem = searchQuery.getText().toLowerCase();

                String query = "SELECT * FROM experiment WHERE ename NOT LIKE '%" + eItem + "%'";

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    fillTable(rs, expSearchResults);
                }
            }
        });

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

                String query = "SELECT * FROM consumable NATURAL JOIN inventory WHERE amnt " + quantity;

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
        calcNumResearchers.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "SELECT COUNT(*) AS numResearchers FROM researcher";
                    ResultSet rs = db.executeSQLQuery(query);
                    rs.next();
                    numResearchers.setText(String.valueOf(rs.getInt("numResearchers")));
                } catch (SQLException sqle) {
                    showErrorDialog(sqle.getMessage());
                }

            }
        });
        calcNumSupervisors.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "SELECT COUNT(*) AS numSuper FROM supervisor";
                    ResultSet rs = db.executeSQLQuery(query);
                    rs.next();
                    numSupervisors.setText(String.valueOf(rs.getInt("numSuper")));
                } catch (SQLException sqle) {
                    showErrorDialog(sqle.getMessage());
                }

            }
        });
        calcNumExp.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "SELECT COUNT(*) AS numExp FROM experiment";
                    ResultSet rs = db.executeSQLQuery(query);
                    rs.next();
                    numExperiments.setText(String.valueOf(rs.getInt("numExp")));
                } catch (SQLException sqle) {
                    showErrorDialog(sqle.getMessage());
                }

            }
        });
        calcMostInspec.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select max(numInspections) as maxNum from (select count(*) as numInspections, ssid from rinspectm group by ssid)";
                    ResultSet rs = db.executeSQLQuery(query);

                    rs.next();
                    mostSuper.setText("");
                    numMostSuper.setText("");
                    int maxNum = rs.getInt("maxNum");
                    numMostSuper.setText(String.valueOf(maxNum));

                    query = "select sname from (select count(*) as maxNum, sname from rinspectm natural join supervisor group by sname) where maxNum = " + maxNum;
                    rs = db.executeSQLQuery(query);
                    rs.next();
                    mostSuper.setText(rs.getString("sname"));
                } catch (SQLException sqle) {
                    showErrorDialog(sqle.getMessage());
                }

            }
        });
        calcLeastInspec.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select min(numInspections) as minNum from (select count(*) as numInspections, ssid from rinspectm group by ssid)";
                    ResultSet rs = db.executeSQLQuery(query);

                    rs.next();
                    leastSuper.setText("");
                    numLeastSuper.setText("");
                    int minNum = rs.getInt("minNum");
                    numLeastSuper.setText(String.valueOf(minNum));

                    query = "select sname from (select count(*) as minNum, sname from rinspectm natural join supervisor group by sname) where minNum = " + minNum;
                    rs = db.executeSQLQuery(query);
                    rs.next();
                    leastSuper.setText(rs.getString("sname"));
                } catch (SQLException sqle) {
                    showErrorDialog(sqle.getMessage());
                }

            }
        });
        calcSuperAllInspected.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select sname from supervisor s where not exists (select * from machinery m where not exists(select * from rinspectm i where i.ssid=s.ssid and i.iid=m.iid))";
                    ResultSet rs = db.executeSQLQuery(query);
                    rs.next();
                    superAllMachines.setText("");
                    superAllMachines.setText(rs.getString("sname"));
                } catch (SQLException sqle) {
                    showErrorDialog(sqle.getMessage());
                }

            }
        });
        calcMostSupplier.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String query = "create or replace" +
                            " view groupedCount as select count(*) as supplierCount, supplier from productinfo group by supplier";
                    db.executeSQLQuery(query);
                    query = "select supplier from groupedCount where supplierCount = (select max(supplierCount) from groupedCount)";
                    ResultSet rs = db.executeSQLQuery(query);
                    rs.next();
                    mostSupplier.setText("");
                    mostSupplier.setText(rs.getString("supplier"));
                } catch (SQLException sqle) {
                    showErrorDialog(sqle.getMessage());
                }
            }
        });
        calcAvgBreakdown.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             * TODO: average is 0 when it should be 0.2 -- type conversion?
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select min(breakdownDate) as oldest, max(breakdownDate) as recent, count(*) as numBreakdowns from breakdown order by breakdownDate ASC";
                    ResultSet rs = db.executeSQLQuery(query);
                    rs.next();

                    int oldest = Integer.parseInt(rs.getString("oldest").substring(2,4));
                    System.out.println("oldest date = " + oldest);
                    int recent = Integer.parseInt(rs.getString("recent").substring(2,4));
                    System.out.println("recent date = " + recent);
                    int num = rs.getInt("numBreakdowns");

                    int numerator = recent - oldest;
                    System.out.println("numerator = " + numerator);

                    float division = (float) numerator/num;
                    System.out.println("division = " + division);

                    avgBreakdown.setText(Float.toString(division));
                } catch (SQLException sqle) {
                    showErrorDialog(sqle.getMessage());
                }
            }
        });

        insertNewExperimentButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

//                expDate.setFormat(displayFormatter);
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
            showErrorDialog(sqle.getMessage());
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

    // code adapted from http://stackoverflow.com/questions/29662235/how-to-get-jtable-data-to-update-from-resultset?rq=1
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
            System.out.println("tabledfilled");
        } catch (SQLException sqle) {
            showErrorDialog(sqle.getMessage());
        }
    }

    private void showErrorDialog(String errorMsg) {
        JOptionPane.showMessageDialog(mainFrame, errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
    }


}
