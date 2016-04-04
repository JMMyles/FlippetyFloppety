package com.flippetyfloppety;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jeanie on 4/3/2016.
 */
public class InsertLabCreated extends JFrame {
    private JLabel title;
    private JTextField name;
    private JTextField qtyCreated;
    private JTextField unitsCreated;
    private JButton insertButton;
    private JTextField newLoc;
    private JPanel insertLC;
    private JFrame mainFrame;

    private int booknum;
    private java.sql.Date expdate;
    private DatabaseSetup db;
    private GUIHelper guiHelper;
    public InsertLabCreated(DatabaseSetup db, int booknum, java.sql.Date sqldate) {

        this.db = db;
        this.booknum = booknum;
        this.expdate = sqldate;
        this.guiHelper = new GUIHelper(db);

        title.setText("Insert New Lab Created Item");
        mainFrame = new JFrame("NewLabCreated");
        getContentPane().add(insertLC);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        insertButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String newItemName = name.getText();
                float qty = Float.parseFloat(qtyCreated.getText());
                String units = unitsCreated.getText();
                String loc = newLoc.getText();

                try {
                    // insert into inventory table
                    PreparedStatement ps = db.getConnection().prepareStatement("INSERT INTO inventory VALUES (inc_iid.nextval,'" + loc + "',?," + qty +  ",'" + newItemName + "')");
                    ps.setDate(1, expdate);
                    ps.executeUpdate();

                    // get iid we just entered
                    String query = "SELECT inc_iid.currval from inventory";
                    ResultSet rs = db.executeSQLQuery(query);
                    rs.next();
                    int newIid = rs.getInt(1);

                    System.out.println(newIid);

                    // insert into labcreated table
                    ps = db.getConnection().prepareStatement("INSERT INTO labcreated (iid, amnt, units, booknum, datelc) VALUES (?,?,?,?,?)");
                    ps.setInt(1, newIid);
                    ps.setFloat(2, qty);
                    ps.setString(3, units);
                    ps.setInt(4, booknum);
                    ps.setDate(5, expdate);

                    ps.executeUpdate();

                    // insert into rcreatesi table
                    // first get rsid associated with booknum
                    query = "SELECT rsid FROM researcher NATURAL JOIN labbook WHERE booknum=" + booknum;
                    rs = db.executeSQLQuery(query);
                    rs.next();

                    String rsid = rs.getString("rsid");

                    query = "INSERT INTO rcreatesi (rsid, iid) VALUES ('" + rsid + "'," + newIid + ")";
                    db.executeSQLQuery(query);

                    // As I just entered it, I am also the last person to update this new inventory item, update the table

                    query = "INSERT INTO rupdatei (rsid, iid, lastchecked) values (?, ?, ?)";
                    ps = db.getConnection().prepareStatement(query);
                    ps.setString(1, rsid);
                    ps.setInt(2, newIid);
                    ps.setDate(3, expdate);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(mainFrame, "Quantity updated succesfully");

                } catch (SQLException sqle) {
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
                }

            }
        });

        addWindowListener(new WindowAdapter() {
           @Override
            public void windowClosing(WindowEvent e) {
               int invUsed = JOptionPane.showConfirmDialog(null, "Did you use any items from inventory?", "Inventory Used?", JOptionPane.YES_NO_OPTION);
               if (invUsed == JOptionPane.YES_OPTION) {
                   InventoryUsed inv = new InventoryUsed(db, booknum, expdate);
                   inv.setVisible(true);
               }
               e.getWindow().dispose();
           }
        });
    }
}
