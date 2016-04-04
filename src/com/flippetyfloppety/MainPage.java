package com.flippetyfloppety;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jeanie on 3/25/2016.
 */
public class MainPage extends JFrame {
    private GUIHelper guiHelper;
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
    private JTextField associateOneQuery;
    private JComboBox associateComboBox;
    private JTextField associateTwoQuery;
    private JButton associateSearchBtn;
    private JButton newEquipmentButton;
    private JScrollBar scrollBar1;
    private JComboBox greaterLessCombo;

    private int user;
    private DatabaseSetup db;

    public MainPage(int userType, DatabaseSetup db) {
        this.guiHelper = new GUIHelper(db);
        System.out.println("In Main Page");
        this.user = userType;
        this.db = db;

        mainFrame = new JFrame("Main");

        disabledEditTable(expSearchResults);
        disabledEditTable(inventoryFilterResultsTable);
        disabledEditTable(filterResultsTable);
        disabledEditTable(iFilterResultsTable);
        disabledEditTable(bFilterResultsTable);


        getContentPane().add(inventoryPane);


        if (this.user == Login.RESEARCHER) {
            inventoryPane.setEnabledAt(2, false);
            inventoryPane.setEnabledAt(3, false);
            newEquipmentButton.setEnabled(false);
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

                    // GET ALL COLUMN NAMES FOR CONSUMABLE INVENTORY JOIN
                    String urgentQuery = "SELECT * FROM consumable NATURAL JOIN inventory";
                    guiHelper.fillProjectionList(uModel, urgentQuery);

                    // GET ALL COLUMN NAMES FROM INSPECTION , MACHINERY, EQUIPMENT JOIN
                    String inspectionQuery = "SELECT * FROM inspection NATURAL JOIN machinery NATURAL JOIN equipment " +
                            " NATURAL JOIN inventory NATURAL JOIN rinspectm NATURAL JOIN SUPERVISOR";
                    guiHelper.fillProjectionList(iModel, inspectionQuery);

                    String breakdownQuery = "SELECT * FROM breakdown NATURAL JOIN machinery NATURAL JOIN equipment " +
                            " NATURAL JOIN inventory ";
                    guiHelper.fillProjectionList(bModel, breakdownQuery);
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
                    guiHelper.fillTable(rs, inventoryFilterResultsTable);
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
                    guiHelper.fillTable(rs, inventoryFilterResultsTable);
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

                String query = "SELECT * FROM inventory WHERE qnty = " + invAmount;

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    guiHelper.fillTable(rs, inventoryFilterResultsTable);
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

                String query = "SELECT * FROM inventory WHERE qnty < " + invAmount;

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    guiHelper.fillTable(rs, inventoryFilterResultsTable);
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

                String query = "SELECT * FROM inventory WHERE qnty > " + invAmount;

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    guiHelper.fillTable(rs, inventoryFilterResultsTable);
                }
            }
        });
        associateSearchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String firstAssociate = associateOneQuery.getText().toLowerCase();
                String associateQuery = associateComboBox.getSelectedItem().toString().toLowerCase();
                String greaterLess = greaterLessCombo.getSelectedItem().toString().toLowerCase();
                String secondAssociate = associateTwoQuery.getText().toLowerCase();

                String query = "";
                if (associateQuery.equals("and")) {
                    query = "SELECT * FROM inventory WHERE iname LIKE '%" + firstAssociate + "%' AND qnty" + greaterLess + " " + secondAssociate;
                } else if (associateQuery.equals("or")) {
                    query = "SELECT * FROM inventory WHERE iname LIKE '%" + firstAssociate + "%' OR  qnty" + greaterLess + " " + secondAssociate;
                } else {
                    System.out.println("Error: Inappropriate association");
                }

                ResultSet rs = db.executeSQLQuery(query);
                guiHelper.fillTable(rs, inventoryFilterResultsTable);

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
                    guiHelper.fillTable(rs, expSearchResults);
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

                String query = "SELECT * FROM experiment WHERE ename NOT LIKE '%" + eItem + "%' or booknum not like '%" + eItem + "%'";

                ResultSet rs = db.executeSQLQuery(query);

                if (rs == null) {
                    System.out.println("Result is NULL");
                } else {
                    guiHelper.fillTable(rs, expSearchResults);
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

                String proj = guiHelper.getProjectedAttributes(columnList);

                String query = "SELECT * FROM consumable NATURAL JOIN inventory WHERE amnt " + quantity;

                ResultSet rs = db.executeSQLQuery(query);

                guiHelper.fillTable(rs, filterResultsTable);
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

                String proj = guiHelper.getProjectedAttributes(iColumnList);

                String query = "SELECT " + proj + " FROM inspection NATURAL JOIN machinery NATURAL JOIN equipment " +
                        " NATURAL JOIN inventory NATURAL JOIN rinspectm NATURAL JOIN SUPERVISOR " + sqlGroup + " " + sortBy;
                System.out.println("Query = " + query);
                ResultSet rs = db.executeSQLQuery(query);
                guiHelper.fillTable(rs, iFilterResultsTable);

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

                String proj = guiHelper.getProjectedAttributes(bColumnList);
                String query = "SELECT " + proj + " FROM breakdown NATURAL JOIN machinery NATURAL JOIN equipment " +
                        " NATURAL JOIN inventory " + sqlGroup + " " + sortBy;
                System.out.println(query);
                ResultSet rs = db.executeSQLQuery(query);
                guiHelper.fillTable(rs, bFilterResultsTable);
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
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
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
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
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
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
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
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
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
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
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
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
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
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
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
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
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
                String name = expName.getText();
                String cdate = expDate.getText();

                String[] splitDate = cdate.split("-");

                if (splitDate.length > 3) {
                    JOptionPane.showMessageDialog(mainFrame, "Make sure you enter your date in yyyy-mm-dd format", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {

                    if (splitDate[0].length() < 4 || Integer.parseInt(splitDate[0]) > Calendar.getInstance().get(Calendar.YEAR)) {
                        JOptionPane.showMessageDialog(mainFrame, "Invalid Year entered!", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (splitDate[1].length() < 2 || Integer.parseInt(splitDate[1]) > 12 || splitDate[2].length() < 2 || Integer.parseInt(splitDate[2]) > 31) {
                        JOptionPane.showMessageDialog(mainFrame, "Invalid Date entered! Remember leading zeros!", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                int labbooknum = 0;
                int labpagenum = 0;
                java.sql.Date sqldate = null;
                try {
                    Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(cdate);
                    sqldate = new java.sql.Date(utilDate.getTime());

                    labbooknum = Integer.parseInt(booknum.getText());
                    labpagenum = Integer.parseInt(pagenum.getText());

                    // PreparedStatemnt is needed to add a date format in mysql
                    PreparedStatement p = db.getConnection().prepareStatement("INSERT INTO experiment (booknum, cdate, pagenum, ename) VALUES (" + labbooknum + ",?, " + labpagenum + ", '" + name + "')");
                    p.setDate(1, sqldate);
                    p.execute();
                } catch (SQLException sqle) {
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
                } catch (ParseException pe) {
                    guiHelper.showErrorDialog(mainFrame, pe.getMessage());
                }

                int labcreated = JOptionPane.showConfirmDialog(null, "Did you create any items in the experiment?", "Lab Created from Experiment" , JOptionPane.YES_NO_OPTION);
                if (labcreated == JOptionPane.YES_OPTION) {
                    InsertLabCreated ilc = new InsertLabCreated(db, labbooknum, sqldate);
                    ilc.setVisible(true);
                }


                // clear the fields when the user returns
                expName.setText("");
                expDate.setText("");
                booknum.setText("");
                pagenum.setText("");
            }
        });
        expSearchResults.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table =(JTable) e.getSource();
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                // double click event
                if (e.getClickCount() == 2) {

                    int booknumCol = guiHelper.getColumnIndex(table, "booknum");
                    int cdateCol = guiHelper.getColumnIndex(table, "cdate");

                    Object booknumVal;
                    Object cdateVal;

                    try {
                        booknumVal = table.getValueAt(row, booknumCol);
                        cdateVal = table.getValueAt(row, cdateCol);
                    } catch (ArrayIndexOutOfBoundsException oob) {
                        // ignore, this happens when we are at the inventory natural join, we do not want to further select.
                        return;
                    }

                    // update the table to show all inventory items used in the experiment
                    try {

                        String query = "select booknum, qtyUsed, iid, ename, iname, iloc, qnty as qntyRemaining from eusesi natural join experiment natural join inventory where booknum=? and cdate=?";
                        PreparedStatement ps = db.getConnection().prepareStatement(query);
                        ps.setObject(1, booknumVal);
                        ps.setObject(2, cdateVal);
                        ResultSet rs = ps.executeQuery();
                        guiHelper.fillTable(rs, expSearchResults);
                    } catch (SQLException sqle) {
                        guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
                    }
                }
            }
        });

        // when inventory table row is clicked, go see who updated it last
        inventoryFilterResultsTable.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table =(JTable) e.getSource();
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                // double click event
                if (e.getClickCount() == 2) {

                    int iidCol = guiHelper.getColumnIndex(table, "iid");

                    Object iidVal;

                    try {
                        iidVal = table.getValueAt(row, iidCol);
                    } catch (ArrayIndexOutOfBoundsException oob) {
                        // ignore, this happens when we are at the rupdatei natural join, we do not want to further select.
                        return;
                    }

                    // update the table to show all inventory items used in the experiment
                    try {
                        String query = "select rsid, iid, iname, lastchecked, supplier, ordernum from rupdatei natural join inventory natural left join equipment where iid=?";
                        PreparedStatement ps = db.getConnection().prepareStatement(query);
                        ps.setObject(1, iidVal);
                        ResultSet rs = ps.executeQuery();
                        guiHelper.fillTable(rs, inventoryFilterResultsTable);
                    } catch (SQLException sqle) {
                        guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
                    }
                }
            }
        });

        newEquipmentButton.addActionListener( new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                InsertEquipment ie = new InsertEquipment(db);
                ie.setVisible(true);
            }

        });
    }



    // Disabled editing of tables on the whole main page form
    private void disabledEditTable(JTable table) {
        DefaultTableModel tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(tableModel);
    }

}
