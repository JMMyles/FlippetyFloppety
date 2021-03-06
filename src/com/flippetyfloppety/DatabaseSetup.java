package com.flippetyfloppety;

import javax.swing.*;
import java.sql.*;

public class DatabaseSetup {
    private Connection connection = null;

    public DatabaseSetup() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            String url = "jdbc:oracle:thin:@localhost:1522:ug";
            this.connection = DriverManager.getConnection(url, "ora_q2d9", "a32916132");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        return this.connection;
    }

    public boolean closeConnection() {
        try {
            this.connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet executeSQLQuery(JFrame mainFrame, String query) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            if (mainFrame != null) {
                GUIHelper gui = new GUIHelper(this);
                gui.showErrorDialog(mainFrame, e.getMessage());
            }

            return null;
        }
    }
}
