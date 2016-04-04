package com.flippetyfloppety;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jes97210 on 4/3/16.
 */
public class InsertEquipment extends JFrame {

    private DatabaseSetup db;
    private GUIHelper guiHelper;
    private JTabbedPane mname;
    private JTextField msupplier_in;
    private JButton mcreate;
    private JFormattedTextField cname_in;
    private JFormattedTextField cloc_in;
    private JFormattedTextField cqnty_in;
    private JFormattedTextField camnt_in;
    private JFormattedTextField csupplier_in;
    private JFormattedTextField cordnum_in;
    private JButton ccreate;
    private JLabel mloc;
    private JLabel mqnty;
    private JFormattedTextField cdate_in;
    private JFormattedTextField mdate_in;
    private JPanel newE;
    private JTextField mname_in;
    private JTextField mloc_inc;
    private JTextField mqty_in;
    private JTextField mordnum_in;
    private JTextField msernum_in;
    private JFrame mainFrame;

    public InsertEquipment(DatabaseSetup db) {
        this.db = db;
        this.guiHelper = new GUIHelper(db);

        mainFrame = new JFrame("NewEquipment");
        getContentPane().add(newE);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        mcreate.addActionListener(new ActionListener() {
            // @param e
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = mname_in.getText();

                String cdate = mdate_in.getText();
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
                java.sql.Date sqldate = null;
                try {
                    Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(cdate);
                    sqldate = new java.sql.Date(utilDate.getTime());
                }
                catch (ParseException pe) {
                    guiHelper.showErrorDialog(mainFrame, pe.getMessage());
                }

                float qty = Float.parseFloat(mqty_in.getText());
                String loc = mloc_inc.getText();
                String sup = msupplier_in.getText();
                String ord = mordnum_in.getText();
                String ser = msernum_in.getText();

                try {
                    // insert into inventory table
                    PreparedStatement ps = db.getConnection().prepareStatement("INSERT INTO inventory VALUES (inc_iid.nextval,'" + loc + "',?," + qty + ",'" + newName + "')");
                    ps.setDate(1, sqldate);
                    ps.executeUpdate();

                    // get iid just entered
                    String query = "SELECT inc_iid.currval from inventory";
                    ResultSet rs = db.executeSQLQuery(query);
                    rs.next();
                    int newIid = rs.getInt(1);

                    System.out.println(newIid);

                    // insert into productinfo
                    ps = db.getConnection().prepareStatement("INSERT INTO productinfo (supplier, ordernum) VALUES (?,?)");
                    ps.setString(1, sup);
                    ps.setString(2, ord);

                    ps.executeUpdate();

                    // insert into equipment table
                    ps = db.getConnection().prepareStatement("INSERT INTO equipment (iid, supplier, ordernum) VALUES (?,?,?)");
                    ps.setInt(1, newIid);
                    ps.setString(2, sup);
                    ps.setString(3, ord);

                    ps.executeUpdate();

                    // insert into machinery table
                    ps = db.getConnection().prepareStatement("INSERT INTO machinery (iid, serialnum) VALUES (?,?)");
                    ps.setInt(1, newIid);
                    ps.setString(2, ser);

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(mainFrame, "Machinery inserted successfully!");
                }catch (SQLException sqle) {
                    guiHelper.showErrorDialog(mainFrame, sqle.getMessage());
                }

            }
        });
    }
}
